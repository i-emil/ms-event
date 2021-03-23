package com.troojer.msevent.model;

import com.troojer.msevent.model.enm.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageDto {

    private List<String> usersId;

    private MessageType type;

    private String title;

    private String message;
}
