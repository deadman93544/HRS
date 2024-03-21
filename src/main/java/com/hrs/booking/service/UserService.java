package com.hrs.booking.service;

import com.hrs.booking.auth.AuthCacheProvider;
import com.hrs.booking.entity.CurrentUser;
import com.hrs.booking.entity.HRSUser;
import com.hrs.booking.entity.Privilege;
import com.hrs.booking.entity.Role;
import com.hrs.booking.jpa.PrivilegeRepository;
import com.hrs.booking.jpa.RoleRepository;
import com.hrs.booking.jpa.UserRepository;
import com.hrs.booking.util.EncoderDecoder;
import com.hrs.booking.view.user.LoginRequest;
import com.hrs.booking.view.user.RoleRequest;
import com.hrs.booking.view.user.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UserService extends BaseService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private AuthCacheProvider authCache;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public void createTechAdmin() {
        HRSUser techAdmin = userRepository.findByUsername("super_admin");
        if(techAdmin == null){
            techAdmin = new HRSUser();
            techAdmin.setUsername("super_admin");
            techAdmin.setPasswordHash(encoder.encode("Admin@123"));
            techAdmin.setFirstname("Tech");
            techAdmin.setLastname("Admin");
            techAdmin.setEmail("admin@hrs.com");
            techAdmin.addRole(roleRepository.getOneByName("TECHADMIN"));
            userRepository.save(techAdmin);
        }
    }

    public HRSUser validateLogin(LoginRequest request, String ip) {

        HRSUser user;
        user = userRepository.findByEmailOrUsername(request.getUsername(), request.getUsername());

        if (user != null) {

            if(encoder.matches(request.getPassword(), user.getPasswordHash())){
                user.setLastLogin(new Date());
            }
            else{
                logger.info("Cannot Authenticate! Wrong Password");
                return null;
            }
            userRepository.saveAndFlush(user);
            return user;
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String subject) throws UsernameNotFoundException {
        HRSUser user = userRepository.findByEmailOrUsername(subject, subject);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s was not found", subject));
        }
        return new CurrentUser(user, new String[]{"ROLE_USER"});
    }

    public HRSUser createUser(UserRequest request) {

        validateUser(request);
        validatePassword(request.getPassword());
        HRSUser user = userRepository.findByEmailOrUsername(request.getEmail(), request.getUserName());
        if (user != null) {
            throw new RuntimeException(String.format("Already exist [email=%s]", request.getEmail()));
        }

        user = request.toEntity();

        for (String role : request.getRoles()) {
            user.addRole(roleRepository.getOneByName(role));
        }

        user.setPasswordHash(encoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public HRSUser updateUser(UserRequest request) {

        validateUser(request);
        HRSUser user = userRepository.findByEmail(request.getEmail());

        request.toEntity(user);

        user.getRoles().clear();
        for (String role : request.getRoles()) {
            Role tempRole = roleRepository.getOneByName(role);
            user.addRole(tempRole);
        }
        return userRepository.save(user);
    }

    private void validateUser(UserRequest request) {
        if (request.getFirstName().isEmpty()) {
            throw new RuntimeException("First Name is required.");
        }
        if (request.getLastName().isEmpty()) {
            throw new RuntimeException("Lastname is required.");
        }
        if (request.getEmail().isEmpty()) {
            throw new RuntimeException("User email is required.");
        }
        if (request.getUserName().isEmpty()) {
            throw new RuntimeException("Username is required.");
        }

    }

    public Set<String> getUserPrivileges(HRSUser user) {
        return user.getPrivileges();
    }

    public HRSUser getUserDetail(String userId) {
        return userRepository.findOneByUuid(userId);
    }

    public Role addRole(RoleRequest request) {
        Role role = roleRepository.findByName(request.getName());
        if (role != null) {
            throw new RuntimeException(String.format("Already exist Role with name %s", request.getName()));
        }
        role = request.toEntity();
        for (String privilege : request.getPrivileges()) {
            Privilege temp = privilegeRepository.getOneByName(privilege);
            role.addPrivilege(temp);
        }
        return roleRepository.save(role);
    }

    public Role updateRole(RoleRequest request) {
        Role role = roleRepository.findByName(request.getName());
        role = request.toEntity(role);
        role.getPrivileges().clear();
        for (String privilege : request.getPrivileges()) {
            Privilege temp = privilegeRepository.getOneByName(privilege);
//            if (!validateRoleSaveAccess(role, temp)){
//                throw new ValidationException("You don't have access to update this Role!");
//            }
            role.addPrivilege(temp);
        }
        return roleRepository.save(role);
    }

    public Role getRole(String roleId) {
        return roleRepository.findOneByUuid(roleId);
    }

    public void updatePassword(UserRequest request) {
        validatePassword(request.getPassword());
        Optional<HRSUser> opUser = userRepository.findByUuid(request.getId());
        if (opUser.isPresent()) {
            HRSUser user = opUser.get();
            user.setPasswordHash(encoder.encode(request.getPassword()));
            user.setLastPassModifiedDate(new Date());
            userRepository.saveAndFlush(user);
            authCache.getAuthCache().invalidateAll();
        } else {
            throw new RuntimeException("Invalid User Id(unmasked) : " + request.getId());
        }
    }

    private boolean validatePassword (String password) {
        Pattern passwordPattern = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,10}$");
        if (password.isEmpty() || !passwordPattern.matcher(password).matches()) {
            throw new RuntimeException("Password should be at least 1 lowercase letter, 1 uppercase letter, 1 digit, 1 special character  and is at least 8 and at most 10 characters long!");
        }
        return true;
    }
}
