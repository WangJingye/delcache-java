package com.delcache.common.entity;

import com.delcache.component.PrimaryKey;

public class Admin extends BaseEntity {

    @PrimaryKey
    private int adminId;

    private String username;

    private String password;

    private String realname;

    private String mobile;

    private String email;

    private String avatar;

    private String salt;

    private int identity;

    private int lastLoginTime;

    private int passwdModifyTime;

    private int status;

    private int createTime;

    private int updateTime;

    public Integer getAdminId() {
        return this.adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealname() {
        return this.realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getIdentity() {
        return this.identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setLastLoginTime(int lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getPasswdModifyTime() {
        return this.passwdModifyTime;
    }

    public void setPasswdModifyTime(int passwdModifyTime) {
        this.passwdModifyTime = passwdModifyTime;
    }

    public int getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
