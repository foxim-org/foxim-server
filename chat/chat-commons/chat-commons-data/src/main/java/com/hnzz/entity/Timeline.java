package com.hnzz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.hnzz.entity
 * @ClassName:Timeline
 * @Author 冼大丰
 * @Date 2023/1/28 15:14
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Timeline implements Serializable {
    @Id
    private String id;
    /**
     * 用户Id
     */
    @Field
    private String userId;
    /**
     * 朋友圈内容
     */
    @Field
    private String text;
    /**
     * 发送图片
     */
    @Field
    private List<String> imageUrls;
    /**
     * 发送图片
     */
    @Field
    private String videoUrls;
    /**
     * 转发，回复，点赞
     */
    @Field
    private List<Reply> replies;
    /**
     * 允许谁看
     */
    @Field
    private List<String> whitelist;

    @Field
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt; // 加入时间，无法更改
}
