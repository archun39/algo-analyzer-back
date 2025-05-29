package com.algoanalyzer.problem.presentation.controller;

import com.algoanalyzer.problem.application.port.in.GetProblemUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.algoanalyzer.problem.domain.model.Problem;
import com.algoanalyzer.problem.presentation.dto.response.ProblemResponseDto;
import com.algoanalyzer.problem.application.mapper.ProblemMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problems")
public class ProblemController {
    private final GetProblemUseCase getProblemUseCase;
    private final ProblemMapper problemMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProblemController.class);

    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemResponseDto> getProblem(@PathVariable Long problemId) {
        logger.info("문제 조회 시작: problemId={}", problemId);
        long startTime = System.currentTimeMillis();
        Problem problem = getProblemUseCase.getProblem(problemId);
        long fetchTimeMs = System.currentTimeMillis() - startTime;

        logger.info("문제 조회 완료: problemId={}, 조회 시간={}ms", problemId, fetchTimeMs);
        ProblemResponseDto responseDto = problemMapper.toResponseDto(problem);
        
        return ResponseEntity.ok(responseDto);
    }
} 