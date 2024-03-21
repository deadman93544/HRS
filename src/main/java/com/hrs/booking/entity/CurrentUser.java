package com.hrs.booking.entity;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class CurrentUser extends User {

    private HRSUser user;

    public CurrentUser(HRSUser user, String[] roles) {
        super(user.getEmail(), user.getPasswordHash(), AuthorityUtils.createAuthorityList(roles));
        this.user = user;
    }

    public HRSUser getUser() {
        return user;
    }

    public void setUser(HRSUser user) {
        this.user = user;
    }
}