package com.hnzz.dao;

import com.hnzz.entity.Sms;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SmsDao extends MongoRepository<Sms, String> {

    Optional<Sms> findByMobileAndCode(String mobile, String code);

    void deleteByExpireTimeBefore(LocalDateTime now);
}
