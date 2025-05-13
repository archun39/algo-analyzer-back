package com.algoanalyzer.analysis.presentation.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.algoanalyzer.analysis.application.port.in.AnalyzeProblemUseCase;
import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;
import com.algoanalyzer.analysis.presentation.dto.request.ProblemAnalysisRequestDto;
import com.algoanalyzer.analysis.presentation.dto.response.ProblemAnalysisResponseDto;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemAnalysisController {
    private final AnalyzeProblemUseCase analyzeProblemUseCase;
    
    @PostMapping("/problemId}/analysis")
    public ResponseEntity<ProblemAnalysisResponseDto> analyzeProblem(
        @RequestBody @Valid ProblemAnalysisRequestDto request) {
        // 유스케이스 실행
        ProblemAnalysis analysis = analyzeProblemUseCase.analyzeProblem(request);
        // 도메인 모델 → 응답 DTO 매핑
        ProblemAnalysisResponseDto dto = ProblemAnalysisResponseDto.builder()
            .problemId(analysis.getProblemId())
            .timeComplexity(analysis.getTimeComplexity())
            .timeComplexityReasoning(analysis.getTimeComplexityReasoning())
            .spaceComplexity(analysis.getSpaceComplexity())
            .spaceComplexityReasoning(analysis.getSpaceComplexityReasoning())
            .algorithmType(analysis.getAlgorithmType())
            .algorithmTypeReasoning(analysis.getAlgorithmTypeReasoning())
            .dataStructures(analysis.getDataStructures())
            .dataStructuresReasoning(analysis.getDataStructuresReasoning())
            .solutionImplementation(analysis.getSolutionImplementation())
            .solutionImplementationReasoning(analysis.getSolutionImplementationReasoning())
            .build();
        return ResponseEntity.ok(dto);
    }
} 