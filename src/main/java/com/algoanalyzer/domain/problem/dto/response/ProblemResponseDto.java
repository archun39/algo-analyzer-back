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
    @JsonProperty("problem_id")
    private Long problemId;

    private String title;
    private String description;
    private String input;
    private String output;

    @JsonProperty("time_limit")
    private String timeLimit;

    @JsonProperty("memory_limit")
    private String memoryLimit;
    
    private List<String> analysisResults; // 분석 결과를 문자열 리스트로 변경
} 