package com.ian.service.impl;

import cn.hutool.db.sql.Condition;
import com.ian.entity.News;
import com.ian.entity.Record;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ianhau
 */
@Service
public class NewsService {
    @Resource
    MongoTemplate mongoTemplate;

    public PageImpl<News> page(Pageable pageable) {
        Query query = new Query();
        long total = mongoTemplate.count(query, News.COLLECTION_NAME);
        query.with(pageable);
        return new PageImpl<>(mongoTemplate.find(query, News.class, News.COLLECTION_NAME), pageable, total);
    }
}
