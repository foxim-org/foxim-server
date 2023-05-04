package com.hnzz.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @author HB on 2023/2/20
 * TODO 验证码
 */
@Data
@Document("checkCode")
public class CheckCode {
    @Id
    private String id;
    @Field
    private String mobile;
    @Field
    private String code;

    /**
     * 过期时间
     */
    @Field
    private Date expireAt;
}
