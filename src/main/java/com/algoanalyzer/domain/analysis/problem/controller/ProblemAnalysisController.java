package com.algoanalyzer.domain.analysis.problem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algoanalyzer.domain.analysis.problem.dto.request.ProblemAnalysisRequestDto;
import com.algoanalyzer.domain.analysis.problem.dto.response.ProblemAnalysisResponseDto;
import com.algoanalyzer.domain.analysis.problem.service.ProblemAnalysisService;
import com.algoanalyzer.domain.problem.dto.response.ProblemResponseDto;
import com.algoanalyzer.domain.problem.service.ProblemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/analysis/problem")
@RequiredArgsConstructor
public class ProblemAnalysisController {
    
    private final ProblemAnalysisService problemAnalysisService;
    private final ProblemService problemService;
    
    @PostMapping("/analyze")
    public ResponseEntity<ProblemAnalysisResponseDto> analyzeProblem(
            @RequestBody @Valid ProblemAnalysisRequestDto request) {
        // 먼저 문제 정보를 가져옴
        ProblemResponseDto problemInfo = problemService.getProblem(request.getProblemId());
        
        // 문제 정보를 분석 요청 DTO에 설정
        request = ProblemAnalysisRequestDto.builder()
                .problemId(problemInfo.getProblemId())
                .description(problemInfo.getDescription())
                .input(problemInfo.getInput())
                .output(problemInfo.getOutput())
                .timeLimit(problemInfo.getTimeLimit())
                .memoryLimit(problemInfo.getMemoryLimit())
                .tags(problemInfo.getTags())
                .build();
        
        return ResponseEntity.ok(problemAnalysisService.analyzeProblem(request));
    }
} 