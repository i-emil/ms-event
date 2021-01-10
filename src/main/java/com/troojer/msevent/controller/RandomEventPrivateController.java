package com.troojer.msevent.controller;

import com.troojer.msevent.service.RandomEventService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("private/random-events")
@CrossOrigin
public class RandomEventPrivateController {

    private final RandomEventService randomEventService;

    public RandomEventPrivateController(RandomEventService randomEventService) {
        this.randomEventService = randomEventService;
    }

    @DeleteMapping("inappropriate")
    public void removeInappropriateEvents() {
        randomEventService.deleteInappropriate();
    }
}
