package com.hrs.booking.view.user;

import com.hrs.booking.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoleRequest {
    private String id;
    private String name;
    private List<String> privileges = new ArrayList<>();

    public Role toEntity(Role role) {
        if (role == null) {
            role = new Role();
        }
        role.setName(this.name);
        return role;
    }

    public Role toEntity() {
        return toEntity(new Role());
    }
}