package com.delcache.common.controller;

import com.delcache.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
    @Autowired
    public BaseService Db;
}
