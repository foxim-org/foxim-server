package com.hnzz.form.userform;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @PackageName:com.hnzz.form.userform
 * @ClassName:Timelines
 * @Author 冼大丰
 * @Date 2023/1/28 15:21
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "朋友圈表")
public class SendTimelineFrom {
    @ApiModelProperty("朋友圈内容")
    private String text;
    @ApiModelProperty("朋友圈多图片")
    private List<String> imageUrls;
    @ApiModelProperty("朋友圈视频")
    private String videoUrls;
}
