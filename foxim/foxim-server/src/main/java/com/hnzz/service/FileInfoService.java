package com.hnzz.service;

import com.hnzz.entity.FileInfo;

import java.util.List;

/**
 * @author HB on 2023/2/23
 * TODO 文件业务抽象类
 */
public interface FileInfoService {

    FileInfo saveFileInfo(FileInfo fileInfo);

    List<FileInfo> getFileInfoByUserId(String userId);

    List<FileInfo> getFileInfoByGroupId(String groupId);
}
