package com.troojer.msevent.service;

import com.troojer.msevent.model.*;

import java.util.Optional;
import java.util.Set;

public interface MangeEventService {

    String updateEventTitle(String key, EventDto eventDto);

    String updateEventDescription(String key, EventDto eventDto);

    BudgetDto updateEventBudget(String key, EventDto eventDto);

    String updateEventCover(String key, EventDto eventDto);

    Set<TagDto> updateEventTags(String key, EventDto eventDto);

    InvitingDto updateEventInviting(String key, EventDto eventDto);

    StartEndDatesDto updateEventDate(String key, EventDto eventDto);

    Optional<String> updateEventLocation(String key, EventDto eventDto);

    void deleteEvent(String key);

}
