package com.hnzz.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * @PackageName:com.hnzz.form
 * @ClassName:GroupOutForm
 * @Author 冼大丰
 * @Date 2023/5/10 19:45
 * @Version 1.0
 **/
@Data
@ApiModel("群申请返回表")
public class GroupOutForm implements Serializable {
    /**
     * 群申请表id
     */

    @ApiModelProperty("群申请表id")
    private String id;
    /**
     * 群狐狸号
     */
    @ApiModelProperty("群狐狸号")
    private Integer foxCode;
    @ApiModelProperty("群ID")
    private String groupId;
    @ApiModelProperty("申请加入群聊Id")
    private String userId;
    @ApiModelProperty("申请加入群聊的状态PENDING（等待） | ACCEPTED（接受） ")
    private String status;
    private String username;
    private String avatarUrl;
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date createdAt;
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date updateAt;
}
