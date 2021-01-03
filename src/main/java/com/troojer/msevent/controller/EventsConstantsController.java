package com.troojer.msevent.controller;

import com.troojer.msevent.service.impl.UserPlanConstantsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("events-rules")
@CrossOrigin
public class EventsConstantsController {

    private final UserPlanConstantsService userPlanConstantsService;

    public EventsConstantsController(UserPlanConstantsService userPlanConstantsService) {
        this.userPlanConstantsService = userPlanConstantsService;
    }

    @GetMapping()
    public Map<String, String> getConstantsByPlan() {
        return userPlanConstantsService.getConstantsMap();
    }
}
