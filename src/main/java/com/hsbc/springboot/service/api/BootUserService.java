package com.hsbc.springboot.service.api;

import com.hsbc.springboot.pojo.entity.BootUser;
import com.hsbc.springboot.pojo.form.BootUserForm;

public interface BootUserService {
    /**
     * user register
     *
     * @param bootUserForm user information
     * @return register user id
     */
    Integer register(BootUserForm bootUserForm);

    /**
     * find user by Id
     *
     * @param id user id
     * @return user
     */
    BootUser findUserById(Integer id);
}
