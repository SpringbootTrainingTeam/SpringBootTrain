package com.hsbc.springboot.pojo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "boot_user_role")
public class BootUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    private Short userRoleId;
    private Short roleId;
    private Date updateTime;
    private Date createTime;

    @Column(length = 1)
    private String deleteFlag;
}
