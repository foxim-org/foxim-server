package com.hnzz.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @PackageName:com.hnzz.form
 * @ClassName:Navigationfrom
 * @Author 冼大丰
 * @Date 2023/5/11 17:10
 * @Version 1.0
 **/
@Data
@ApiModel("底部导航栏Id")
public class NavigationFrom implements Serializable {
    private List<String> ids;
}
