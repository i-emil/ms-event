package com.troojer.msinterest.controller;

import com.troojer.msinterest.model.InterestDto;
import com.troojer.msinterest.service.InterestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("interests")
public class InterestController {
    private final InterestService interestService;

    public InterestController(InterestService interestService) {
        this.interestService = interestService;
    }

    @GetMapping("{interestId}")
    public InterestDto getInterest(@PathVariable Long interestId) {
        return interestService.getInterest(interestId);
    }

    @GetMapping("name/{name}")
    public Set<InterestDto> getInterestsByName(@PathVariable String name) {
        return interestService.getInterestByName(name);
    }
}
