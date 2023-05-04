package com.hnzz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @PackageName:com.hnzz.dto
 * @ClassName:ContactsRequests
 * @Author zj
 * @Date 2023/3/15 14:06
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactsRequests {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 名称
     */
    private String username;
    /**
     * 狐狸号
     */
    private Integer foxCode;
    /**
     * 头像
     */
    private String avatarUrl;
    /**
     * 状态
     */
    private String status;
}
