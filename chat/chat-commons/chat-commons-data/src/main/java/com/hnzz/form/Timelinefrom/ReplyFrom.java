package com.hnzz.form.Timelinefrom;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @PackageName:com.hnzz.form.Timelinefrom
 * @ClassName:Replyfrom
 * @Author 冼大丰
 * @Date 2023/2/10 11:46
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("朋友圈回复点赞表")
public class ReplyFrom implements Serializable {
    @ApiModelProperty("朋友圈Id")
    private String timelineId;
    @ApiModelProperty("朋友圈用户Id")
    private String authorId;
    @ApiModelProperty("用户名称")
    private String username;
    @ApiModelProperty("朋友圈用户头像")
    private String avatarUrl;
    @ApiModelProperty("评论的回复")
    private String text;

}
