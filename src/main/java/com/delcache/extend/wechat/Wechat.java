package com.delcache.extend.wechat;

import com.delcache.common.entity.SiteInfo;
import com.delcache.component.Db;
import com.delcache.component.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.util.Map;

public class Wechat {

    public static String getOpenIdByCode(String code) throws Exception {
        SiteInfo siteInfo = (SiteInfo) Db.table(SiteInfo.class).find();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
        url = String.format(url, siteInfo.getWechatAppId(), siteInfo.getWechatAppSecret(), code);
        String response = Util.sendRequest(url, "", "GET", null);
        if (StringUtils.isEmpty(response)) {
            throw new Exception("微信信息注册失败");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> apiData = objectMapper.readValue(response, Map.class);
        return apiData.get("open_id").toString();
    }

}
