package com.algoanalyzer.domain.analysis.problem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProblemAnalysisRequestDto {
    @NotNull(message = "문제 번호는 필수입니다.")
    private Long problemId;
    private String title;
    private String description;
    private String input;
    private String output;
} 