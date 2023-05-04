package com.hnzz.form.groupform;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @PackageName:com.hnzz.form.groupform
 * @ClassName:Silent
 * @Author 冼大丰
 * @Date 2023/2/15 12:29
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Silent implements Serializable {
    /**
     * 群聊id
     */
    @NotBlank(message = "群聊id不能为空")
    @ApiModelProperty("群聊id")
    private String groupId;
    /**
     * 需要禁言的群成员Id
     */
    @NotBlank(message = "群聊id不能为空")
    @ApiModelProperty("群聊id")
    private List<String> usersId;

}
