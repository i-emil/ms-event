package com.troojer.mstag.service;

import com.troojer.mstag.model.TagDto;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TagService {

    Set<TagDto> getTagsByValue(String value, Pageable pageable);

    Set<TagDto> getOrAddTagsByValue(Collection<TagDto> values);

    Set<TagDto> getTagsByIds(Set<String> ids);
}
