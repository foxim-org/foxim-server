package com.hnzz.ability.gateway.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author HB on 2023/4/19
 * TODO ip工具类
 */
@Slf4j
public class IpUtil {

    private static final String[] IP_ADDRESS_TO = {"X-Real-IP","X-Forwarded-For","x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP","HTTP_CLIENT_IP","HTTP_X_FORWARDED_FOR"};
    private static final String UNKNOWN = "unknown";
    private static final String LOCAL_HOST_1 = "0:0:0:0:0:0:0:1";
    private static final String LOCAL_HOST_2 = "127.0.0.1";
    private static final String IP_SPLIT = ",";
    private static final Integer IP_LENGTH = 15;


    public static String getIpAddr(ServerHttpRequest request) {
        String ipAddress = null;
        try {
            for (String ipAddressTo : IP_ADDRESS_TO) {
                ipAddress = request.getHeaders().getFirst(ipAddressTo);
                if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                    continue;
                } else {
                    log.info("根据{}获取到ip为:{}",ipAddressTo, ipAddress);
                    break;
                }
            }

            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddress().getAddress().getHostAddress();
                log.info("根据请求获取到ip为:{}", ipAddress);
                if (ipAddress.equals(LOCAL_HOST_1) || ipAddress.equals(LOCAL_HOST_2)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                        ipAddress = inet.getHostAddress();
                        log.info("根据网卡获取到ip为:{}", ipAddress);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }

            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            // "***.***.***.***".length()
            if (ipAddress != null && ipAddress.length() > IP_LENGTH) {
                // = 15
                if (ipAddress.indexOf(IP_SPLIT) > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(IP_SPLIT));
                    log.info("根据{}获取到ip为:{}", IP_LENGTH, ipAddress);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            ipAddress = "";
        }
        return ipAddress;
    }
}
