package com.geni.java.spring.chat.dockearmodelrunner;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stringtemplate.v4.ST;

@RestController
@RequestMapping("/api/docker-model-runner/chat")
public class DockerModelController {

    private static final String SYSTEM_PROMPT= "Your are helpful assistant that generates professional Linked in posts aobut technical subjects."
            +"Enuser that posts are engaging , information & tailored to professional audience"
            +"Used friendly and approachable tone while maintaining professionalism.";
    private final ChatClient dockerChatClient;

    public DockerModelController(@Qualifier("OpenAIChatClient") ChatClient dockerChatClient) {
        this.dockerChatClient = dockerChatClient;
    }

    @PostMapping("/linkedin-post-generator")
    public String linkedinPostGenerator(@RequestBody String message)
    {
        return dockerChatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(message)
                .call()
                .content();


    }
}
