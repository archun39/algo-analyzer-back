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
        // 문제 조회 시작 시간 측정
        long startTime = System.currentTimeMillis();
        Problem problem = getProblemUseCase.getProblem(problemId);
        long fetchTimeMs = System.currentTimeMillis() - startTime;

        System.out.println("문제 조회 시간: " + fetchTimeMs + "ms");
        ProblemResponseDto responseDto = buildResponseDto(problem);
        
        return ResponseEntity.ok(responseDto);
    }

    private ProblemResponseDto buildResponseDto(Problem problem) {
        return ProblemResponseDto.builder()
            .problemId(problem.getProblemId())
            .title(problem.getTitle())
            .description(problem.getDescription())
            .input(problem.getInput())
            .output(problem.getOutput())
            .timeLimit(problem.getTimeLimit())
            .memoryLimit(problem.getMemoryLimit())
            .tags(problem.getTags())
            .build();
    }
} 