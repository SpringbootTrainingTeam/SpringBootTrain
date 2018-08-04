package com.hsbc.springboot.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@ToString
@AllArgsConstructor
@Table(name = "boot_role")
public class BootRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    public BootRole(){}
    public BootRole(String name) {
        this.name = name;
    }
    private Date createTime;
    private Date updateTime;
}
