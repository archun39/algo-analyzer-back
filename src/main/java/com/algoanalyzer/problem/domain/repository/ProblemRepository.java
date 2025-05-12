package com.algoanalyzer.domain.problem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.algoanalyzer.domain.problem.model.ProblemDocument;

public interface ProblemRepository extends MongoRepository<ProblemDocument, Long> {
    ProblemDocument findByProblemId(Long problemId);
} 