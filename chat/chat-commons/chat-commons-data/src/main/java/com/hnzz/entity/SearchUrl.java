package com.hnzz.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author HB on 2023/4/27
 * TODO 配置类
 */
@Data
public class SearchUrl {
    @Id
    private String id;
    private String name;
    private String url;
}
