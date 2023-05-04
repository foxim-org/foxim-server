package com.hnzz.form.bots;

import com.hnzz.entity.bot.AutoReply;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author HB on 2023/2/22
 * TODO 机器人自动问答设置
 */
@Data
@ApiModel("机器人自动问答设置")
public class BotsAutoReplySetting {
    @ApiModelProperty("机器人id (必填)")
    @NotBlank
    private String id;

    @ApiModelProperty("问答内容(必填)")
    @NotNull(message = "设置的自助回答不能为空")
    private List<AutoReply> autoReplies;

    @ApiModelProperty("执行操作(必填) [add(新增问题) | del(删除) | put(修改)]")
    @NotBlank(message = "请带入设置类型")
    private String type;
}
