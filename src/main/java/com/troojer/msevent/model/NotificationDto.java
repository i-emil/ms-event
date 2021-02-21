package com.troojer.msevent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {

    private String title;

    private String description;

    private Map<String, String> params;

    private InnerNotificationType type;

    private String userId;

    private String sendingDate;

}
