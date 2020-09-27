package com.troojer.mstag.util;

import com.troojer.mstag.model.CurrentUser;
import org.springframework.stereotype.Component;

@Component
public class AccessCheckerUtil {

    private final CurrentUser currentUser;

    public AccessCheckerUtil(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isUserId(String userId) {
        return userId.equals(currentUser.getId());
    }

    public String getUserId() {
        return currentUser.getId();
    }
}
