package org.es.api.controller;

import lombok.RequiredArgsConstructor;
import org.es.api.dto.Message;
import org.es.api.dto.request.QuestionRequestDto;
import org.es.api.dto.response.ChatGptResponseDto;
import org.es.api.service.ChatGptService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ai")
public class AiController {
    private final ChatGptService chatGptService;

    @PostMapping("/question")
    public Message sendQuestion(@RequestBody List<Message> messages) {

        ChatGptResponseDto chatGptResponseDto = chatGptService.askQuestion(messages);
        return chatGptResponseDto.getChoices().get(0).getMessage();
    }
}
