package com.hnzz.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @PackageName:com.zzkj.entity
 * @ClassName:群成员关系表
 * @Author 冼大丰
 * @Date 2023/1/4 12:36
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class GroupUsers implements Serializable {
    /**
     * 群成员关系id
     */
    private String id;
    /**
     * 群聊的id
     */
    private String groupId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 清空聊天
     */
    private String messageTTL;
    /**
     *企业批量通过手机号导入会员可以用到，该会员可能没注册我们软件
     */
    private String mobile;
    /**
     * 用户覆盖用户在群里的昵称
     */
    private String username;
    /**
     * 用户头像
     */
    private String avatarUrl;
    /**
     * 用户给群聊的备注
     */
    private String remarksName;
    /**
     * 是否为群管理
     */
    private Boolean isAdmin;
    /**
     * 用于群置顶
     */
    private Boolean isSticky;
    /**
     * 用于免打扰
     */
    private Boolean isMuted;
    /**
     * 是否被禁言
     */
    private Boolean isSilencedTo;
    /**
     * 禁言特定时间，时间数设大点就显示为永久禁言
     */
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date silencedTo;
    /**
     * 加入时间，无法更改
     */
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date createdAt;
    /**
     * 用作最后聊天的排序条件
     * 最后聊天排序
     */
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date recentAt;
    /**
     * 最后转发消息时间
     */
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date lastForwardTime;
}
