package com.delcache.common.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "tbl_site_info")
@DynamicInsert(true)
public class SiteInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "default_password")
    private String defaultPassword;

    @Column(name = "web_host")
    private String webHost;

    @Column(name = "web_ip")
    private String webIp;

    @Column(name = "wechat_app_id")
    private String wechatAppId;

    @Column(name = "wechat_app_secret")
    private String wechatAppSecret;

    @Column(name = "wechat_mch_id")
    private String wechatMchId;

    @Column(name = "wechat_pay_key")
    private String wechatPayKey;

    @Column(name = "create_time")
    private int createTime;

    @Column(name = "update_time")
    private int updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public String getWebHost() {
        return webHost;
    }

    public void setWebHost(String webHost) {
        this.webHost = webHost;
    }

    public String getWebIp() {
        return webIp;
    }

    public void setWebIp(String webIp) {
        this.webIp = webIp;
    }

    public String getWechatAppId() {
        return wechatAppId;
    }

    public void setWechatAppId(String wechatAppId) {
        this.wechatAppId = wechatAppId;
    }

    public String getWechatAppSecret() {
        return wechatAppSecret;
    }

    public void setWechatAppSecret(String wechatAppSecret) {
        this.wechatAppSecret = wechatAppSecret;
    }

    public String getWechatMchId() {
        return wechatMchId;
    }

    public void setWechatMchId(String wechatMchId) {
        this.wechatMchId = wechatMchId;
    }

    public String getWechatPayKey() {
        return wechatPayKey;
    }

    public void setWechatPayKey(String wechatPayKey) {
        this.wechatPayKey = wechatPayKey;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }
}
