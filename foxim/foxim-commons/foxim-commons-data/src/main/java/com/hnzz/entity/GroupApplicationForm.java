package com.hnzz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * @PackageName:com.hnzz.entity
 * @ClassName:GroupApplicationForm
 * @Author 冼大丰
 * @Date 2023/5/10 12:47
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Document("groupApplicationForm")
public class GroupApplicationForm implements Serializable {
    /**
     * 群申请表id
     */
    @Id
    @ApiModelProperty("群申请表id")
    private String id;
    /**
     * 群狐狸号
     */
    @Field
    @ApiModelProperty("群狐狸号")
    private Integer foxCode;
    @Field
    @ApiModelProperty("群ID")
    private String groupId;
    @Field
    @ApiModelProperty("申请加入群聊Id")
    private String userId;
    @Field
    @ApiModelProperty("申请加入群聊的状态PENDING（等待） | ACCEPTED（接受） ")
    private String status;
    @Field
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date createdAt;

    @Field
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date updateAt;

}
