package com.troojer.msevent.constraints.validator;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.LocationClient;
import com.troojer.msevent.constraints.IsLocationExist;
import com.troojer.msevent.model.LocationDto;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsLocationExistValidator
        implements ConstraintValidator<IsLocationExist, LocationDto> {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final LocationClient locationClient;

    public IsLocationExistValidator(LocationClient locationClient) {
        this.locationClient = locationClient;
    }

    @Override
    public boolean isValid(LocationDto location, ConstraintValidatorContext context) {
        if (location == null || location.getId() == null) return false;
        try {
            locationClient.getLocation(location.getId());
            return true;
        } catch (Exception e) {
            logger.warn("isLocationValid exc: ", e);
            return false;
        }
    }

}
