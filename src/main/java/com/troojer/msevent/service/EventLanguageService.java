package com.troojer.msevent.service;

import com.troojer.msevent.model.LanguageDto;

import java.util.Set;

public interface EventLanguageService {
    Set<LanguageDto> getLanguageListByEventId(Long profileId);

    void addLanguageListByEventId(Long eventId, Set<LanguageDto> languageDtoSet);
}
