package com.troojer.mstag.controller;

import com.troojer.mstag.model.TagDto;
import com.troojer.mstag.service.TagService;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@RestController
@RequestMapping("tags")
@Validated
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public Set<TagDto> getOrAddTagsByValue(@RequestBody @Size(min = 1, max = 10, message = "{tag.list.size}") Set<@Valid TagDto> tags) {
        return tagService.getOrAddTagsByValue(tags);
    }

    @GetMapping("search/{value}")
    public Set<TagDto> getTagsByValue(@PathVariable @Pattern(regexp = "[\\d\\w-]{3,20}", message = "{tag.value.pattern}") String value, Pageable pageable) {
        return tagService.getTagsByValue(value, pageable);
    }

    @GetMapping("id/{ids}")
    public Set<TagDto> getAllByIds(@PathVariable @Size(min = 1, max = 10, message = "{tag.list.size}") Set<@Pattern(regexp = "[0-9]*") String> ids) {
        return tagService.getTagsByIds(ids);
    }
}
