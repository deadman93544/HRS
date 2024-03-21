package com.hrs.booking.controller;

import com.hrs.booking.entity.HRSUser;
import com.hrs.booking.service.UserService;
import com.hrs.booking.util.EncoderDecoder;
import com.hrs.booking.view.user.LoginRequest;
import com.hrs.booking.view.user.LoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

//API Controller class to handle Login
// This controller has no Endpoint prefix as "/api" as other controllers as this one is Public
@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EncoderDecoder encoderDecoder;

    @Value("${hrs.auth.secretkey}")
    private String secretkey;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretkey.getBytes());
    }

    //    API Endpoint to Login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest login, HttpServletRequest request) {

//        Checking User Credentials
        HRSUser user = userService.validateLogin(login, request.getRemoteAddr());

        if (user == null) {
            throw new RuntimeException("Invalid username/email");
        }

        LoginResponse loginResponse = new LoginResponse();

//        Creating User Authentication Token on successful verification.
        String authToken = Jwts.builder()
                .setSubject(login.getEmail() == null ? login.getUsername() : login.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(getAuthExpiration(user.getRoles().stream().findFirst().get().getAuthHours()))
                .signWith(key).compact();

        loginResponse.setToken(encoderDecoder.encryptUsingSecretKey(authToken));

        return loginResponse;
    }

    private Date getAuthExpiration(Long authHour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, authHour.intValue());
        return calendar.getTime();
    }
}
