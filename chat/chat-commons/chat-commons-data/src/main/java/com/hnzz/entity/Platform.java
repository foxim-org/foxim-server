package com.hnzz.entity;

import com.hnzz.commons.base.enums.AdminUserStatus;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author HB on 2023/3/7
 * TODO 平台实体类
 */
@Data
@Document("platform")
@Accessors(chain = true)
public class Platform {
    @Id
    private String id;
    private String platformName;
    private String status;
    private String logoUrl;
    private String description;
    private Integer onlineNum;
    private Integer totalNum;
    private String superAdminId;
    private LocalDateTime createTime;

    public Platform createPlatform(String platformName,String superAdminId){
        return new Platform()
        .setPlatformName(platformName)
        .setStatus(AdminUserStatus.PENDING_REVIEW.getStatus())
        .setOnlineNum(0)
        .setTotalNum(0)
        .setSuperAdminId(superAdminId)
        .setCreateTime(LocalDateTime.now());
    }
}
