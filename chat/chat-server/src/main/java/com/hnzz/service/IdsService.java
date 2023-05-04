package com.hnzz.service;

import com.hnzz.form.IdsPattern;

import java.util.List;

/**
 * @author HB on 2023/2/9
 * TODO 号池抽号业务层
 */
public interface IdsService {
    /**
     * 从号池中随机抽选一个未绑定的号码
     * @return id 返回一个靓号
     */
    Integer getIdWithRandom(IdsPattern idsPattern);

    void setIdsUsed(Integer foxCode);
}
