package com.hnzz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @PackageName:com.hnzz.dto
 * @ClassName:ContactsSearch
 * @Author zj
 * @Date 2023/3/11 11:39
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactsSearch {
    @Field
    private String id;
    @Field
    private String username;
    @Field
    private String avatarUrl;
}
