package com.algoanalyzer.domain.problem.dto.response;

import com.algoanalyzer.domain.analysis.problem.dto.response.ProblemAnalysisResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProblemWithAnalysisResponseDto {
    private ProblemResponseDto problemResponse;
    private ProblemAnalysisResponseDto analysisResponse;
} 