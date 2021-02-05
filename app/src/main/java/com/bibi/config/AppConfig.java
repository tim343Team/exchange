package com.bibi.config;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/9
 */
public class AppConfig {
    //测试
//    static String BASE_IP="bx11api.smartdatachain.com";
    static String BASE_IP="42.192.184.221";
    static String SOCKET_IP="42.192.184.221";
    public static String BASE_URL = "http://"+BASE_IP+":8003";
    //生产
//    static String BASE_IP = "152.32.185.198";
//    static String SOCKET_IP = "152.32.187.116";
//    public static String BASE_URL = "http://"+BASE_IP;

    public static String C2C_URL = SOCKET_IP;
    public static String MARKET_URL = SOCKET_IP;
    public static String GROUP_URL = SOCKET_IP;
    public static String HEART_URL = SOCKET_IP;
}
