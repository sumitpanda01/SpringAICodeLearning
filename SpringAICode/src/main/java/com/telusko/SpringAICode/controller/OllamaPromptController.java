package com.telusko.SpringAICode.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OllamaPromptController {

    @Autowired
    private EmbeddingModel embeddingModel;

    private ChatClient chatClient;

    public OllamaPromptController(OllamaChatModel chatModel){
        this.chatClient= ChatClient.create(chatModel);
    }

    @GetMapping("/api/{message}")
    public ResponseEntity<String> getAnswer(@PathVariable String message){

        ChatResponse chatResponse = chatClient
                .prompt(message)
                .call().chatResponse();
        System.out.println(chatResponse.getMetadata().getModel());

        String response  = chatResponse.getResult().getOutput().getText();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/recommend")
    public String recommend(
            @RequestParam String type,
            @RequestParam String year,
            @RequestParam String lang
    ){

        String tempt= """
                I want to watch a {type} movie tonight with good rating,
                looking for movies around this year {year}.
                The language i'm looking for is {lang}.
                Suggest one specific movie and tell me the cast and length of movie.
                
                response format should be:-
                1. Movie name
                2. basic plot
                3. cast
                4. length
                5. IMDB rating.
                """;

        PromptTemplate promptTemplate = new PromptTemplate(tempt);
        Prompt prompt = promptTemplate.create(
                Map.of("type",type,"year",year,"lang",lang)
        );
        String response = chatClient
                .prompt(prompt)
                .call()
                .content();

        return response;
    }

    @PostMapping("/api/embedding")
    public float[] embedding(@RequestParam String text) {

        System.out.println(">>> EMBEDDING ENDPOINT CALLED <<<");
        System.out.println("Input text: " + text);

        float[] vector = embeddingModel.embed(text);

        System.out.println("Vector length: " + vector.length);

        return vector;
    }
}
