package com.troojer.msevent.service.impl;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.ImageClient;
import com.troojer.msevent.client.LocationClient;
import com.troojer.msevent.client.ProfileClient;
import com.troojer.msevent.client.TagClient;
import com.troojer.msevent.model.AgeDto;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.TagDto;
import com.troojer.msevent.model.exception.InvalidEntityException;
import com.troojer.msevent.model.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventDataChecker {
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final TagClient tagClient;
    private final LocationClient locationClient;
    private final ImageClient imageClient;
    private final ProfileClient profileClient;

    public void checkAllEventData(EventDto eventDto) {
        checkLocation(eventDto.getLocationId());
        checkCover(eventDto.getCover());
        checkAge(eventDto.getAge());
        checkTags(eventDto.getTags());
    }

    public void checkAge(AgeDto ageDto) {
        if (ageDto == null) return;
        Integer minAge = ageDto.getMin();
        Integer maxAge = ageDto.getMax();
        int current = profileClient.getProfileFilter().getCurrentAge();
        if (minAge != null && maxAge != null &&
                minAge <= maxAge &&
                minAge >= AgeDto.MIN_AGE && maxAge <= AgeDto.MAX_AGE &&
                current >= minAge && current <= maxAge) return;
        throw new InvalidEntityException("age range is: " + AgeDto.MIN_AGE + "-" + AgeDto.MAX_AGE + " and userAge have to be in selected range");
    }

    public void checkLocation(String locationId) {
        try {
            locationClient.getLocation(locationId);
        } catch (NotFoundException e) {
            logger.warn("location exception: ", e);
            throw new InvalidEntityException("location isn't exist");
        }
    }

    public void checkCover(String imageId) {
        try {
            imageClient.isImageExist(imageId);
        } catch (NotFoundException e) {
            logger.warn("cover exception: ", e);
            throw new InvalidEntityException("cover isn't exist");
        }
    }

    public void checkTags(Set<TagDto> tagDtoSet) {
        Set<TagDto> allTags = tagClient.getAllTags();
        if (!allTags.containsAll(tagDtoSet)) throw new InvalidEntityException("person count is incorrect");
    }

}
