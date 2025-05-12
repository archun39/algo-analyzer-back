package com.algoanalyzer.analysis.application.port.in;

import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;
import com.algoanalyzer.analysis.presentation.dto.request.ProblemAnalysisRequestDto;

public interface AnalyzeProblemUseCase {
    ProblemAnalysis analyzeProblem(ProblemAnalysisRequestDto request);
}
