package com.hnzz.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @PackageName:com.hnzz.entity
 * @ClassName:Sms
 * @Author zj
 * @Date 2023/3/26 15:26
 * @Version 1.0
 **/
@Data
@Document(collection = "sms")
@ApiModel("短信信息表")
public class Sms implements Serializable {
    @Id
    @ApiModelProperty("短信ID")
    private String id;
    @Field
    @ApiModelProperty("手机号")
    private String mobile;
    @Field
    @ApiModelProperty("验证码")
    private String code;
    @Field
    @ApiModelProperty("时间")
    private LocalDateTime expireTime;
}
