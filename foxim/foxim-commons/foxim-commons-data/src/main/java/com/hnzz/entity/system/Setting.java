package com.hnzz.entity.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author HB on 2023/5/5
 * TODO 系统设置实体类
 */
@Data
@Document("setting")
@ApiModel("系统设置基础类")
public class Setting {
    @Id
    private String id;
    @ApiModelProperty("配置名")
    private String name;
    @ApiModelProperty("配置值 , 用json格式保存")
    private String value;
    @ApiModelProperty("操作者")
    private String operator;
    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    @ApiModelProperty("更新时间")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
}
