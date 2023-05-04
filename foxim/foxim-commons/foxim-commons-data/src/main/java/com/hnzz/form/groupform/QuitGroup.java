package com.hnzz.form.groupform;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @PackageName:com.zzkj.vo
 * @ClassName:QuitGroup
 * @Author 冼大丰
 * @Date 2023/1/5 15:58
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class QuitGroup implements Serializable {
    @NotBlank(message = "群聊id不能为空")
    @ApiModelProperty("群聊id")
    private String gid;

    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty("退出群聊的用户id")
    private String userId;
}
