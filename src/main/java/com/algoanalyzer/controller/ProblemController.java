package com.algoanalyzer.controller;

import com.algoanalyzer.dto.ProblemRequestDto;
import com.algoanalyzer.dto.ProblemResponseDto;
import com.algoanalyzer.service.ProblemService;
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