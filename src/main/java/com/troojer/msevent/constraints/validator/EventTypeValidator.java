package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.constraints.EventTypeValidation;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.EventParticipantTypeDto;
import com.troojer.msevent.model.enm.EventType;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.util.AccessCheckerUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

import static com.troojer.msevent.model.enm.ParticipantType.*;

public class EventTypeValidator
        implements ConstraintValidator<EventTypeValidation, EventDto> {

    private final AccessCheckerUtil accessChecker;

    public EventTypeValidator(AccessCheckerUtil accessChecker) {
        this.accessChecker = accessChecker;
    }

    @Override
    public void initialize(EventTypeValidation constraintAnnotation) {

    }

    @Override
    public boolean isValid(EventDto event, ConstraintValidatorContext context) {
        if (!isTypeValid(event.getType())) return false;
        EventType type = EventType.valueOf(event.getType());
        Map<ParticipantType, EventParticipantTypeDto> participantsType = event.getParticipantsType();
        if (participantsType == null || participantsType.isEmpty()) return false;
        int maleCount = (participantsType.get(MALE) == null) ? 0 : participantsType.get(MALE).getTotal();
        int femaleCount = (participantsType.get(FEMALE) == null) ? 0 : participantsType.get(FEMALE).getTotal();
        int allCount = (participantsType.get(ALL) == null) ? 0 : participantsType.get(ALL).getTotal();
        int coupleCount = (participantsType.get(COUPLE) == null) ? 0 : participantsType.get(COUPLE).getTotal();

        int totalPersonCount = maleCount + femaleCount + allCount + coupleCount * 2;

        return switch (type) {
            case STANDARD -> coupleCount == 0 && totalPersonCount > 1 && totalPersonCount <= 10;
            case COUPLE -> coupleCount > 1 && coupleCount <= 5 && femaleCount == 0 && maleCount == 0 && allCount == 0;
            case TOUR -> totalPersonCount > 1 && totalPersonCount <= 30;
            case PARTY -> totalPersonCount > 1 && totalPersonCount <= 100;
        };
    }

    public boolean isTypeValid(String type) {
        return type != null && type.matches("STANDARD|COUPLE|TOUR|PARTY");
    }

}
