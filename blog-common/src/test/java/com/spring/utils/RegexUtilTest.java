package com.spring.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegexUtilTest {

    RegexUtil regexUtil = new RegexUtil();

    @Test
    void isValidEmail() {
        String email = "test@test.com";
        System.out.println(regexUtil.isValidEmail(email));
    }
}