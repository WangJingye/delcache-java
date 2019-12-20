package com.delcache.backend.system.service;

import com.delcache.backend.common.config.Config;
import com.delcache.common.entity.Admin;
import com.delcache.common.entity.Menu;
import com.delcache.common.entity.RoleMenu;
import com.delcache.backend.common.BaseService;
import com.delcache.extend.Db;
import com.delcache.extend.UrlManager;
import com.delcache.extend.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Service
@Transactional
public class MenuService extends BaseService {

    @Autowired
    RequestMappingHandlerMapping handlerMapping;

    /**
     * 获取列表
     *
     * @param params
     * @return Map
     */
    public Map<String, Object> getList(Map<String, Object> params) {
        Db selector = Db.table(Menu.class);
        if (!StringUtils.isEmpty(params.get("status"))) {
            selector.where("status", params.get("status"));
        }
        if (!StringUtils.isEmpty(params.get("name"))) {
            selector.where("name", params.get("name"), "like");
        }
        if (!StringUtils.isEmpty(params.get("url"))) {
            selector.where("url", params.get("url"), "like");
        }
        selector.order("id desc");
        return this.pagination(selector, params);
    }


    /**
     * 获取当前菜单
     *
     * @return Menu
     */
    public Menu getCurrentMenu() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        String uri = UrlManager.parseRequestUrl(request.getRequestURI());
        return (Menu) Db.table(Menu.class).where("url", uri).find();
    }

    /**
     * 获取后台用户的可用菜单
     *
     * @return List
     */
    public List<Menu> getAdminMenus() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        Db selector = Db.table(Menu.class).where("status", 1);
        Admin user = (Admin) request.getSession().getAttribute("user");
        //不是超级管理员只能获取有权限的数据
        if (user.getIdentity() == 0) {
            String sql = "select a.* from tbl_role_menu as a left join tbl_role_admin as b on a.role_id = b.role_id where b.admin_id = " + user.getAdminId().toString() + ";";
            List<RoleMenu> roleMenus = (List<RoleMenu>) Db.table(RoleMenu.class).findAll(sql);
            selector.where("id", Util.arrayColumn(roleMenus, "menuId"), "in");
        }
        return (List<Menu>) selector.order("sort desc,create_time asc").findAll();
    }

    /**
     * 获取后台用户的菜单
     *
     * @return List
     */
    public List<Menu> getLayoutMenus() {
        List<Menu> rows = this.getAdminMenus();
        List<Menu> topList = new ArrayList<>();
        for (Menu menu : rows) {
            //顶部菜单
            if (menu.getParentId() == 0) {
                List<Menu> childList = this.getChild(rows, menu.getId());
                menu.extra = childList;
                if (menu.getUrl().isEmpty()) {
                    for (Menu child : childList) {
                        if (!StringUtils.isEmpty(child.getUrl())) {
                            menu.setUrl(child.getUrl());
                            break;
                        }
                    }
                }
                topList.add(menu);
            }
        }
        return topList;
    }

    /**
     * 获取菜单列表
     *
     * @param parent_id
     * @param i
     * @return Map
     */
    public Map<String, String> getChildMenus(int parent_id, int i) {
        Map<String, String> result = new LinkedHashMap<>();
        if (parent_id == 0) {
            result.put("0", "顶级目录");
        }
        List<Menu> rows = (List<Menu>) Db.table(Menu.class)
                .where("parent_id", parent_id)
                .where("status", 1)
                .order("sort desc,create_time asc")
                .findAll();
        i++;
        for (Menu v : rows) {
            String name = Util.strPad(v.getName(), (v.getName().length() + i * 2), "--", 1);
            result.put(String.valueOf(v.getId()), name);
            Map<String, String> child = this.getChildMenus(v.getId(), i);
            result.putAll(child);
        }
        return result;
    }

    /**
     * @param rows
     * @param id
     * @return List
     */
    public List<Menu> getChild(List<Menu> rows, int id) {
        List<Menu> childList = new ArrayList<>();
        for (Menu menu : rows) {
            if (menu.getParentId() == id) {
                List<Menu> children = this.getChild(rows, menu.getId());
                menu.extra = children;
                if (menu.getUrl().isEmpty()) {
                    for (Menu child : children) {
                        if (!StringUtils.isEmpty(child.getUrl())) {
                            menu.setUrl(child.getUrl());
                            break;
                        }
                    }
                }
                childList.add(menu);
            }
        }
        return childList;
    }


    /**
     * @return List
     */
    public List<Menu> getActiveMenu() {
        Menu m = this.getCurrentMenu();
        List<Menu> activeList = new ArrayList<>();
        if (m != null) {
            activeList = this.getParent(m);
        }
        return activeList;
    }

    public List<Menu> getParent(Menu menu) {
        List<Menu> menus = new ArrayList<>();
        menus.add(menu);
        if (menu.getParentId() == 0) {
            return menus;
        }
        Menu m = (Menu) Db.table(Menu.class).where("id", menu.getParentId()).find();
        menus.addAll(this.getParent(m));
        return menus;
    }

    /**
     * @param id
     * @return List
     */
    public List<String> getAllMethodList(String id) {
        List<Menu> menuList = (List<Menu>) Db.table(Menu.class).where("url", "", "!=").findAll();
        List<String> existList = Util.arrayColumn(menuList, "url");
        for (Map.Entry<String, String[]> entry : Config.actionNoLoginList.entrySet()) {
            for (String action : entry.getValue()) {
                existList.add("/" + Util.stringTrim(entry.getKey() + "/" + action, "/"));

            }
        }
        for (Map.Entry<String, String[]> entry : Config.actionWhiteList.entrySet()) {
            for (String action : entry.getValue()) {
                existList.add("/" + Util.stringTrim(entry.getKey() + "/" + action, "/"));

            }
        }
        List<String> urlList = new ArrayList<>();

        String currentUrl = "";
        if (!StringUtils.isEmpty(id)) {
            Menu current = (Menu) Db.table(Menu.class).where("id", id).find();
            currentUrl = current.getUrl();
        }
        Map<RequestMappingInfo, HandlerMethod> map = this.handlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            String url = UrlManager.parseMethodUrl(entry.getKey().getPatternsCondition().getPatterns().toString());
            if ((!existList.contains(url) || currentUrl.equals(url)) && !urlList.contains(url)) {
                urlList.add(url);
            }
        }
        return urlList;
    }

}
