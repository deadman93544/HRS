package com.hrs.booking.view.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest{
    private String username;
    private String email;
    private String password;
}