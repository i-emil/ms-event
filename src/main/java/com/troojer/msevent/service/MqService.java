package com.troojer.msevent.service;

import com.troojer.msevent.model.NotificationDto;

public interface MqService {
    void sendNotificationToQueue(NotificationDto message);
}
