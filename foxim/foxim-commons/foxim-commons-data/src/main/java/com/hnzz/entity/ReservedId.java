package com.hnzz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author HB on 2023/2/9
 * TODO 注册时的号池展示
 */
@Data
@Document(collation = "ids")
@ApiModel("选号展示内容")
public class ReservedId {
    /**
     * 编号
     */
    @Field("id")
    @ApiModelProperty("编号")
    private Integer id;

}
