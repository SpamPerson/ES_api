package org.es.api.service;

import org.es.api.dto.Message;
import org.es.api.dto.request.ChatGptRequestDto;
import org.es.api.dto.request.QuestionRequestDto;
import org.es.api.dto.response.ChatGptResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ChatGptService {
    private static RestTemplate restTemplate = new RestTemplate();
    @Value("${openai.key}")
    private String API_KEY;
    @Value("${openai.model}")
    private String MODEL;
    public static final Integer MAX_TOKEN = 300;
    public static final Double TEMPERATURE = 0.9;
    public static final Double TOP_P = 0.9;

    public HttpEntity<ChatGptRequestDto> buildHttpEntity(ChatGptRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        headers.add("Authorization", "Bearer " + API_KEY);
        return new HttpEntity<>(requestDto, headers);
    }

    public ChatGptResponseDto getResponse(HttpEntity<ChatGptRequestDto> chatGptRequestDtoHttpEntity) {
        ResponseEntity<ChatGptResponseDto> responseEntity = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                chatGptRequestDtoHttpEntity,
                ChatGptResponseDto.class
        );
        return responseEntity.getBody();
    }

    public ChatGptResponseDto askQuestion(List<Message> messages) {
        List<Message> messageList = messages;
        if (messageList.size() > 10) {
            while (messageList.size() > 10) {
                messageList.remove(0);
            }
        }

        return this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequestDto(
                                MODEL,
                                messageList,
                                MAX_TOKEN,
                                TEMPERATURE,
                                TOP_P
                        )
                )
        );
    }

}
