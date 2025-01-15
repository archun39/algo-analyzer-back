package com.algoanalyzer.domain.problem.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class ProblemResponseDto {
    @JsonProperty("problem_id")
    private int problemId;

    private String title;
    private String description;
    private String input;
    private String output;

    @JsonProperty("time_limit")
    private String timeLimit;

    @JsonProperty("memory_limit")
    private String memoryLimit;

    private int level;

    private List<String> tags;
} 