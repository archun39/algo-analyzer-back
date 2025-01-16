package com.algoanalyzer.domain.problem.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

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
    private List<String> tags;
} 