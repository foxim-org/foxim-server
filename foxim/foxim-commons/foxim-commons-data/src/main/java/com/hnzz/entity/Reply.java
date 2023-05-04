package com.hnzz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.hnzz.entity
 * @ClassName:Replies
 * @Author 冼大丰
 * @Date 2023/2/9 16:13
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Reply implements Serializable {
    /**
     * 朋友圈回复点赞转发的id
     */
    @Id
    private String id;
    /**
     * 原作者文本
     */
    @Field
    private String authorText;
    /**
     * 回复的内容
     */
    @Field
    private String replyText;

    /**
     * 图片
     */
    @Field
    private List<String> imageUrls;
    /**
     * 当前用户Id
     */
    @Field
    private String userId;
    /**
     * 昵称
     */
    @Field
    private String username;
    /**
     * 头像
     */
    @Field
    private String avatarUrl;
    /**
     * 转发时的转发内容
     */
    @Field
    private String comment;
    /**
     * 原作者id
     */
    @Field
    private String authorId;
    /**
     * 原作者昵称
     */
    @Field
    private String authorUsername;
    /**
     * 原作者头像
     */
    @Field
    private String authorAvatarUrl;
    /**
     * 创建时间
     */
    @Field
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date createdAt;
}
