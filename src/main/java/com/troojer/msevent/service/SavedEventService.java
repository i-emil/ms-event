package com.troojer.msevent.service;

import com.troojer.msevent.model.EventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SavedEventService {

    void saveEvent(String eventKey);

    Page<EventDto> getSavedEvents(Pageable pageable);

    void deleteSavedEvent(String savedEventId);

}
