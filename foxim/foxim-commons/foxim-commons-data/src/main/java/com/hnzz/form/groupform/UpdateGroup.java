package com.hnzz.form.groupform;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.hnzz.vo
 * @ClassName:updateGroup
 * @Author 冼大丰
 * @Date 2023/1/9 14:18
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("更新群组表单")
public class UpdateGroup {

    /**
     * 群id号
     */
    @NotBlank(message = "群id不能为空！")
    @ApiModelProperty("群id")
    private String groupId;
    /**
     * 群名称
     */
    @ApiModelProperty("群名")
    private String name;
    /**
     * 群头像
     */
    @ApiModelProperty("群头像")
    private String groupHead;
    /**
     * 群介绍
     */
    @ApiModelProperty("群介绍")
    private String desc;
    /**
     * 群公告
     */
    @ApiModelProperty("群公告")
    private String notice;
    /**
     * 群的标签
     */
    @ApiModelProperty("群标签")
    private List<String> tags;
    /**
     * 群成员的昵称
     */
    @ApiModelProperty("群成员的昵称")
    private String displayName;
}
