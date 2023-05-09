package com.hnzz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * @PackageName:com.hnzz.entity
 * @ClassName:Navigation
 * @Author 冼大丰
 * @Date 2023/5/9 16:37
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document("navigation")
public class Navigation implements Serializable {
    @Id
    private String id;
    @Field
    private String routing;
    @Field
    private String name;
    @Field
    private String img;
    @Field
    private String imgBright;
}
