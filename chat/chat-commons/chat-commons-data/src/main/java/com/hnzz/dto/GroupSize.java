package com.hnzz.dto;

import com.hnzz.entity.FileInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @PackageName:com.hnzz.dto
 * @ClassName:GroupSize
 * @Author 冼大丰
 * @Date 2023/1/17 11:55
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("群的基本资料")
public class GroupSize {

    @ApiModelProperty("群Id")
    private String groupId;

    @ApiModelProperty("群名称")
    private String GroupName;

    @ApiModelProperty("群成员人数")
    private Long GroupUserSize;

    @ApiModelProperty("群头像")
    private String groupHead;

}
