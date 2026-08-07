package com.geni.java.spring.chat.openai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.chat.client.ChatClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class OpenAIChatControllerNegativeTest {

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;

    @Mock
    private ChatClient.CallResponseSpec callResponseSpec;

    private OpenAIChatController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new OpenAIChatController(chatClient);
    }

    @Test
    void summarize_whenChatClientThrowsException_propagatesException() {
        String userMessage = "Some meeting notes";

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(userMessage)).thenReturn(requestSpec);
        when(requestSpec.call()).thenThrow(new RuntimeException("OpenAI API error: rate limit exceeded"));

        OpenAIChatException exception = assertThrows(OpenAIChatException.class,
                () -> controller.summarize(userMessage));

        assertTrue(exception.getMessage().contains("OpenAI API error: rate limit exceeded"));
    }

    @Test
    void summarize_whenMessageIsNull_currentlyPassesNullThrough() {
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user((String) null)).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(callResponseSpec);
        when(callResponseSpec.content()).thenReturn("");

        String result = controller.summarize(null);

        assertEquals("", result);
        // This test documents current (unsafe) behavior — no null validation exists yet
    }

    @Test
    void summarize_whenMessageIsEmpty_currentlyPassesEmptyThrough() {
        String emptyMessage = "";

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(emptyMessage)).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(callResponseSpec);
        when(callResponseSpec.content()).thenReturn("");

        String result = controller.summarize(emptyMessage);

        assertEquals("", result);
        // This test documents current (unsafe) behavior — no empty-string validation exists yet
    }
}