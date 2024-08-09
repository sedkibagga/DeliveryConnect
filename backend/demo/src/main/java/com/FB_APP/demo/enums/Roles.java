package com.FB_APP.demo.enums;

import java.util.List;

public class Roles {
    public static final String ADMIN = "ADMIN";
    public static final String SEDENTARY = "SEDENTARY";
    public static final String CLIENT = "CLIENT";
    public static final String DELIVERY = "DELIVERY";
    public static final String SUPPORTER = "SUPPORTER";
    public static final String  WAREHOUSE = "WAREHOUSE";
    public static final List<String> ALL_ROLES = List.of(ADMIN, SEDENTARY, CLIENT, DELIVERY, SUPPORTER , WAREHOUSE);
}

