package com.algoanalyzer.analysis.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.algoanalyzer.analysis.application.port.in.AnalyzeProblemUseCase;
import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;
import com.algoanalyzer.analysis.presentation.dto.response.ProblemAnalysisResponseDto;
import com.algoanalyzer.analysis.application.mapper.ProblemAnalysisMapper;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemAnalysisController {
    private final AnalyzeProblemUseCase analyzeProblemUseCase;
    private final ProblemAnalysisMapper problemAnalysisMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProblemAnalysisController.class);

    @PostMapping("/{problemId}/analysis")
    public ResponseEntity<ProblemAnalysisResponseDto> analyzeProblem(
        @PathVariable Long problemId) {
        
        logger.info("문제 분석 시작: problemId={}", problemId);
        long startTime = System.currentTimeMillis();
        ProblemAnalysis analysis = analyzeProblemUseCase.analyzeProblem(problemId);
        long fetchTimeMs = System.currentTimeMillis() - startTime;
        
        logger.info("문제 분석 완료: problemId={}, 분석 시간={}ms", problemId, fetchTimeMs);
        ProblemAnalysisResponseDto responseDto = problemAnalysisMapper.toResponseDto(analysis);
        
        return ResponseEntity.ok(responseDto);
    }
} 