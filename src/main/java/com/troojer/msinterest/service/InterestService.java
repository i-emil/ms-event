package com.troojer.msinterest.service;

import com.troojer.msinterest.model.InterestDto;

import java.util.Set;

public interface InterestService {
    InterestDto getInterest(Long interestId);

    Set<InterestDto> getInterestByName(String name);
}
