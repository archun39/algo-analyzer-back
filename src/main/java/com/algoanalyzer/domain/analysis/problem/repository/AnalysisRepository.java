package com.algoanalyzer.domain.analysis.problem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.algoanalyzer.domain.analysis.problem.model.AnalysisLevel1Document;

public interface AnalysisRepository extends MongoRepository<AnalysisLevel1Document, Long> {
    AnalysisLevel1Document findByProblemId(Long problemId);
}