package org.es.api.dto.request;

import lombok.Getter;
import org.es.api.dto.Message;

import java.io.Serializable;
import java.util.List;

@Getter
public class QuestionRequestDto {
    List<Message> messages;
}
