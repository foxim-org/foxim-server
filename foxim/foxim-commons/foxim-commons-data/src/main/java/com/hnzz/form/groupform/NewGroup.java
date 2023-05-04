package com.hnzz.form.groupform;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.zzkj.vo
 * @ClassName:NewGroup
 * @Author 冼大丰
 * @Date 2023/1/5 12:44
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("新建群组表单")
public class NewGroup {
    /**
     * 群名称
     */
    @ApiModelProperty("群名，必须有")
    @NotBlank(message = "请输入群名称")
    private String name;
    /**
     * 群头像
     */
    @ApiModelProperty("群头像")
    private String groupHead;
    /**
     * 群介绍
     */
    @ApiModelProperty("群介绍")
    private String desc=null;
    /**
     * 群公告
     */
    @ApiModelProperty("群公告")
    private String notice=null;
    /**
     * 群的标签
     */
    @ApiModelProperty("群标签")
    private List<String> tags;
    /**
     * 群成员
     */
    @ApiModelProperty("群成员用户Id")
    private List<String> usersId;
    /**
     *
     * 群聊背景
     */
    @ApiModelProperty("群聊背景")
    private String backGround="默认背景";
    /**
     * 用于清空聊天
     */
    @ApiModelProperty("用于清空聊天")
    private String messageTTL;
    /**
     * 是否开启机器人功能
     */
    @ApiModelProperty("是否开启机器人功能")
    private Boolean openBots=false;
    /**
     * 是否允许通过群添加好友
     */
    @ApiModelProperty("是否允许通过群添加好友")
    private Boolean addAble=false;
    /**
     * 是否允许撤回
     */
    @ApiModelProperty("是否允许撤回")
    private Boolean undoAble=false;
    /**
     * 是否允许群成员更改群组头像
     */
    @ApiModelProperty("是否允许群成员更改群组头像")
    private Boolean changeGroupHead=false;
    /**
     * 是否允许群成员更改群组名称
     */
    @ApiModelProperty("是否允许群成员更改群组名称")
    private Boolean changeGroupName=false;
    /**
     * 是否允许群成员新增成员
     */
    @ApiModelProperty("是否允许群成员新增成员")
    private Boolean addGroupUser=false;
    /**
     * 是否允许群成员新增或编辑贴文
     */
    @ApiModelProperty("是否允许群成员新增或编辑贴文")
    private Boolean editorialPost=false;
    /**
     * 是否允许群成员提醒他人
     */
    @ApiModelProperty("是否允许群成员提醒他人")
    private Boolean remindOthers=false;
    /**
     * 是否允许群成员群组通话
     */
    @ApiModelProperty("是否允许群成员群组通话")
    private Boolean groupCall=false;
    /**
     * 是否允许群成员成员私聊
     */
    @ApiModelProperty("是否允许群成员成员私聊")
    private Boolean privateMemberChat=false;
    /**
     * 全局禁言,不禁言
     */
    @ApiModelProperty("全局禁言,默认不禁言")
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date silencedTo=new Date();
    /**
     * 创建群聊的时间
     */
    @ApiModelProperty("创建群聊的时间")
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date createdAt=new Date();

    @ApiModelProperty("最后聊天排序")
    private Date recentAt=new Date();
}
