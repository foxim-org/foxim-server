package com.hnzz.form.bots;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * @author HB on 2023/2/22
 * TODO 机器人自动转发设置
 */
@Data
@ApiModel("机器人自动转发设置")
public class BotsAutoForwardSetting {

    @ApiModelProperty("机器人id (必填)")
    @NotBlank
    private String id;

    @ApiModelProperty("是否开启自动转发")
    private Boolean forward;

    @ApiModelProperty("设定延迟,单位为秒")
    private Integer deplayed;

    @ApiModelProperty(value = "信息源Ids , 即被转发的用户id集合",dataType = "List<String>")
    private List<String> sourceIds;
    /**
     * 转发目标群 id集合
     */
    @ApiModelProperty(value = "转发目标 (转发至群) id集合",dataType = "List<String>")
    private List<String> targetIdsToGroup;
    @ApiModelProperty(value = "转发目标 (转发至用户) id集合",dataType = "List<String>")
    private List<String> targetIdsToUser;

    @ApiModelProperty("是否开启在指定时间段转发")
    private Boolean timeEnable;

    @ApiModelProperty("机器人指定时间段转发开启时间")
    private LocalTime startTime;

    @ApiModelProperty("机器人指定时间段转发关闭时间")
    private LocalTime endTime;

    /** 转发类型设置： 文字、图片、小视频、语音、文章、名片、动画表情 */
    @ApiModelProperty
    private Boolean text;
    @ApiModelProperty
    private Boolean image;
    @ApiModelProperty
    private Boolean shortVideo;
    @ApiModelProperty
    private Boolean audio;
    @ApiModelProperty
    private Boolean article;
    @ApiModelProperty
    private Boolean card;
    @ApiModelProperty
    private Boolean emoji;
}
