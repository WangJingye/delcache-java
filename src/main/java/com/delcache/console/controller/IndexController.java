package com.delcache.console.controller;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class IndexController {
    public static void main(String[] args) {
        Map<Integer, String> result = new HashMap<>();
        System.out.println(String.join(",", result.values()));

//        String[] valueArray =   new String[result.size()];
//        result.values().toArray(valueArray);
//        System.out.println(String.join(",",result.values()));
    }
}
