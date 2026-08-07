package com.geni.java.spring.chat.openai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/openai/chat")
public class OpenAIChatController {
    private final ChatClient chatClient;

    public OpenAIChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/summarize")
    public String summarize(@RequestBody String message)
    {
        try {
            return chatClient.prompt()
                    .user(message)
                    .call()
                    .content();
        } catch (Exception e) {
            throw new OpenAIChatException("Failed to get response from OpenAI: " + e.getMessage(), e);
        }

    }

}
