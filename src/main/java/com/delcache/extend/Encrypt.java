package com.delcache.extend;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

public class Encrypt {

    public static String encryptPassword(String password, String salt) {
        if (StringUtils.isEmpty(password)) {
            password = "";
        }
        return DigestUtils.md5Hex(salt + DigestUtils.md5Hex(password + salt));
    }
}
