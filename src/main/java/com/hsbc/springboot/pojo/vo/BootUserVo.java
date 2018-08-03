package com.hsbc.springboot.pojo.vo;

import com.hsbc.springboot.pojo.entity.BootRole;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
public class BootUserVo {

    private Integer id;
    private String name;
    private List<BootRole> roles;
    private Date createTime;
    private Date updateTime;
}
