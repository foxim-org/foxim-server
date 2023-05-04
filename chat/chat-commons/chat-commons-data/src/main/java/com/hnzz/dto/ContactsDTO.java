package com.hnzz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author HB on 2023/3/9
 * TODO
 */
@Data
@ApiModel("后台界面好友关系展示表")
public class ContactsDTO {
    @ApiModelProperty("关系id")
    private String id;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("用户昵称")
    private String username;
    @ApiModelProperty("用户头像")
    private String avatarUrl;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("好友Id")
    private String contactId;
    @ApiModelProperty("好友名称")
    private String name;
    @ApiModelProperty("创建时间")
    private Date createdAt;
    @ApiModelProperty("看他的朋友圈")
    private Boolean isFriendCircle;
    @ApiModelProperty("看我的朋友圈")
    private Boolean isMyCircle;
    @ApiModelProperty("备注")
    private String remark;
}
