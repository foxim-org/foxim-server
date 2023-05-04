package com.hnzz.form.userform;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackageName:com.hnzz.form.userform
 * @ClassName:UserSearch
 * @Author 冼大丰
 * @Date 2023/1/17 10:54
 * @Version 1.0
 **/
@Data
@ApiModel("用户搜索信息表")
public class UserSearch {

    @ApiModelProperty("搜索信息，手机号或者其他")
    private String searchinfo;
}
