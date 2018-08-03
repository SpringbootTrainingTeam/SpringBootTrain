package com.hsbc.springboot.controller;

import com.hsbc.springboot.pojo.entity.BootUser;
import com.hsbc.springboot.pojo.form.BootUserForm;
import com.hsbc.springboot.pojo.vo.BootUserVo;
import com.hsbc.springboot.service.api.BootUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final BootUserService bootUserService;

    @Autowired
    public UserController(BootUserService bootUserService) {
        this.bootUserService = bootUserService;
    }

    @PostMapping("/register")
    public Object register(@RequestBody BootUserForm bootUserForm) {
        return bootUserService.register(bootUserForm);
    }

    @GetMapping("/{id}")
    public Object getUserById(@PathVariable(value = "id") Integer id) {
        BootUser bootUser = bootUserService.findUserById(id);
        BootUserVo bootUserVo = new BootUserVo();
        bootUserVo.setId(bootUser.getId());
        bootUserVo.setName(bootUser.getUsername());
        bootUserVo.setCreateTime(bootUser.getCreateTime());
        bootUserVo.setUpdateTime(bootUser.getUpdateTime());
        return bootUserVo;
    }

}
