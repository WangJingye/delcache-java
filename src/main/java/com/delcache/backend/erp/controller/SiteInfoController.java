package com.delcache.backend.erp.controller;

import com.delcache.backend.common.BaseController;
import com.delcache.common.entity.SiteInfo;
import com.delcache.extend.Db;
import com.delcache.extend.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class SiteInfoController extends BaseController {

    @RequestMapping(value = "erp/site-info/edit", method = RequestMethod.GET)
    public String edit(Model model) {
        model.addAttribute("title", "网站信息");
        SiteInfo data = (SiteInfo) Db.table(SiteInfo.class).find();
        model.addAttribute("data", data);
        return this.render("erp/site-info/edit", model);
    }

    @ResponseBody
    @RequestMapping(value = "erp/site-info/edit", method = RequestMethod.POST)
    public Object edit(HttpServletRequest request) {
        try {
            Map<String, Object> params = Request.getInstance(request).getParams();
            SiteInfo data = (SiteInfo) Db.table(SiteInfo.class).find();
            if (data == null) {
                data = new SiteInfo();
            }
            data.load(params);
            Db.table(SiteInfo.class).save(data);
            return this.success("操作成功");
        } catch (Exception e) {
            return this.error(e.getMessage());
        }
    }
}