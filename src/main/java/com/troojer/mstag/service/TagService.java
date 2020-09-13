package com.troojer.mstag.service;

import com.troojer.mstag.model.TagDto;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface TagService {

    List<TagDto> getTagsByValue(String value, Pageable pageable);

    List<TagDto> getOrAddTagsByValue(Collection<TagDto> values);
}
