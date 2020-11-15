package com.troojer.msevent.schedule;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.service.EventService;
import com.troojer.msevent.service.FindEventService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExpiredEventsSchedule {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final EventService eventService;
    private final FindEventService findEventService;

    public ExpiredEventsSchedule(EventService eventService, FindEventService findEventService) {
        this.eventService = eventService;
        this.findEventService = findEventService;
    }

    @Scheduled(fixedDelay = 5 * 60_000L)
    @SchedulerLock(name = "closeOldEvents")
    public void closeOldEvents() {
        eventService.setEndedStatusToAllExpired();
        logger.info("closeOldEvents() called");
    }

    @Scheduled(fixedDelay = 30 * 24 * 60 * 60_000L)
    @SchedulerLock(name = "deleteOldUserFoundEvents")
    public void deleteOldUserFoundEvents() {
        findEventService.deleteOld(LocalDateTime.now().minusMonths(6));
        logger.info("deleteOldUserFoundEvents() called");
    }
}
