package com.hrs.booking.service;

import com.hrs.booking.auth.HRSAuthorisations;
import com.hrs.booking.entity.Privilege;
import com.hrs.booking.entity.Role;
import com.hrs.booking.jpa.PrivilegeRepository;
import com.hrs.booking.jpa.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Service
public class PrivilegeService extends BaseService
{
    private static final Logger logger = LoggerFactory.getLogger(PrivilegeService.class);

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init()
    {
        Field[] fields = HRSAuthorisations.Privileges.class.getDeclaredFields();
        for (Field f : fields)
        {
            if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()))
            {
                logger.info("Found privilege {} ", f.getName());
                if (!(privilegeRepository.countByName(f.getName()) > 0))
                {
                    Privilege p = new Privilege();
                    p.setName(f.getName());
                    privilegeRepository.saveAndFlush(p);
                    logger.info("Not in db, saved {}", f.getName());

                    //attach it to tech admin role:)
                    Role role = roleRepository.getOneByName("TECHADMIN");
                    if (role == null)
                    {
                        role = new Role();
                        role.setName("TECHADMIN");
                        roleRepository.saveAndFlush(role);
                    }
                    role.addPrivilege(p);
                    roleRepository.saveAndFlush(role);
                    logger.info("attached to tech admin, saved {}", f.getName());
                }
            }
        }
        userService.createTechAdmin();
    }
}
