package com.troojer.msevent.util;

import com.troojer.msevent.model.CurrentUser;
import org.springframework.stereotype.Component;

@Component
public class AccessCheckerUtil {

    private CurrentUser currentUser;

    public AccessCheckerUtil(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isUserId(String userId) {
        return userId.equals(currentUser.getId());
    }

    public String getUserId(){
        return currentUser.getId();
    }
}
