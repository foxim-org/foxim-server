package com.hnzz.form;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author HB on 2023/2/16
 * TODO 选号规则
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
@ApiModel("选号匹配规则")
public class IdsPattern implements Serializable {
    public boolean AAA;
    public boolean AAAA;
    public boolean AABB;
    public boolean AAABB;
    public boolean ABAB;
    public boolean AABBCC;
    public boolean AAABBB;
    public boolean ABABAB;
    public boolean ABCABC;
    public boolean ABC;
    public boolean ABCD;
}
