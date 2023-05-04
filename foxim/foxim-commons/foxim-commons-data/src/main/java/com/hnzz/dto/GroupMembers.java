package com.hnzz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @PackageName:com.hnzz.dto
 * @ClassName:Page
 * @Author 冼大丰
 * @Date 2023/1/10 14:41
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("群成员显示表")
public class GroupMembers implements Serializable {

    @ApiModelProperty("用户Id")
    private String Id;
    /**
     * 用户覆盖用户在群里的昵称
     */
    @ApiModelProperty("用户在群里的昵称")
    private String username;
    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String avatarUrl;

    @ApiModelProperty("是否为群管理员")
    private Boolean isAdmin;

    @ApiModelProperty("是否为群主")
    private Boolean isBoss;
}
