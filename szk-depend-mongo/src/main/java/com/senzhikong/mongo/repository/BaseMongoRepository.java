package com.senzhikong.mongo.repository;

import com.senzhikong.mongo.entity.BaseMongoPO;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author shu.zhou
 */
public interface BaseMongoRepository extends MongoRepository<BaseMongoPO, String> {
}
