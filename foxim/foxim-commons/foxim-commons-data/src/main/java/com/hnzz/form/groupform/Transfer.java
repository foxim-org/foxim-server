package com.hnzz.form.groupform;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @PackageName:com.hnzz.form.groupform
 * @ClassName:Transfer
 * @Author 冼大丰
 * @Date 2023/2/14 11:48
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor

public class Transfer implements Serializable {
    /**
     * 群聊id
     */
    @NotBlank(message = "群聊id不能为空")
    @ApiModelProperty("群聊id")
    private String groupId;
    /**
     * 加入用户的id
     */
    @NotNull(message = "拉取用户不能为空")
    @ApiModelProperty("转让为群主的用户Id")
    private String userId;
}

