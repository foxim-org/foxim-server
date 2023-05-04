package com.hnzz.service.impl;

import com.hnzz.commons.base.exception.AppException;
import com.hnzz.dao.SmsDao;
import com.hnzz.entity.Sms;
import com.hnzz.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

/**
 * @PackageName:com.hnzz.service.impl
 * @ClassName:SmsServiceImpl
 * @Author zj
 * @Date 2023/3/26 15:30
 * @Version 1.0
 **/
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Resource
    private SmsDao smsDao;

    @Value("${smsbao.username}")
    private String username;

    @Value("${smsbao.password}")
    private String password;

    @Value("${smsbao.url}")
    private String apiUrl;

    @Value("${smsbao.content}")
    private String content;

    @Override
    public void sendVerificationCode(String mobile) {
        // 删除过期的验证码
        smsDao.deleteByExpireTimeBefore(LocalDateTime.now());

        // 生成6位随机验证码
        String code = String.valueOf(new Random().nextInt(900000) + 100000);

        // 将验证码保存到MongoDB中
        Sms sms = new Sms();
        sms.setMobile(mobile);
        sms.setCode(code);
        sms.setExpireTime(LocalDateTime.now().plusMinutes(5));
        smsDao.save(sms);

        // 发送短信验证码
        String contentCode = String.format(content, code);
        StringBuffer httpArg = new StringBuffer();
        httpArg.append("u=").append(username).append("&");
        httpArg.append("p=").append(md5(password)).append("&");
        httpArg.append("m=").append(mobile).append("&");
        httpArg.append("c=").append(encodeUrlString(contentCode, "UTF-8"));
        String result = request(apiUrl, httpArg.toString());
        if (!result.equals("0")){
            log.error("验证码发送失败: {}",result);
            throw new AppException("验证码发送失败");
        }
    }

    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = reader.readLine();
            if (strRead != null) {
                sbf.append(strRead);
                while ((strRead = reader.readLine()) != null) {
                    sbf.append("\n");
                    sbf.append(strRead);
                }
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String md5(String plainText) {
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    public static String encodeUrlString(String str, String charset) {
        String strret = null;
        if (str == null)
            return str;
        try {
            strret = java.net.URLEncoder.encode(str, charset);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return strret;
    }

    @Override
    public void verifyVerificationCode(String mobile, String code) {
        // 查询验证码是否存在
        Optional<Sms> sms = smsDao.findByMobileAndCode(mobile, code);
        if (!sms.isPresent()) {
            throw new AppException("验证码输入错误或验证码不存在!");
        }

        // 验证码已过期
        Sms verificationCode = sms.get();
        if (verificationCode.getExpireTime().isBefore(LocalDateTime.now())) {
            smsDao.delete(verificationCode);
            throw new AppException("验证码已过期!");
        }
        smsDao.delete(verificationCode);
    }

}
