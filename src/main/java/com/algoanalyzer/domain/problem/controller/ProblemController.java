package com.algoanalyzer.domain.problem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algoanalyzer.domain.analysis.problem.dto.request.ProblemAnalysisRequestDto;
import com.algoanalyzer.domain.analysis.problem.dto.response.ProblemAnalysisResponseDto;
import com.algoanalyzer.domain.analysis.problem.service.ProblemAnalysisService;
import com.algoanalyzer.domain.problem.dto.response.ProblemResponseDto;
import com.algoanalyzer.domain.problem.dto.response.ProblemWithAnalysisResponseDto;
import com.algoanalyzer.domain.problem.service.ProblemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
@Slf4j
public class ProblemController {
    
    private final ProblemService problemService;
    private final ProblemAnalysisService problemAnalysisService;

    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemWithAnalysisResponseDto> getProblem(@PathVariable Long problemId) {
        
        // 문제 조회
        ProblemResponseDto response = problemService.getProblem(problemId);

        log.info("문제 조회 완료: {}", response);

        ProblemAnalysisRequestDto analysisRequest = buildAnalysisRequest(response);
        // 문제 분석 요청
        ProblemAnalysisResponseDto analysisResult = problemAnalysisService.analyzeProblem(analysisRequest);

        log.info("문제 분석 완료: {}", analysisResult);

        ProblemWithAnalysisResponseDto combinedResponse = ProblemWithAnalysisResponseDto.builder()
                .problemResponse(response)
                .analysisResponse(analysisResult)
                .build();

        log.info("문제 조회 및 분석 완료: {}", combinedResponse);

        return ResponseEntity.ok(combinedResponse);
    }

    // 문제 분석 요청 빌드
    private ProblemAnalysisRequestDto buildAnalysisRequest(ProblemResponseDto response) {
        return ProblemAnalysisRequestDto.builder()
                .problemId(response.getProblemId())
                .description(response.getDescription())
                .input(response.getInput())
                .output(response.getOutput())
                .timeLimit(response.getTimeLimit())
                .memoryLimit(response.getMemoryLimit())
                .tags(response.getTags())
                .build();
    }
} 