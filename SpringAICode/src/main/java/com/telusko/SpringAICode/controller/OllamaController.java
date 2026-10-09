package com.telusko.SpringAICode.controller;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OllamaController {

    private OllamaChatModel chatModel;

    public OllamaController(OllamaChatModel chatModel){
        this.chatModel= chatModel;
    }

    @GetMapping("/api/{message}")
    public String getAnswer(@PathVariable String message){
        String response = chatModel.call(message);
        return response;
    }
}
