package com.hnzz.form.userform;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author HB
 * @Classname UserIdList
 * @Date 2023/1/10 18:01
 * @Description TODO
 */
@Data
@Accessors(chain = true)
public class UserIdList {

    private List<String> usersid;
}
