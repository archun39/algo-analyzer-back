package com.algoanalyzer.problem.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

// 문제 정보를 저장하는 모델
public interface ProblemDocumentRepository extends MongoRepository<ProblemDocument, Long> {
    Optional<ProblemDocument> findByProblemId(Long problemId);
}
