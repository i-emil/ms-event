package com.troojer.msevent.dao.repository.impl;

import com.troojer.msevent.dao.EventParticipantTypeEntity;
import com.troojer.msevent.dao.ParticipantEntity;
import com.troojer.msevent.dao.repository.ParticipantExtendedRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

@Repository
public class ParticipantRepositoryImpl implements ParticipantExtendedRepository {

    private final EntityManager entityManager;

    public ParticipantRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void addParticipant(Long participantTypeId, ParticipantEntity participant) {
        EventParticipantTypeEntity entity = entityManager.find(EventParticipantTypeEntity.class, participantTypeId, LockModeType.PESSIMISTIC_READ);
        if (!entity.isFree())
            throw new IllegalStateException("addParticipant(); can't added participant " + participant.getUserId() + " to participantType " + participantTypeId);
        entity.increaseAccepted();
        entityManager.persist(participant);
        entityManager.persist(entity);
    }
}
