package com.geni.java.spring.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.huggingface.HuggingfaceChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIProviderConfig {

    @Bean("OpenAIChatClient")
    ChatClient openAIChatClient(OpenAiChatModel openAiChatModel){

        return ChatClient.builder(openAiChatModel).build();
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
}
