package com.troojer.msevent.controller;

import com.troojer.msevent.model.*;
import com.troojer.msevent.model.label.manageevent.*;
import com.troojer.msevent.service.impl.ManageEventServiceImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("events/manage")
public class ManageEventController {

    private final ManageEventServiceImpl manageEventService;

    public ManageEventController(ManageEventServiceImpl manageEventService) {
        this.manageEventService = manageEventService;
    }

    @PutMapping(value = "title/{key}")
    public Optional<String> updateEventTitle(@PathVariable String key, @Validated(TitleUpdateValidation.class) @RequestBody EventDto eventDto) {
        return Optional.of(manageEventService.updateEventTitle(key, eventDto));
    }

    @PutMapping(value = "description/{key}")
    public Optional<String> updateEventDescription(@PathVariable String key, @Validated(DescriptionUpdateValidation.class) @RequestBody EventDto eventDto) {
        return Optional.of(manageEventService.updateEventDescription(key, eventDto));
    }

    @PutMapping("budget/{key}")
    public BudgetDto updateEventBudget(@PathVariable String key, @Validated(BudgetUpdateValidation.class) @RequestBody EventDto eventDto) {
        return manageEventService.updateEventBudget(key, eventDto);
    }

    @PutMapping("cover/{key}")
    public Optional<String> updateEventCover(@PathVariable String key, @Validated(CoverUpdateValidation.class) @RequestBody EventDto eventDto) {
        return Optional.of(manageEventService.updateEventCover(key, eventDto));
    }

    @PutMapping("tags/{key}")
    public Set<TagDto> updateEventTags(@PathVariable String key, @Validated(TagsUpdateValidation.class) @RequestBody EventDto eventDto) {
        return manageEventService.updateEventTags(key, eventDto);
    }

    @PutMapping("inviting/{key}")
    public InvitingDto updateEventInviting(@PathVariable String key, @Validated(InvitingUpdateValidation.class) @RequestBody EventDto eventDto) {
        return manageEventService.updateEventInviting(key, eventDto);
    }

    @PutMapping("date/{key}")
    public StartEndDatesDto updateEventDate(@PathVariable String key, @Validated(DatesUpdateValidation.class) @RequestBody EventDto eventDto) {
        return manageEventService.updateEventDate(key, eventDto);
    }

    @PutMapping("location/{key}")
    public Optional<String> updateEventLocation(@PathVariable String key, @Validated(LocationUpdateValidation.class) @RequestBody EventDto eventDto) {
        return manageEventService.updateEventLocation(key, eventDto);
    }

    @DeleteMapping("{key}")
    public void deleteEvent(@PathVariable String key) {
        manageEventService.deleteEvent(key);
    }
}
