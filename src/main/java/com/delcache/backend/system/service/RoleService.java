package com.delcache.backend.system.service;

import com.delcache.common.entity.Role;
import com.delcache.common.entity.RoleAdmin;
import com.delcache.common.entity.RoleMenu;
import com.delcache.common.service.BaseService;
import com.delcache.extend.Db;
import com.delcache.extend.Encrypt;
import com.delcache.extend.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;


@Service
@Transactional
public class RoleService extends BaseService {

    public Map<String, Object> getList(Map<String, Object> params) {
        Db selector = db.table(Role.class);
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
     * 设置权限用户
     *
     * @param params
     */
    public void setRoleAdmin(Map<String, Object> params) {
        List<String> newList = new ArrayList<>();
        if (!StringUtils.isEmpty(params.get("admin_id"))) {
            newList.addAll(Arrays.asList(params.get("admin_id").toString().split(",")));
        }
        List<RoleAdmin> roleMenus = (List<RoleAdmin>) this.db.table(RoleAdmin.class).where("role_id", params.get("id")).findAll();
        List<String> oldList = Util.arrayColumn(roleMenus, "adminId");
        List<String> removeList = new ArrayList<>(oldList);
        removeList.removeAll(newList);
        newList.removeAll(oldList);
        List<RoleAdmin> inserts = new ArrayList<>();
        for (String adminId : newList) {
            RoleAdmin roleAdmin = new RoleAdmin();
            roleAdmin.setAdminId(Integer.parseInt(adminId));
            roleAdmin.setRoleId(Integer.parseInt(params.get("id").toString()));
            inserts.add(roleAdmin);
        }
        if (inserts.size() > 0) {
            this.db.table(RoleAdmin.class).multiInsert(inserts);
        }
        if (removeList.size() > 0) {
            this.db.table(RoleAdmin.class).where("role_id", params.get("id")).where("admin_id", removeList, "in").delete();
        }
    }

    /**
     * 设置角色权限
     *
     * @param params
     */
    public void setRoleMenu(Map<String, Object> params) {
        List<String> newList = new ArrayList<>();
        if (!StringUtils.isEmpty(params.get("menu_ids"))) {
            newList.addAll(Arrays.asList(params.get("menu_ids").toString().split(",")));
        }
        List<RoleMenu> roleMenus = (List<RoleMenu>) this.db.table(RoleMenu.class).where("role_id", params.get("id")).findAll();
        List<String> oldList = Util.arrayColumn(roleMenus, "menuId");
        List<String> removeList = new ArrayList<>(oldList);
        removeList.removeAll(newList);
        newList.removeAll(oldList);
        List<RoleMenu> inserts = new ArrayList<>();
        for (String menuId : newList) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(Integer.parseInt(params.get("id").toString()));
            roleMenu.setMenuId(Integer.parseInt(menuId));
            inserts.add(roleMenu);
        }
        if (inserts.size() > 0) {
            this.db.table(RoleMenu.class).multiInsert(inserts);
        }
        if (removeList.size() > 0) {
            this.db.table(RoleMenu.class).where("role_id", params.get("id")).where("menu_id", removeList, "in").delete();
        }
    }

}