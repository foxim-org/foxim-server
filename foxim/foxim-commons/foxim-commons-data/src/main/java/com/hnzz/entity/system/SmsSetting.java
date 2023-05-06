package com.hnzz.entity.system;

import lombok.Data;

/**
 * @author HB on 2023/5/6
 * TODO 短信模板配置
 */
@Data
public class SmsSetting {
    private String username;
    private String password;
    private String url;
    private String content;
}
