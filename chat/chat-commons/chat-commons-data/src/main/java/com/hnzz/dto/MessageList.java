package com.hnzz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author HB on 2023/3/8
 * TODO
 */
@Data
@ApiModel("消息列表分页展示数据")
public class MessageList {
    @ApiModelProperty("分页数据 , 内部count不要用 , 真实数据在messageDTOS中")
    private Page page;
    @ApiModelProperty("真实消息列表数据")
    private List<MessageDTO> messageDTOS;
}
