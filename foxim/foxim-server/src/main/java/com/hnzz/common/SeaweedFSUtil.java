package com.hnzz.common;

import com.hnzz.commons.base.exception.AppException;
import com.hnzz.entity.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author HB on 2023/2/23
 * TODO seaweedfs文件上传服务
 */
@Slf4j
public class SeaweedFSUtil {

    private static final String SUBMIT = "/submit";
    private static final String DIR = "/dir";
    private static final String ASSIGN = "/assign";
    private static final String TTL = "ttl";
    private static final String LOOKUP = "/lookup";


    /**
     * 文件上传
     * @param url
     * @param file
     * @return ResponseEntity
     */
    public static ResponseEntity<FileInfo> uploadFile(String url, MultipartFile file){
        try {
            byte[] fileBytes = file.getBytes();
            ByteArrayResource resource = new ByteArrayResource(fileBytes) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
            parts.add("file",resource);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parts, httpHeaders);
            RestTemplate client = new RestTemplate();
            return client.postForEntity(url, httpEntity, FileInfo.class);
        } catch (IOException e) {
            log.warn("发生IO处理异常 ",e);
            throw new AppException(e);
        }
    }

    public static ResponseEntity<byte[]> downloadFile(String url){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return client.exchange(url, HttpMethod.GET, entity, byte[].class);
    }

    public static String getUrl(String host,Integer port){
        StringBuilder sb = new StringBuilder();
        sb.append("http://").append(host).append(":").append(port);
        return sb.toString();
    }

    public static String getUrl(String host,Integer port, String... setting){
        String url = getUrl(host, port);
        StringBuilder sb = new StringBuilder(url);
        for (String s:setting) {
            sb.append(s);
        }
        return sb.toString();
    }


}
