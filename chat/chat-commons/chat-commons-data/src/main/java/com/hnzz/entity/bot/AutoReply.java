package com.hnzz.entity.bot;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

/**
 * @author HB on 2023/2/27
 * TODO 自助回答
 */
@Data
@ApiModel("自动问答内容")
public class AutoReply {
    @ApiModelProperty("问答Id")
    private String replyId;

    @ApiModelProperty("问题")
    @NotBlank(message = "问题不能为空")
    @Length(max = 20,message = "问题长度不能超过20字")
    private String question;

    @ApiModelProperty("答案")
    @NotBlank(message = "回答不能为空")
    @Length(max = 500,message = "回答长度不能超过500字")
    private String answer;

    @ApiModelProperty("关键词")
    @NotBlank(message = "触发关键词不能为空")
    private String keyWord;
}
