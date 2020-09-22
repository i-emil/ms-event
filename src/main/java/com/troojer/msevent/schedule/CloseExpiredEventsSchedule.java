package com.troojer.msevent.schedule;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.service.EventService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CloseExpiredEventsSchedule {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final EventService eventService;

    public CloseExpiredEventsSchedule(EventService eventService) {
        this.eventService = eventService;
    }

    @Scheduled(fixedDelay = 5 * 60_000L)
    @SchedulerLock(name = "closeOldEvents")
    public void closeOldEvents() {
        eventService.setEndedStatusToAllExpired();
        logger.info("closeOldEvents() called");
    }

}
