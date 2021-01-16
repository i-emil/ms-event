package com.troojer.msevent.dao;

import com.troojer.msevent.model.enm.EventStatus;

import java.time.ZonedDateTime;

public interface SimpleEvent {

    Long getId();

    String getKey();

    String getTitle();

    String getDescription();

    String getCover();

    ZonedDateTime getStartDate();

    ZonedDateTime getEndDate();

    EventStatus getStatus();

}
