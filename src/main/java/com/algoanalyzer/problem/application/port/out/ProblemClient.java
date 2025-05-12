package com.algoanalyzer.problem.application.port.out;

import com.algoanalyzer.problem.domain.model.Problem;

/**
 * 외부 API로부터 Problem 정보를 가져오는 Out Port 인터페이스입니다.
 */
public interface ProblemClient {
    /**
     * 외부 API 호출을 통해 문제 정보를 조회합니다.
     * @param problemId 조회할 문제 ID
     * @return Problem 도메인 객체
     */
    Problem fetchProblem(Long problemId);
} 