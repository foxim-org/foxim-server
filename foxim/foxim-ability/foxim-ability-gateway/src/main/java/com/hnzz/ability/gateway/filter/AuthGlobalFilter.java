package com.hnzz.ability.gateway.filter;

import com.hnzz.ability.gateway.application.AuthIgnoreConfig;
import com.hnzz.ability.gateway.utils.IpUtil;
import com.hnzz.ability.gateway.utils.ResponseUtils;
import com.hnzz.commons.base.jwt.JWTHelper;
import com.hnzz.commons.base.result.ResultCodes;
import com.hnzz.entity.AccessRecords;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author xdf
 * @version 1.0
 */
@Component
@Slf4j
@RefreshScope
public class AuthGlobalFilter implements GlobalFilter , Ordered {
    /**
     * 注入资源配置类
     */
    @Resource
    private AuthIgnoreConfig authIgnoreConfig;

    @Resource
    private JWTHelper jwtHelper;

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * Spring提供的Url匹配工具
     */
    private final AntPathMatcher antPathMatcher=new AntPathMatcher();

    @SneakyThrows
    @Override
    public Mono filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取当前的请求的路由
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        log.debug("[auth global filter] 全局认证拦截器 - {}",path);

        // 获取请求头
        HttpHeaders headers = request.getHeaders();
        // 构造新的请求头
        HttpHeaders newHttpHeaders=new HttpHeaders();
        //先将旧的请求头里的数据复制到新的请求头里
        newHttpHeaders.putAll(headers);
        // 获取到请求ip
        String ipAddr = IpUtil.getIpAddr(request);
        newHttpHeaders.set("ipAddr",ipAddr);
        // 获取到请求的id 并存入请求头
        ServerHttpRequest newRequest=request.mutate().uri(request.getURI()).build();

        AccessRecords accessRecords = new AccessRecords();
        accessRecords.setStatusCode(HttpStatus.OK.value());
        accessRecords.setAccessTime(new Date());
        accessRecords.setIp(ipAddr);
        accessRecords.setPath(path);
        accessRecords.setAccessMethod(request.getMethodValue());

        //判断当前的请求是否必须认证通过后才能放行
        //获取所有要放行的资源【无需登录的资源列表】
        List<String> paths = authIgnoreConfig.getPaths();
        List<String> admins = authIgnoreConfig.getAdmins();
        //循环判断当前请求的url是否在无需登录的资源列表中
        for (String ignorePath : paths){
            if (antPathMatcher.match(ignorePath,path)){
                log.debug("[auth global filter] 当前请求无需认证 - {}",path);
                AccessRecords insert = mongoTemplate.insert(accessRecords);
                newHttpHeaders.set("accessId",insert.getId());
                //构建一个新的请求
                newRequest=new ServerHttpRequestDecorator(newRequest){
                    //将新的请求构建成一个装饰者对象
                    @Override
                    public HttpHeaders getHeaders() {
                        return newHttpHeaders;
                    }
                };

                return chain.filter(exchange.mutate().request(newRequest).build());
            }
        }

        //--------------------------------------------------------------


        //获取请求头中的登录令牌
        String jwtToken = headers.getFirst("Authorization");
        //如果令牌为空，说明当前客户端没有登录
        if (jwtToken==null){
            //告知用户必须登录才能访问
            accessRecords.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            accessRecords.setErrorMessage(ResultCodes.AUTH_ERROR.getMsg());
            return ResponseUtils.result(exchange, ResultCodes.AUTH_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //校验jwt令牌是否被篡改过和令牌是否已过期
        String userJson = "";
        try{
            userJson = jwtHelper.parseJWT(jwtToken).get("id",String.class);
        }catch (Exception e){
            accessRecords.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            accessRecords.setErrorMessage(ResultCodes.AUTH_ERROR.getMsg());
            return ResponseUtils.result(exchange,ResultCodes.AUTH_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (Objects.equals(userJson, "")){
            //告知用户必须登录才能访问
            accessRecords.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            accessRecords.setErrorMessage(ResultCodes.AUTH_ERROR.getMsg());
            return ResponseUtils.result(exchange,ResultCodes.AUTH_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //令牌有效
        log.debug("[auth global filter] 令牌校验通过 - {} - json：{}", path, userJson);

        //将用户的json 加入到请求头中，继续往后传递
        //--------------------------------------------------------------

        //通过URLEncoder编码的方式 解决请求头中中文乱码的问题
        String encode = URLEncoder.encode(userJson, "UTF-8");
        for (String ignorePath : admins) {
            if (antPathMatcher.match(ignorePath, path)) {
                newHttpHeaders.set("adminId",encode);
                accessRecords.setUserId(encode);
                accessRecords.setAccessRole("admin");
            }else {
                //将解析出来的用户json，放入到请求头中
                newHttpHeaders.set("userId",encode);
                accessRecords.setUserId(encode);
                accessRecords.setAccessRole("user");
            }
            
        }
        //再将登录令牌的请求头移除掉
        newHttpHeaders.remove("Authorization");
        AccessRecords insert = mongoTemplate.insert(accessRecords);
        newHttpHeaders.set("accessId",insert.getId());
        //构建一个新的请求
        newRequest=new ServerHttpRequestDecorator(newRequest){
            //将新的请求构建成一个装饰者对象
            @Override
            public HttpHeaders getHeaders() {
                return newHttpHeaders;
            }
        };

        return chain.filter(exchange.mutate().request(newRequest).build());
    }

    @Override
    public int getOrder() {
        return -100;
    }

}
