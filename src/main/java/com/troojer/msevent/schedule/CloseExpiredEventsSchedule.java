package com.troojer.msevent.schedule;

import com.troojer.msevent.service.EventService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CloseExpiredEventsSchedule {

    private final EventService eventService;

    public CloseExpiredEventsSchedule(EventService eventService) {
        this.eventService = eventService;
    }


    @Scheduled(fixedDelay = 5 * 60_000L)
    @SchedulerLock(name = "closeOldEvents")
    public void closeOldEvents() {
        eventService.setEndedStatusToAllExpired();
    }

}
