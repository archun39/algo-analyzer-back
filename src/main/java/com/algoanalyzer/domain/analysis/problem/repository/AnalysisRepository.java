package com.algoanalyzer.domain.analysis.problem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.algoanalyzer.domain.analysis.problem.dto.response.ProblemAnalysisResponseDto;

public interface AnalysisRepository extends MongoRepository<ProblemAnalysisResponseDto, Long> {
    ProblemAnalysisResponseDto findByProblemId(Long problemId);
}
