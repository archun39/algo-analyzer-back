package com.algoanalyzer.problem.presentation.controller;

import com.algoanalyzer.problem.application.port.in.GetProblemUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.algoanalyzer.problem.domain.model.Problem;
import com.algoanalyzer.problem.presentation.dto.response.ProblemResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problems")
public class ProblemController {
    private final GetProblemUseCase getProblemUseCase;

    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemResponseDto> getProblem(@PathVariable Long problemId) {
        Problem problem = getProblemUseCase.getProblem(problemId);

        ProblemResponseDto responseDto = ProblemResponseDto.builder()
            .problemId(problem.getProblemId())
            .title(problem.getTitle())
            .description(problem.getDescription())
            .timeLimit(problem.getTimeLimit())
            .memoryLimit(problem.getMemoryLimit())
            .build();
        
        return ResponseEntity.ok(responseDto);
    }
} 