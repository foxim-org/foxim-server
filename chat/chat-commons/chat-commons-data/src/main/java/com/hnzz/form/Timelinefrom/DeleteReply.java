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
 * @ClassName:DeleteReply
 * @Author 冼大丰
 * @Date 2023/2/14 11:07
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("删除回复")
public class DeleteReply implements Serializable {
    @ApiModelProperty("朋友圈Id")
    private String timelineId;
    @ApiModelProperty("回复Id")
    private String replyId;
}
