package com.geni.java.spring.config;

import com.geni.java.spring.chat.advisor.ErrorAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.huggingface.HuggingfaceChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AIProviderConfig {

    @Bean("OpenAIChatClient")
    ChatClient openAIChatClient(OpenAiChatModel openAiChatModel, SimpleLoggerAdvisor simpleLoggerAdvisor, SafeGuardAdvisor safeGuardAdvisor, ErrorAdvisor errorAdvisor){
        return ChatClient.builder(openAiChatModel)
                        .defaultAdvisors(simpleLoggerAdvisor,safeGuardAdvisor(),errorAdvisor)
                        .build();
    }

    @Bean("HuggingFaceChatClient")
    ChatClient huggingFaceChatClient(HuggingfaceChatModel huggingFaceChatModel)
    {
         return ChatClient.builder(huggingFaceChatModel).build();
    }

    @Bean("OllamaChatClient")
    ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel)
    {
        return ChatClient.builder(ollamaChatModel).build();
    }

    @Bean
    SimpleLoggerAdvisor simpleLoggerAdvisor()
    {
        return new SimpleLoggerAdvisor();
    }

    @Bean
    SafeGuardAdvisor safeGuardAdvisor()
    {
        return new SafeGuardAdvisor(List.of("password","hack"));
    }
}
