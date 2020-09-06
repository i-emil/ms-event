package com.troojer.msinterest.service.impl;

import com.troojer.msinterest.dao.repository.InterestRepository;
import com.troojer.msinterest.mapper.InterestMapper;
import com.troojer.msinterest.model.InterestDto;
import com.troojer.msinterest.model.exception.NotFoundException;
import com.troojer.msinterest.service.InterestService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;

    public InterestServiceImpl(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    @Override
    //todo exception message
    public InterestDto getInterest(Long interestId) {
        return InterestMapper.entityToDto(interestRepository
                .findById(interestId).orElseThrow(() -> new NotFoundException("interest.notFound")));
    }

    @Override
    public Set<InterestDto> getInterestByName(String name) {
        return InterestMapper.entitiesToDtos(interestRepository.getAllByName(name));
    }
}
