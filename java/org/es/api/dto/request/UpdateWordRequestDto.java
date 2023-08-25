package org.es.api.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateWordRequestDto {
    String wordCode;
    String columnName;
    String value;
}
