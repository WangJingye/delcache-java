package com.delcache.common.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
@Table(name = "tbl_role_admin")
@DynamicInsert(true)
public class RoleAdmin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "admin_id")
    private int adminId;

    @Column(name = "role_id")
    private int roleId;

    @Column(name = "create_time")
    private int createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }
}
