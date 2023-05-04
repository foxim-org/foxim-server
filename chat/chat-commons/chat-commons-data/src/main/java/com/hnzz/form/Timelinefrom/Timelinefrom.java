package com.hnzz.form.Timelinefrom;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.hnzz.form.Timelinefrom
 * @ClassName:Timelinefrom
 * @Author 冼大丰
 * @Date 2023/2/9 16:57
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("朋友圈转发表")
public class Timelinefrom implements Serializable {
    @ApiModelProperty("朋友圈Id")
    private String id;
    @ApiModelProperty("用户Id")
    private String userId;
    @ApiModelProperty("用户名称")
    private String username;
    @ApiModelProperty("头像")
    private String avatarUrl;
    @ApiModelProperty("朋友圈内容")
    private String text;
    @ApiModelProperty("转发时要说的内容")
    private String comment;
    @ApiModelProperty("朋友圈的图片")
    private List<String> imageUrls;
//    @ApiModelProperty("朋友圈发送时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private Date createdAt;
}
