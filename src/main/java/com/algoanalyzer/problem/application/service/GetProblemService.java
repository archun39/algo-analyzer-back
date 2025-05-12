package com.algoanalyzer.problem.application.service;

import com.algoanalyzer.problem.application.port.in.GetProblemUseCase;
import com.algoanalyzer.problem.application.port.out.ProblemClient;
import com.algoanalyzer.problem.domain.model.Problem;
import com.algoanalyzer.problem.domain.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 문제 조회 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class GetProblemService implements GetProblemUseCase {
    private final ProblemRepository problemRepository;
    private final ProblemClient problemClient;

    @Override
    public Problem getProblem(Long problemId) {
        return problemRepository.findByProblemId(problemId)
            .orElseGet(() -> {
                // DB에 없으면 외부 API 호출
                Problem fetched = problemClient.fetchProblem(problemId);
                // 조회된 도메인 객체를 DB에 저장
                problemRepository.save(fetched);
                return fetched;
            });
    }
} 