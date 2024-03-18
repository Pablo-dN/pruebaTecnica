package org.example.avoris.repository;

import org.example.avoris.model.MongoDBSearchRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends MongoRepository<MongoDBSearchRequest, String> {

  @Query("{'searchId' : ?0}.limit(1)")
  MongoDBSearchRequest findBySearchId(String searchId);
}
