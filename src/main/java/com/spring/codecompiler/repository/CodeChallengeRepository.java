package com.spring.codecompiler.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.spring.codecompiler.api.domain.CodeChallenge;

public interface CodeChallengeRepository extends MongoRepository<CodeChallenge, String>{
	

}
