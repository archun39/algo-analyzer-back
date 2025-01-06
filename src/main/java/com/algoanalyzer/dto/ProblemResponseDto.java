package com.algoanalyzer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProblemResponseDto {
    private Long problemId;
    private String title;
    private String level;
    private String tags;
    private Double averageTries;
    private String description;
    private String input;
    private String output;
} 