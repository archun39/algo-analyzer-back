package com.algoanalyzer.analysis.domain.repository;

import java.util.Optional;

import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;

/**
 * 문제 분석 정보를 저장하고 조회하는 도메인 저장소 포트입니다.
 */
public interface ProblemAnalysisRepository {
    /**
     * 주어진 문제 ID에 대한 분석 결과를 조회합니다.
     * @param problemId 조회할 문제 ID
     * @return 분석 결과 도메인 객체(Optional)
     */
    Optional<ProblemAnalysis> findByProblemId(Long problemId);

    /**
     * 분석 결과 도메인 객체를 저장합니다.
     * @param analysis 저장할 분석 결과 객체
     */
    void save(ProblemAnalysis analysis);
}
