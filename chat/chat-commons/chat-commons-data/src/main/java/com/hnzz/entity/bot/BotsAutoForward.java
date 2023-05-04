package com.hnzz.entity.bot;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalTime;
import java.util.List;

/**
 * @author HB on 2023/2/22
 * TODO 机器人自动转发设置
 */
@Data
public class BotsAutoForward {

    private Boolean forward;

    private Integer deplayed;

    private List<String> sourceIds;
    /**
     * 转发目标群|用户 id集合
     */
    private List<String> targetIdsToGroup;

    private List<String> targetIdsToUser;

    private Boolean timeEnable;

    private LocalTime startTime;

    private LocalTime endTime;

    /** 转发类型设置： 文字、图片、小视频、语音、文章、名片、动画表情 */
    private Boolean text;
    private Boolean image;
    private Boolean shortVideo;
    private Boolean audio;
    private Boolean article;
    private Boolean card;
    private Boolean emoji;

    public BotsAutoForward() {
        this.forward = Boolean.FALSE;
        this.deplayed = 1;
        this.sourceIds = null;
        this.targetIdsToGroup = null;
        this.targetIdsToUser = null;
        this.timeEnable = Boolean.FALSE;
        this.startTime = LocalTime.MIN;
        this.endTime = LocalTime.MIN;
        this.text = Boolean.FALSE;
        this.image = Boolean.FALSE;
        this.shortVideo = Boolean.FALSE;
        this.audio = Boolean.FALSE;
        this.article = Boolean.FALSE;
        this.card = Boolean.FALSE;
        this.emoji = Boolean.FALSE;
    }
}
