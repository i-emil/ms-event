package com.troojer.msevent.service;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.FilterDto;
import java.util.*;


public interface OfferEventService {

    List<EventDto> getOfferEvents(FilterDto filter);

    void accept(String eventKey);

}
