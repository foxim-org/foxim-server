package com.hnzz.service;

public interface SmsService {

    void sendVerificationCode(String mobile);

    void verifyVerificationCode(String mobile, String code);
}
