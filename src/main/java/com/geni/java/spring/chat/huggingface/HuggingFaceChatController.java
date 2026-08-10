package com.geni.java.spring.chat.huggingface;


import com.geni.java.spring.chat.openai.OpenAIChatException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/huggingface/chat")
public class HuggingFaceChatController {

    private final static String SYSTEM_PROMPT="You are a senior software engineer specializing in writing clean, efficient, and idiomatic code." +
            "When given a coding task, respond only with well-structured, production-quality code." +
            "Follow language-specific best practices and conventions." +
            "Include brief comments only where necessary to clarify non-obvious logic." +
            "Do not include lengthy explanations unless explicitly asked.";

    private final ChatClient chatClient;

    public HuggingFaceChatController(@Qualifier("HuggingFaceChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/generate-code")
    public ChatClientResponse generateCode(@RequestBody String message)
    {
        try {
            return chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(message)
                    .call()
                    .chatClientResponse();
        } catch (Exception e) {
            throw new OpenAIChatException("Failed to get response from : " + e.getMessage(), e);
        }
    }
}
