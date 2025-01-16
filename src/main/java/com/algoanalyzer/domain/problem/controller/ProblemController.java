package com.algoanalyzer.domain.problem.controller;

import com.algoanalyzer.domain.problem.dto.request.ProblemRequestDto;
import com.algoanalyzer.domain.problem.dto.response.ProblemResponseDto;
import com.algoanalyzer.domain.problem.dto.response.ProblemWithAnalysisResponseDto;
import com.algoanalyzer.domain.problem.service.ProblemService;
import com.algoanalyzer.domain.analysis.problem.service.ProblemAnalysisService;
import com.algoanalyzer.domain.analysis.problem.dto.request.ProblemAnalysisRequestDto;
import com.algoanalyzer.domain.analysis.problem.dto.response.ProblemAnalysisResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
@Slf4j
public class ProblemController {
    
    private final ProblemService problemService;
    private final ProblemAnalysisService problemAnalysisService;
    
    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemWithAnalysisResponseDto> getProblem(@PathVariable Long problemId) {
        ProblemResponseDto response = problemService.getProblem(problemId);
        
        ProblemAnalysisRequestDto analysisRequest = ProblemAnalysisRequestDto.builder()
                .problemId(response.getProblemId())
                .description(response.getDescription())
                .input(response.getInput())
                .output(response.getOutput())
                .timeLimit(response.getTimeLimit())
                .memoryLimit(response.getMemoryLimit())
                .tags(response.getTags())
                .build();
        
        ProblemAnalysisResponseDto analysisResult = problemAnalysisService.analyzeProblem(analysisRequest);

        ProblemWithAnalysisResponseDto combinedResponse = ProblemWithAnalysisResponseDto.builder()
                .problemResponse(response)
                .analysisResponse(analysisResult)
                .build();

        return ResponseEntity.ok(combinedResponse);
    }

    @PostMapping("/search")
    public ResponseEntity<ProblemResponseDto> searchProblem(
            @RequestBody @Valid ProblemRequestDto request) {
        return ResponseEntity.ok(problemService.getProblem(request.getProblemId()));
    }
} 