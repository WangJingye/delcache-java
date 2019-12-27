package com.delcache.extend.wechat;

import com.delcache.common.entity.SiteInfo;
import com.delcache.component.Db;
import com.delcache.component.Util;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.*;

public class WechatPay {

    /**
     * 统一支付接口
     * data数组需要信息
     * out_trade_no 支付单号
     * title 标题
     * money 金额
     * type 类型，回调值通过attach判断订单类型
     *
     * @return mixed 小程序需要返回值调起支付
     * @throws \Exception
     */
    public static Map<String, String> createPreOrder(String title, String outTradeNo, double money) throws Exception {
        if (money == 0) {
            return null;
        }
        SiteInfo siteInfo = (SiteInfo) Db.table(SiteInfo.class).find();
        String randStr = String.valueOf(Util.time()) + Util.strPad(new Random().nextInt(999999), 6, "0", 1);
        Map<String, String> params = new TreeMap<>();
        params.put("appid", siteInfo.getWechatAppId());
        params.put("mch_id", siteInfo.getWechatMchId());
//        params.put("openid", );
        params.put("body", title);
        params.put("nonce_str", DigestUtils.md5Hex(siteInfo.getWechatAppId() + randStr));//随机字符串
        params.put("notify_url", siteInfo.getWebHost() + "/v1/pay/notify");//回调的url
        params.put("spbill_create_ip", siteInfo.getWechatAppId());//回调的url
        params.put("out_trade_no", outTradeNo);//回调的url
        params.put("total_fee", String.valueOf((int) (money * 100)));//因为充值金额最小是1 而且单位为分 如果是充值1元所以这里需要*100
        params.put("trade_type", "JSAPI");//交易类型 默认
        //这里是按照顺序的 因为下面的签名是按照顺序 排序错误 肯定出错
        params.put("sign", WechatPay.sign(params, siteInfo.getWechatPayKey()));//交易类型 默认
        StringBuilder postXml = new StringBuilder("<xml>");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            postXml.append("<").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry.getKey()).append(">");
        }
        postXml.append("</xml>");
        //统一接口prepay_id
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String retString = Util.sendRequest(url, postXml.toString(), "POST", null);
        Map response = Util.xmlToMap(retString);
        if (response == null) {
            throw new Exception("支付返回信息有误");
        }
        if (response.get("return_code") == "SUCCESS") {
            Map<String, String> res = new LinkedHashMap<>();
            res.put("appId", params.get("appid"));
            res.put("nonceStr", params.get("nonce_str"));//随机字符串
            res.put("package", "prepay_id=" + res.get("prepay_id"));
            res.put("signType", "MD5");
            res.put("timeStamp", String.valueOf(Util.time()));//时间戳
            res.put("paySign", WechatPay.sign(res, siteInfo.getWechatPayKey()));//签名,具体签名方案参见微信公众号支付帮助文档;
            res.put("outTradeNo", outTradeNo);//签名,具体签名方案参见微信公众号支付帮助文档;
            return res;
        } else {
            throw new Exception(response.get("return_msg").toString());
        }
    }

    /**
     * 微信支付回调处理方法
     * @param xml
     * @return
     * @throws Exception
     */
    public Map notify(String xml) throws Exception {
        SiteInfo siteInfo = (SiteInfo) Db.table(SiteInfo.class).find();
        Map response = Util.xmlToMap(xml);
        if (response == null) {
            throw new Exception("交易失败");
        }
        if (response.get("return_code") == null || !"SUCCESS".equals(response.get("return_code"))) {
            throw new Exception("交易失败");
        }
        String sign = (String) response.get("sign");
        if (sign == null) {
            throw new Exception("签名失败");
        }
        String newSign = WechatPay.sign(response, siteInfo.getWechatPayKey());
        if (!sign.equals(newSign)) {
            throw new Exception("签名失败");
        }
        return response;
    }

    /**
     * 退款
     * @param transcationId
     * @param outRefundNo
     * @param money
     * @throws Exception
     */
    public void refund(String transcationId, String outRefundNo, double money) throws Exception {
        SiteInfo siteInfo = (SiteInfo) Db.table(SiteInfo.class).find();
        String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
        String randStr = String.valueOf(Util.time()) + Util.strPad(new Random().nextInt(999999), 6, "0", 1);
        Map<String, String> params = new TreeMap<>();
        params.put("appid", siteInfo.getWechatAppId());
        params.put("mch_id", siteInfo.getWechatMchId());
        params.put("nonce_str", DigestUtils.md5Hex(siteInfo.getWechatAppId() + randStr));//随机字符串
        params.put("transaction_id", transcationId);//交易流水号
        params.put("out_refund_no", outRefundNo);//退款单号
        String payMoney = String.valueOf((int) (money * 100));
        params.put("total_fee", payMoney);//总金额
        params.put("refund_fee", payMoney);//退款金额
        params.put("sign", WechatPay.sign(params, siteInfo.getWechatPayKey()));//签名
        StringBuilder postXml = new StringBuilder("<xml>");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            postXml.append("<").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry.getKey()).append(">");
        }
        postXml.append("</xml>");

        String ret = Util.sendRequest(url, postXml.toString(), "POST", null);
        Map response = Util.xmlToMap(ret);
        if (response == null) {
            throw new Exception("退款返回信息有误");
        }
        if (!"SUCCESS".equals(response.get("return_code"))) {
            throw new Exception(response.get("return_msg").toString());
        }
    }


    /**
     * 签名
     *
     * @return string
     */
    public static String sign(Map<String, String> params, String payKey) {
        StringBuilder stringA = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (StringUtils.isEmpty(entry.getValue())) {
                continue;
            }
            if ("sign".equals(entry.getKey())) {
                continue;
            }
            if (stringA.length() > 0) {
                stringA.append("&");
            }
            stringA.append(entry.getKey()).append("=").append(entry.getValue());
        }
        //申请支付后有给予一个商户账号和密码，登陆后自己设置key
        return DigestUtils.md5Hex(stringA.append("&key=").append(payKey).toString()).toUpperCase();
    }
}
