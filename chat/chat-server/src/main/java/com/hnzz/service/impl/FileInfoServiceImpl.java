package com.hnzz.service.impl;

import com.hnzz.entity.FileInfo;
import com.hnzz.service.FileInfoService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HB on 2023/2/23
 * TODO 文件管理业务实现类
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public FileInfo saveFileInfo(FileInfo fileInfo){
        return mongoTemplate.insert(fileInfo);
    }

    @Override
    public List<FileInfo> getFileInfoByUserId(String userId){
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query,FileInfo.class);
    }

    @Override
    public List<FileInfo> getFileInfoByGroupId(String groupId){
        Query query = new Query(Criteria.where("groupId").is(groupId));
        return mongoTemplate.find(query,FileInfo.class);
    }

}
