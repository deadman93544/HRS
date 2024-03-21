package com.hrs.booking.view.user;

import com.hrs.booking.entity.HRSUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest{
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private List<String> roles = new ArrayList<>();

    public HRSUser toEntity(HRSUser user) {
        if (user == null) {
            user = new HRSUser();
        }
        user.setEmail(this.email);
        user.setFirstname(this.firstName);
        user.setLastname(this.lastName);
        user.setUsername(this.userName);
        user.setPhone(this.getPhone());

        if (this.password != null && !this.password.isEmpty()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPasswordHash(encoder.encode(this.password));
        }
        return user;
    }

    public HRSUser toEntity() {
        return toEntity(new HRSUser());
    }

}