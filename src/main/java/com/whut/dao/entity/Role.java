package com.whut.dao.entity;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue
    private int id;
    //流水号
    @Column(name = "role_name")
    private String roleName;
    //工号
    @Column(name = "role_info")
    private String roleInfo;
    //日期

    public Role(String roleName, String roleInfo) {
        this.roleName = roleName;
        this.roleInfo = roleInfo;
    }

    public Role() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleInfo() {
        return roleInfo;
    }

    public void setRoleInfo(String roleInfo) {
        this.roleInfo = roleInfo;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", roleInfo='" + roleInfo + '\'' +
                '}';
    }
}
