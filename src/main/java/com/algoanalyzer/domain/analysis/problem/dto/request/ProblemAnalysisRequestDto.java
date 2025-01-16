package com.algoanalyzer.domain.analysis.problem.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class ProblemAnalysisRequestDto {
    @NotNull(message = "문제 번호는 필수입니다.")
    @JsonProperty("problem_id")
    private Long problemId;
    
    @JsonProperty("description")
    private String description;

    @JsonProperty("input")
    private String input;
    
    @JsonProperty("output")
    private String output;

    @JsonProperty("time_limit")
    private String timeLimit;

    @JsonProperty("memory_limit")
    private String memoryLimit;

    @JsonProperty("tags")
    private List<String> tags;
} 