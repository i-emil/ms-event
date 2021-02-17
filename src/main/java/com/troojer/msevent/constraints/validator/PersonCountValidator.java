package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.client.ProfileClient;
import com.troojer.msevent.client.UserPlanConstantsClient;
import com.troojer.msevent.constraints.PersonCountValidation;
import com.troojer.msevent.model.EventParticipantTypeDto;
import com.troojer.msevent.model.enm.Gender;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.util.AccessCheckerUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;


public class PersonCountValidator
        implements ConstraintValidator<PersonCountValidation, Map<ParticipantType, EventParticipantTypeDto>> {

    private final AccessCheckerUtil accessChecker;
    private final ProfileClient profileClient;

    private int maxPersonCount;

    public PersonCountValidator(AccessCheckerUtil accessChecker, ProfileClient profileClient) {
        this.accessChecker = accessChecker;
        this.profileClient = profileClient;
    }

    @Override
    public void initialize(PersonCountValidation constraintAnnotation) {
        maxPersonCount = UserPlanConstantsClient.getMaxPersonCount(accessChecker.getPlan());
    }

    @Override
    public boolean isValid(Map<ParticipantType, EventParticipantTypeDto> participantsType, ConstraintValidatorContext context) {

        if (participantsType == null || participantsType.isEmpty()) return false;
        int maleCount = (participantsType.get(ParticipantType.MALE) == null) ? 0 : participantsType.get(ParticipantType.MALE).getTotal();
        int femaleCount = (participantsType.get(ParticipantType.FEMALE) == null) ? 0 : participantsType.get(ParticipantType.FEMALE).getTotal();
        int allCount = (participantsType.get(ParticipantType.ALL) == null) ? 0 : participantsType.get(ParticipantType.ALL).getTotal();

        int totalPersonCount = maleCount + femaleCount + allCount;

        Gender gender = Gender.valueOf(profileClient.getProfileFilter().getGender());
        return ((gender == Gender.MALE && maleCount > 0) || (gender == Gender.FEMALE && femaleCount > 0) || allCount > 0)
                && totalPersonCount > 2 && totalPersonCount <= maxPersonCount;
    }

}
