package com.algoanalyzer.analysis.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.RequiredArgsConstructor;

import com.algoanalyzer.analysis.application.port.in.AnalyzeProblemUseCase;
import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;
import com.algoanalyzer.analysis.presentation.dto.response.ProblemAnalysisResponseDto;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemAnalysisController {
    private final AnalyzeProblemUseCase analyzeProblemUseCase;
    
    @PostMapping("/{problemId}/analysis")
    public ResponseEntity<ProblemAnalysisResponseDto> analyzeProblem(
        @PathVariable Long problemId) {
        // 유스케이스 실행
        ProblemAnalysis analysis = analyzeProblemUseCase.analyzeProblem(problemId);
        
        // 도메인 모델 → 응답 DTO 매
        ProblemAnalysisResponseDto responseDto = buildResponseDto(analysis);
        
        return ResponseEntity.ok(responseDto);
    }

    private ProblemAnalysisResponseDto buildResponseDto(ProblemAnalysis analysis) {
        return ProblemAnalysisResponseDto.builder()
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
    }
} 