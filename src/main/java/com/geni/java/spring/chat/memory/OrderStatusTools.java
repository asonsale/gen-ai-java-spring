package com.geni.java.spring.chat.memory;

import com.geni.java.spring.chat.memory.dto.Order;
import com.geni.java.spring.chat.memory.dto.OrderStatus;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class OrderStatusTools {

    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    @PostConstruct
    public void init()
    {
        for(int i=0;i<=10;i++)
        {
            String orderId= "ORD-"+ i;
            Order order =new Order(
                    orderId,
                    i%2==0 ? "UPS":"FedEX",
                    OrderStatus.CREATED,
                    "user-"+i,
                    "User "+i);

            orders.put(orderId,order);

        }
    }

    @Tool
    public String getOrderStatus(@ToolParam(description = "OrderId")String orderId,@ToolParam(description = "userId") String userId)
    {
        Order order = orders.get(orderId);
        if(order !=null && !order.userId().equals(userId))
        {
            return "Order id" + orderId+ " does not belongs to "+ userId;
        }
        if(order==null)
        {
            return"No Order found for Id: "+  orderId;
        }

        OrderStatus currentStatus = order.status();
        OrderStatus nextStatus= nextStage(currentStatus);

        Order updateOrder= new Order(
                order.orderId(),
                order.carrier(),
                nextStatus,
                order.userId(),
                order.userName());
        orders.put(orderId, updateOrder);
        return "oreder "+ orderId+" For "+ order.userName()+
                " Carrier: " + order.carrier()+ " is currently  "+currentStatus;
    }

    private OrderStatus nextStage(OrderStatus currentStatus) {
        return switch(currentStatus){
            case CREATED -> OrderStatus.PROCESSING;
            case PROCESSING -> OrderStatus.SHIPPED;
            case SHIPPED -> OrderStatus.DELIVERED;
            case DELIVERED -> OrderStatus.DELIVERED; // stay at final stage
        };
    }
}
