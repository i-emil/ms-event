package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InvitingDto {

    private boolean active;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String key;

    @JsonInclude(NON_NULL)
    @Size(min = 6, max=10, message = "event.invitePassword.size::{min}::{max}", groups = {CreateValidation.class, UpdateValidation.class})
    private String password;
}
