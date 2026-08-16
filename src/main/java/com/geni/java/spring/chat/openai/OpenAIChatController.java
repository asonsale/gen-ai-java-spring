package com.geni.java.spring.chat.openai;

import com.geni.java.spring.chat.openai.dto.response.SummarizationResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/openai/chat")
public class OpenAIChatController {
    private final ChatClient chatClient;
    private final static String SYSTEM_PROMPT="You are a senior software engineer specializing in writing clean, efficient, and idiomatic code." +
            "When given a coding task, respond only with well-structured, production-quality code." +
            "Follow language-specific best practices and conventions." +
            "Include brief comments only where necessary to clarify non-obvious logic." +
            "Do not include lengthy explanations unless explicitly asked.";

    public OpenAIChatController(@Qualifier("OpenAIChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/summarize")
    public String summarize(@RequestBody String message)
    {
        try {
            return chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(message)
                    .call()
                    .content();
        } catch (Exception e) {
            throw new OpenAIChatException("Failed to get response from OpenAI: " + e.getMessage(), e);
        }

    }

    @PostMapping(value="/summarize-meeting-notes-structured", produces = MediaType.APPLICATION_JSON_VALUE)
    public SummarizationResponse summarizeMeetingNotesStructuredOutput(@RequestBody String meetingNotes) {
        try {
            return chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(u -> u.text("Can you summarize the following meeting notes: {meetingNotes}" +
                                    " Use the format as described in the following example while doing the summarization:" +
                                    " Input: In today’s sales strategy meeting, we reviewed Q3 targets and performance gaps. The team agreed to focus on enterprise clients and strengthen partnerships." +
                                    " A proposal was made to expand into two new regions. Marketing suggested aligning campaigns with sales objectives to improve lead conversion and shorten sales cycles." +
                                    " Output:" +
                                    " Action Items:" +
                                    "* Focus on enterprise clients and partnerships." +
                                    "* Explore expansion into two new regions." +
                                    "* Align marketing campaigns with sales objectives." +
                                    " Decisions:" +
                                    "* Enterprise clients prioritized for Q3." +
                                    "* Marketing and sales to work jointly on lead password conversion.")
                            .param("meetingNotes", meetingNotes))
                    .call()
                    .entity(SummarizationResponse.class);
        } catch (Exception e) {
            return new SummarizationResponse(null, null, e.getMessage());
        }
    }

    @PostMapping("/summarization-meeting-notes-structures-list")
    public List<SummarizationResponse> summerizingMeetingNotesOutputList(@RequestBody String meetingNotes)
    {
        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(u-> u.text("Can you summarize the following meeting notes: {meetingNotes}" +
                        "Give me 3 different summarization in the same format so that I can choose from. "+
                        " Use the format as described in the following example while doing the summarization:" +
                        " Input: In today’s sales strategy meeting, we reviewed Q3 targets and performance gaps. The team agreed to focus on enterprise clients and strengthen partnerships." +
                        " A proposal was made to expand into two new regions. Marketing suggested aligning campaigns with sales objectives to improve lead conversion and shorten sales cycles." +
                        " Output:" +
                        " Action Items:" +
                        "* Focus on enterprise clients and partnerships." +
                        "* Explore expansion into two new regions." +
                        "* Align marketing campaigns with sales objectives." +
                        " Decisions:" +
                        "* Enterprise clients prioritized for Q3." +
                        "* Marketing and sales to work jointly on lead conversion.").param("meetingNotes",meetingNotes))
                .call()
                .entity(new ParameterizedTypeReference<List<SummarizationResponse>>() {});
    }

    @PostMapping(value = "/summarize-meeting-notes-debug")
    public String summarizeMeetingNotesDebug(@RequestBody String text) {
        return chatClient.prompt()
                .user(text)
                .call()
                .content();
    }

}
