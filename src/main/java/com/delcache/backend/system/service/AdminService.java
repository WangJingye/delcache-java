package com.delcache.backend.system.service;

import com.delcache.backend.common.BaseService;
import com.delcache.common.entity.Admin;
import com.delcache.extend.Db;
import com.delcache.extend.Encrypt;
import com.delcache.extend.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
@Transactional
public class AdminService extends BaseService {

    public Map<String, Object> getList(Map<String, Object> params) {
        Db selector = Db.table(Admin.class);
        if (Util.parseInt(params.get("status")) != 0) {
            selector.where("status", params.get("status"));
        }
        if (Util.parseInt(params.get("admin_id")) != 0) {
            selector.where("admin", params.get("admin"));
        }
        if (!StringUtils.isEmpty(params.get("username"))) {
            selector.where("username", params.get("username"), "like");
        }
        if (!StringUtils.isEmpty(params.get("username"))) {
            selector.where("username", params.get("username"), "like");
        }
        return this.pagination(selector, params);
    }

    /**
     * 修改密码
     * @param user
     * @param password
     */
    public void changePassword(Admin user, String password) {
        Random random = new Random();
        String salt = Util.strPad(random.nextInt(9999), 4, "0", 1);
        Map<String, Object> update = new HashMap<>();
        update.put("salt", salt);
        update.put("password", Encrypt.encryptPassword(password, salt));
        Db.table(Admin.class).where("admin_id", user.getAdminId()).update(update);
    }
}