package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.manageevent.InvitingUpdateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InvitingDto {

    @Builder.Default
    private boolean active = true;

    @JsonInclude(NON_NULL)
    @Size(min = 6, max = 10, message = "event.invitePassword.size::{min}::{max}", groups = {CreateValidation.class, InvitingUpdateValidation.class})
    private String password;

    //read only
    @JsonProperty(access = READ_ONLY)
    private String key;
}
