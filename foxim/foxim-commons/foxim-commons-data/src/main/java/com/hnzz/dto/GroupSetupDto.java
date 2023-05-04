package com.hnzz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @PackageName:com.hnzz.dto
 * @ClassName:GroupSetupDto
 * @Author 冼大丰
 * @Date 2023/2/17 11:20
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("群聊设置展示页面")
public class GroupSetupDto {
    /**
     * 用户给群聊的备注
     */
    @ApiModelProperty("用户给群聊的备注")
    private String remarksName;
    /**
     * 群名称
     */
    @ApiModelProperty("群名称")
    private String GroupName;
    /**
     * 群链接
     */
    @ApiModelProperty("群链接")
    private String GroupConnection;
    /**
     * 群公告
     */
    @ApiModelProperty("群公告")
    private String notice;
    /**
     * 群成员在群里的昵称
     */
    @ApiModelProperty("群成员在群里的昵称")
    private String username;
    /**
     * 群聊天背景
     */
    @ApiModelProperty("群聊天背景")
    private String backGround;
    @ApiModelProperty("群置顶")
    private Boolean isSticky;
    @ApiModelProperty("免打扰")
    private Boolean isMuted;

}
