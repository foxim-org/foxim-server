package com.hnzz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @PackageName:com.hnzz.dto
 * @ClassName:InformSearch
 * @Author zj
 * @Date 2023/3/11 17:12
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("首页搜索框实体")
public class InformSearch {
    @Field
    @ApiModelProperty("用户id")
    private String userId;
    @Field
    @ApiModelProperty("群id")
    private String groupId;
    @Field
    @ApiModelProperty("好友名称")
    private String userName;
    @Field
    @ApiModelProperty("群名称")
    private String groupName;
    @Field
    @ApiModelProperty("群头像")
    private String groupHead;
    @Field
    @ApiModelProperty("用户头像")
    private String avatarUrl;
}
