package com.hnzz.form.groupform;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @PackageName:com.hnzz.form.groupform
 * @ClassName:GroupId
 * @Author 冼大丰
 * @Date 2023/1/17 15:27
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class GroupId implements Serializable {
    /**
     * 群聊id
     */
    @NotBlank(message = "群聊id不能为空")
    @ApiModelProperty("群聊id")
    private String groupId;

    @NotNull
    @ApiModelProperty("群聊免打扰选项 True | False")
    private Boolean disturb;

}
