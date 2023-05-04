package com.hnzz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @PackageName:com.hnzz.dto
 * @ClassName:GroupData
 * @Author 冼大丰
 * @Date 2023/1/13 17:15
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("群的基本资料")
public class GroupData {

    @ApiModelProperty("群Id")
    private String id;

    @ApiModelProperty("群名称")
    private String name;

    @ApiModelProperty("头像")
    private String groupHead;
}
