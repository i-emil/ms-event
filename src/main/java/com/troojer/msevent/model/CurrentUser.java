package com.troojer.msevent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Component
@RequestScope
public class CurrentUser {
    private String id;
    private String username;
    private String[] roles;
}
