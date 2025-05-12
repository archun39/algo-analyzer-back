package com.algoanalyzer.problem.application.port.in;

import com.algoanalyzer.problem.domain.model.Problem;

/**
 * 문제 조회 유스케이스를 위한 In Port 인터페이스입니다.
 */
public interface GetProblemUseCase {
    /**
     * 주어진 문제 ID에 대한 Problem 도메인 객체를 반환합니다.
     * @param problemId 조회할 문제 ID
     * @return Problem 도메인 객체
     */
    Problem getProblem(Long problemId);
} 