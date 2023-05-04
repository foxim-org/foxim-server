package com.hnzz.dao;

import com.hnzz.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @PackageName:com.zzkj.dao
 * @ClassName:UserDao
 * @Author 周俊
 * @Date 2023/1/4 10:55
 * @Version 1.0
 **/
public interface UserDao {


    User friendAllowviewpro(String id);

    User friendSimple(String id);

    User selectBySearch(String search);

    User findUserById(String userId);

    User setUserInfo(User user);

    /**
     * 分页查询用户
     * @param pageable 传入的分页内容
     * @param search 查询条件
     * @return 返回封装后的分页数据
     */
    Page<User> findAll(Pageable pageable, String search);

    User setUserAvatarUrl(String userId, String fileUrl);

    boolean userLogout(String id);

    Page<User> userAll(Pageable pageable);

    Boolean formUserByMobile(String mobile);
}
