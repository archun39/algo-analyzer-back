package com.algoanalyzer.domain.analysis.problem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algoanalyzer.domain.analysis.problem.dto.request.ProblemAnalysisRequestDto;
import com.algoanalyzer.domain.analysis.problem.dto.response.ProblemAnalysisResponseDto;
import com.algoanalyzer.domain.analysis.problem.service.ProblemAnalysisService;
import com.algoanalyzer.domain.analysis.problem.repository.AnalysisRepository;
import com.algoanalyzer.domain.problem.dto.response.ProblemResponseDto;
import com.algoanalyzer.domain.problem.service.ProblemService;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/analysis/problem")
@RequiredArgsConstructor
public class ProblemAnalysisController {
    private final AnalysisRepository analysisRepository;
    private final ProblemAnalysisService problemAnalysisService;
    
    @PostMapping("/analyze")
    public ResponseEntity<ProblemAnalysisResponseDto> analyzeProblem(
            @RequestBody @Valid ProblemAnalysisRequestDto request) {
        
        
        request = ProblemAnalysisRequestDto.builder()
            .problemId(request.getProblemId())
            .description(request.getDescription())
            .input(request.getInput())
            .output(request.getOutput())
            .timeLimit(request.getTimeLimit())
            .memoryLimit(request.getMemoryLimit())
            .tags(request.getTags())
            .build();

        ProblemAnalysisResponseDto result = problemAnalysisService.analyzeProblem(request);
        
        // 분석 결과를 프론트엔드로 반환
        return ResponseEntity.ok(result);
    }
} 