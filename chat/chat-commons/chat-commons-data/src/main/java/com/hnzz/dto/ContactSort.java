package com.hnzz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author HB on 2023/1/29
 * TODO 用于返回聊天时间排序
 */

@Data
@Accessors(chain = true)
public class ContactSort implements Comparable<ContactSort>{

    @ApiModelProperty("群id")
    private String groupId;
    @ApiModelProperty("好友Id")
    private String contactId;
    @ApiModelProperty("群名称")
    private String groupName;
    @ApiModelProperty("好友名称")
    private String username;
    @ApiModelProperty("头像")
    private String avatarUrl;
    @ApiModelProperty("群头像")
    private String groupHead;
    @ApiModelProperty("最后聊天时间")
    private Date recentAt;
    @ApiModelProperty("最后聊天内容")
    private String text;
    @ApiModelProperty("是否置顶")
    private Boolean isSticky;
    @ApiModelProperty("消息免打扰")
    private Boolean isMuted;



    @Override
    public int compareTo(ContactSort o) {
        if (this.isSticky!=null&& o.getIsSticky()!=null && !this.isSticky.equals(o.isSticky)) {
            return isSticky ? -1 : 1;
        }
        return  o.getRecentAt().compareTo(this.getRecentAt());
    }
}
