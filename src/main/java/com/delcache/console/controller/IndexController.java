package com.delcache.console.controller;

import com.delcache.common.entity.SiteInfo;
import com.delcache.common.entity.TestAdmin;

import java.beans.*;

public class IndexController {
    public static void main(String[] args) throws Exception {
        BeanInfo bif = Introspector.getBeanInfo(SiteInfo.class, Object.class);
        PropertyDescriptor[] pd = bif.getPropertyDescriptors();
//        for (int i = 0; i < pd.length; i++) {
//            System.out.println(pd[i].getReadMethod().getName());
//            System.out.println(pd[i].getPropertyType());
//        }
//        MethodDescriptor[] md = bif.getMethodDescriptors();
//        for(int m = 0; m<md.length; m++){
//            System.out.println("name:"+md[m].getName());
//            System.out.println("method name:"+md[m].getMethod().getName());
//            System.out.println("displayname:"+md[m].getDisplayName());
//        }
//        BeanDescriptor bd = bif.getBeanDescriptor();
//        System.out.println("display name：" + bd.getDisplayName());
//        System.out.println("name：" + bd.getName());
//        EventSetDescriptor[] esd = bif.getEventSetDescriptors();
//        for(int m=0;m<esd.length;m++){
//            System.out.println("name:"+esd[m].getName());
//            System.out.println("method name:"+esd[m].getGetListenerMethod().getName());
//            System.out.println("method name:"+esd[m].getRemoveListenerMethod().getName());
//            System.out.println("method name:"+esd[m].getAddListenerMethod().getName());
//            System.out.println("displayname:"+esd[m].getDisplayName());
//        }
    }


}