package com.hsbc.springboot.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NodeEntity {
    private String id;
    private String pId;
    private String name;
    private Boolean isOpen;
    private String file;
    private String url;
    private String target;
    private Boolean isParent;
    private String iconPath;
    private Boolean checked;
}
