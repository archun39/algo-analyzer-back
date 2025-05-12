package com.algoanalyzer.domain.analysis.problem.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class ProblemAnalysisRequestDto {
    @NotNull(message = "문제 번호는 필수입니다.")
    private final Long problemId;
    private final String description;
    private final String input;
    private final String output;
    private final String timeLimit;
    private final String memoryLimit;
    private final List<String> tags;
} 