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
 * @PackageName:com.zzkj.vo
 * @ClassName:JoinGroup
 * @Author 冼大丰
 * @Date 2023/1/5 11:09
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class JoinGroup implements Serializable {
    /**
     * 群聊id
     */
    @NotBlank(message = "群聊id不能为空")
    @ApiModelProperty("群聊id")
   private String gid;
    /**
     * 加入用户的id
     */
    @NotNull(message = "拉取用户不能为空")
    @ApiModelProperty("用户的id")
   private List<String> uids;
}
