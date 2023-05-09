package com.hnzz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author HB on 2023/4/25
 * TODO 访问记录
 */
@Data
public class AccessRecords {
    @Id
    private String id;
    private String userId;
    private String ip;
    private String path;
    private String accessMethod;
    private String accessRole;
    private Integer statusCode;
    private String errorMessage;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date accessTime;
}
