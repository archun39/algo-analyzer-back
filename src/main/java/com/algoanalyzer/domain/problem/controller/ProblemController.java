package com.algoanalyzer.domain.problem.controller;

import com.algoanalyzer.domain.problem.dto.request.ProblemRequestDto;
import com.algoanalyzer.domain.problem.dto.response.ProblemResponseDto;
import com.algoanalyzer.domain.problem.service.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {
    
    private final ProblemService problemService;
    
    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemResponseDto> getProblem(@PathVariable Long problemId) {
        return ResponseEntity.ok(problemService.getProblem(problemId));
    }

    @PostMapping("/search")
    public ResponseEntity<ProblemResponseDto> searchProblem(
            @RequestBody @Valid ProblemRequestDto request) {
        return ResponseEntity.ok(problemService.getProblem(request.getProblemId()));
    }
} 