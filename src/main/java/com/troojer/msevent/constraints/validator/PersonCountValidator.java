package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.constraints.PersonCountValidation;
import com.troojer.msevent.model.EventParticipantTypeDto;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.client.UserPlanConstantsClient;
import com.troojer.msevent.util.AccessCheckerUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

import static com.troojer.msevent.model.enm.ParticipantType.*;

public class PersonCountValidator
        implements ConstraintValidator<PersonCountValidation, Map<ParticipantType, EventParticipantTypeDto>> {

    private final AccessCheckerUtil accessChecker;

    private int maxPersonCount;

    public PersonCountValidator(AccessCheckerUtil accessChecker) {
        this.accessChecker = accessChecker;
    }

    @Override
    public void initialize(PersonCountValidation constraintAnnotation) {
        maxPersonCount = UserPlanConstantsClient.getMaxPersonCount(accessChecker.getPlan());
    }

    @Override
    public boolean isValid(Map<ParticipantType, EventParticipantTypeDto> participantsType, ConstraintValidatorContext context) {

        if (participantsType == null || participantsType.isEmpty()) return false;
        int maleCount = (participantsType.get(MALE) == null) ? 0 : participantsType.get(MALE).getTotal();
        int femaleCount = (participantsType.get(FEMALE) == null) ? 0 : participantsType.get(FEMALE).getTotal();
        int allCount = (participantsType.get(ALL) == null) ? 0 : participantsType.get(ALL).getTotal();

        int totalPersonCount = maleCount + femaleCount + allCount;

        return totalPersonCount > 2 && totalPersonCount <= maxPersonCount;
    }

}
