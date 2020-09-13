package com.troojer.mstag.controller;

import com.troojer.mstag.model.TagDto;
import com.troojer.mstag.service.TagService;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("tags")
@Validated
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDto> getTagsByValue(@RequestParam @Pattern(regexp = "[\\d\\w-]{3,20}", message = "{tag.value.pattern}") String value, Pageable pageable) {
        return tagService.getTagsByValue(value, pageable);
    }

    @PostMapping
    public List<TagDto> gerOrAddTagsByValue(@RequestBody @NotEmpty Set<@Valid TagDto> dto) {
        return tagService.getOrAddTagsByValue(dto);
    }
}
