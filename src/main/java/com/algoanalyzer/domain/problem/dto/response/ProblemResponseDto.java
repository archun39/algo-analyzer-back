package com.algoanalyzer.domain.problem.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProblemResponseDto {
    private Long problemId;
    private String title;
    private String description;
    private String input;
    private String output;
    private String timeLimit;
    private String memoryLimit;
} 