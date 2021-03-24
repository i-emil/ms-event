package com.troojer.msevent.model;

import com.troojer.msevent.model.enm.MessageType;
import com.troojer.msevent.model.enm.UserFoundEventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {

    private String title;

    private String description;

    private Map<String, String> params;

    private MessageType type;

    private String userId;

    private String sendingDate;

    private List<String> recipientsId;

}
