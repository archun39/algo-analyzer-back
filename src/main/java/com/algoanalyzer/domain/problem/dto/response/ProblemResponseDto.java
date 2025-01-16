package com.algoanalyzer.domain.problem.dto.response;

import com.algoanalyzer.domain.analysis.problem.dto.response.ProblemAnalysisResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
public class ProblemResponseDto {
    private Long problemId;
    private String title;
    private String description;
    private String input;
    private String output;
    private String timeLimit;
    private String memoryLimit;
    private List<String> tags;
} 