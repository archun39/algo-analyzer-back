package com.algoanalyzer.domain.analysis.problem.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class ProblemAnalysisRequestDto {
    @NotNull(message = "문제 번호는 필수입니다.")
    private Long problemId;
    
    private String description;

    private String input;
    private String output;

    private String timeLimit;
    private String memoryLimit;
    private List<String> tags;
} 