package com.hnzz.controller;


import cn.hutool.json.ObjectMapper;
import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.hnzz.common.ResultUtil;
import com.hnzz.common.SeaweedFSUtil;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.entity.FileInfo;
import com.hnzz.service.FileInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author HB on 2023/2/23
 * TODO 文件上传与下载
 */
@Slf4j
@RestController
@Api(tags = "文件上传接口")
@RequestMapping("api/v1/file")
public class FileController {

    @Value("${app.uploadUrl}")
    private String uploadUrl;

    @Resource
    private FileInfoService fileInfoService;

    @GetMapping("down")
    @ApiOperation("文件下载")
    public ResponseEntity<byte[]> get(@RequestParam("url") String url ) throws IOException {
        System.out.println("===========开始执行=============");
        return SeaweedFSUtil.downloadFile(url);
    }

    @PostMapping("upload")
    @ApiOperation("文件上传")
    public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file,
                                         @RequestParam(value = "groupId",required = false)String groupId,
                                         @RequestHeader("userId")String userId) {
        if (file.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("上传文件不能为空!");
        }
        /*if (!file.getContentType().equals("image/jpeg")||file.getContentType().equals("image/jpg")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("只能上传jpeg或jpg的图片格式!");
        }*/
        /*if (file.getSize()>10*1024*1024){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("文件大小不能超过10MB!");
        }*/
        ResponseEntity<FileInfo> response = SeaweedFSUtil.uploadFile(uploadUrl, file);
        FileInfo fileInfo = response.getBody();
        if (fileInfo==null){
            throw new AppException("上传失败");
        }
        Optional.ofNullable(groupId).ifPresent(fileInfo::setGroupId);
        fileInfo.setUserId(userId);
        fileInfo.setUploadedAt(new Date());
        fileInfo.setFileUrl(fileInfo.getFileUrl());
        log.info("文件上传返回信息: {} ",fileInfo);
        FileInfo fileInfo1 = fileInfoService.saveFileInfo(fileInfo);
        return ResponseEntity.ok(fileInfo1);
    }
    @PostMapping("uploadAll")
    @ApiOperation("多文件上传")
    public ResponseEntity<Object> uploadAll(@RequestParam("file") MultipartFile[] files,
                                            @RequestParam(value = "groupId", required = false) String groupId,
                                            @RequestHeader("userId") String userId) {

        List<Object> list = new ArrayList<>();
        for (MultipartFile file : files) {
            /*if (!file.getContentType().equals("image/jpeg")||file.getContentType().equals("image/jpg")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("只能上传jpeg或jpg的图片格式!");
            }
            if (file.getSize()>5*1024*1024){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("文件大小不能超过5MB!");
            }*/
            ResponseEntity<FileInfo> response = SeaweedFSUtil.uploadFile(uploadUrl, file);
            FileInfo fileInfo = response.getBody();
            if (fileInfo == null) {
                throw new AppException("上传失败");
            }
            Optional.ofNullable(groupId).ifPresent(fileInfo::setGroupId);
            fileInfo.setUserId(userId);
            fileInfo.setUploadedAt(new Date());
            fileInfo.setFileUrl(fileInfo.getFileUrl());
            log.info("文件上传返回信息: {} ", fileInfo);
            FileInfo fileInfo1 = fileInfoService.saveFileInfo(fileInfo);
            list.add(fileInfo1);
        }
        return ResponseEntity.ok(list);
    }

}
