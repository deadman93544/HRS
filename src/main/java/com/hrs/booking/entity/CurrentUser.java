package com.hrs.booking.entity;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

//By extending the User class from Spring Security, CurrentUser inherits fields and methods related to security,
// such as username, password, and authorities (roles).
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