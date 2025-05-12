package com.algoanalyzer.analysis.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
public interface ProblemAnalysisDocumentRepository extends MongoRepository<ProblemAnalysisDocument, Long> {
    Optional<ProblemAnalysisDocument> findByProblemId(Long problemId);
}
