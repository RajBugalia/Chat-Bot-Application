package com.chatbot.financechatbot.controller;

import com.chatbot.financechatbot.model.ChatRequest;
import com.chatbot.financechatbot.model.ChatResponse;
import com.chatbot.financechatbot.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*") // Allows Android emulator to connect without CORS errors
class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    //POST http://10.174.158.64:1001/api/chat/completion
    @PostMapping("/completion")
    public ChatResponse getCompletion(@RequestBody ChatRequest request) {
        return chatService.getChatCompletion(request.getPrompt());
    }

    //GET http://localhost:1001/api/chat/test
    @GetMapping("/test")
    public ChatResponse testCompletion() {
        return chatService.getChatCompletion("Test prompt");
    }
}
