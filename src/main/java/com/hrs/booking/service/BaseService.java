package com.hrs.booking.service;

import com.hrs.booking.entity.CurrentUser;
import com.hrs.booking.entity.HRSUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseService {

    public static HRSUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        if ((authentication.getPrincipal() instanceof CurrentUser)) {
            return ((CurrentUser) authentication.getPrincipal()).getUser();
        }
        return null;
    }


}
