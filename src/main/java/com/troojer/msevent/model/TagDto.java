package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TagDto {

    private Long id;

}
