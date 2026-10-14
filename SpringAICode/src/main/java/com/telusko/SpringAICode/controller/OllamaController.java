package com.telusko.SpringAICode.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OllamaController {

    private ChatClient chatClient;


    //use this model when you are working on multiple model of AI
    //and if you wants to work with only single model then builder is a best option for this
//    public OllamaController(OllamaChatModel chatModel){
//        this.chatClient= ChatClient.create(chatModel);
//    }

//    public OllamaController(ChatClient.Builder builder){
//        this.chatClient = builder.build();
//    }





    //adding the memory to the AI so that when i want to do more with the same chat,
    //so for this it remembers my previous chat and reply accordingly

    ChatMemory chatMemory= MessageWindowChatMemory.builder().build();
    public OllamaController(ChatClient.Builder builder){
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor
                        .builder(chatMemory)
                        .build())
                .build();
    }


    //for content method
//    @GetMapping("/api/{message}")
//    public ResponseEntity<String> getAnswer(@PathVariable String message){
//        String response = chatClient
//                .prompt(message)
//                .call()
//                .content();
//        return ResponseEntity.ok(response);
//    }

    //for chatResponse and metadata
//    @GetMapping("/api/{message}")
//    public ResponseEntity<String> getAnswer(@PathVariable String message){
//
//        ChatResponse chatResponse = chatClient
//                .prompt(message)
//                .call().chatResponse();
//        System.out.println(chatResponse.getMetadata().getModel());
//
//        String response  = chatResponse.getResult().getOutput().getText();
//        return ResponseEntity.ok(response);
//    }

    //for ollama to keep the chat history and reply according to the previous chat


    //@GetMapping("/api/{message}")
    public ResponseEntity<String> getAnswer(
            @PathVariable String message,
            @RequestParam String conversationId) {

        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID,
                        conversationId
                ))
                .call()
                .chatResponse();

        System.out.println(chatResponse.getMetadata().getModel());

        String response = chatResponse.getResult()
                .getOutput()
                .getText();

        return ResponseEntity.ok(response);
    }

}

/**
 * ChatResponse chatResponse = chatClient
 *                 .prompt()
 *                 .user(message)
 *                 .advisors(advisor -> advisor.param(
 *                         ChatMemory.CONVERSATION_ID,
 *                         conversationId
 *                 ))
 *                 .call()
 *                 .chatResponse();
 */