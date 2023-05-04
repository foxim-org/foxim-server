package com.hnzz.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author HB on 2023/4/11
 * TODO
 */
@Data
@ApiModel("转发表单对象")
public class ForwardForm {
    @ApiModelProperty("转发对象id")
    private List<String> id;
    @ApiModelProperty("转发群id")
    private List<String> groupId;
}
