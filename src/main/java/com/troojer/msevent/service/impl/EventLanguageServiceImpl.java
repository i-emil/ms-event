package com.troojer.msevent.service.impl;

import com.troojer.msevent.client.LanguageClient;
import com.troojer.msevent.dao.EventLanguageEntity;
import com.troojer.msevent.dao.repository.EventLanguageRepository;
import com.troojer.msevent.mapper.LanguageMapper;
import com.troojer.msevent.model.LanguageDto;
import com.troojer.msevent.service.EventLanguageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class EventLanguageServiceImpl implements EventLanguageService {
    private final EventLanguageRepository eventLanguageRepository;
    private final LanguageClient languageClient;

    public EventLanguageServiceImpl(EventLanguageRepository eventLanguageRepository, LanguageClient languageClient) {
        this.eventLanguageRepository = eventLanguageRepository;
        this.languageClient = languageClient;
    }

    @Override
    public Set<LanguageDto> getLanguageListByEventId(Long eventId) {
        return LanguageMapper
                .entitySetToDtoSet(getEntitySetByProfileId(eventId), languageClient.getLanguagesMap());
    }

    @Override
    @Transactional
    public void addLanguageListByEventId(Long eventId, Set<LanguageDto> languageDtoSet) {
        eventLanguageRepository.deleteAll(getEntitySetByProfileId(eventId));
        eventLanguageRepository.saveAll(LanguageMapper.dtoSetToEntitySet(eventId, languageDtoSet, languageClient.getLanguagesMap()));
    }

    private Set<EventLanguageEntity> getEntitySetByProfileId(Long profileId) {
        return eventLanguageRepository.getByEventId(profileId);
    }
}
