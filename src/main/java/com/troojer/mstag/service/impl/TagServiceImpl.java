package com.troojer.mstag.service.impl;

import ch.qos.logback.classic.Logger;
import com.troojer.mstag.dao.TagEntity;
import com.troojer.mstag.dao.repository.TagRepository;
import com.troojer.mstag.mapper.TagMapper;
import com.troojer.mstag.model.TagDto;
import com.troojer.mstag.service.TagService;
import com.troojer.mstag.util.AccessCheckerUtil;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final ch.qos.logback.classic.Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());
    private final TagRepository tagRepository;
    private final AccessCheckerUtil accessChecker;

    public TagServiceImpl(TagRepository tagRepository, AccessCheckerUtil accessChecker) {
        this.tagRepository = tagRepository;
        this.accessChecker = accessChecker;
    }

    @Override
    public List<TagDto> getTagsByValue(String value, Pageable pageable) {
        logger.info("getTagsByValue() called");
        return TagMapper.entitiesToDtos(tagRepository.getByValueStartsWith(value, pageable).getContent());
    }

    @Override
    @Transactional
    public List<TagDto> getOrAddTagsByValue(Collection<TagDto> dtos) {
        logger.info("getOrAddTagsByValue() called");
        dtos.forEach((v) -> {
            if (tagRepository.getByValueEquals(v.getValue()).isEmpty()) {
                tagRepository.save(TagEntity.builder().userId(accessChecker.getUserId()).value(v.getValue()).build());
                logger.info("getOrAddTagsByValue(); '{}' added", v.getValue());
            }
        });
        return TagMapper.entitiesToDtos(tagRepository
                .getAllByValueIn(TagMapper.dtosToStringSet(dtos)));
    }
}
