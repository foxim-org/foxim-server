package com.hnzz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @author HB on 2023/2/23
 * TODO 文件实体类
 */
@Data
@Document("files")
public class FileInfo {
    @Id
    @ApiModelProperty("主键id")
    private String id;
    @Field
    @ApiModelProperty("文件编号fileId")
    private String fid;
    @Field
    @ApiModelProperty("文件标识符")
    private String eTag;
    @Field
    @ApiModelProperty("文件路径")
    private String fileUrl;
    @Field
    @ApiModelProperty("用于个人文件过滤")
    private String userId;
    @Field
    @ApiModelProperty("用于群组文件过滤")
    private String groupId;
    @Field
    @ApiModelProperty("文件名")
    private String fileName;
    @Field
    @ApiModelProperty("用于决定文件类型")
    private String contentType;
    @Field
    @ApiModelProperty("排序条件，用于分组")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadedAt;
    @Field
    @ApiModelProperty("文件大小")
    private long size;
}
