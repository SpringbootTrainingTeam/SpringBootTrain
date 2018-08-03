package com.hsbc.springboot.pojo.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@ToString
@Entity
@Table(name = "boot_user")
public class BootUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String username;
    private String password;
    @ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    private List<BootRole> roles;

    private Date createTime;
    private Date updateTime;

    public BootUser() {}

    public BootUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
