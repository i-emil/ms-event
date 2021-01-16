package com.troojer.msevent.controller;

import com.troojer.msevent.client.UserPlanConstantsClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("events-rules")
@CrossOrigin
public class EventsConstantsController {

    private final UserPlanConstantsClient userPlanConstantsClient;

    public EventsConstantsController(UserPlanConstantsClient userPlanConstantsClient) {
        this.userPlanConstantsClient = userPlanConstantsClient;
    }

    @GetMapping()
    public Map<String, String> getConstantsByPlan() {
        return userPlanConstantsClient.getConstantsMap();
    }
}
