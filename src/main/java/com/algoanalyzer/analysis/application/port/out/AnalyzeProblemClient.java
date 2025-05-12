package com.algoanalyzer.analysis.application.port.out;

import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;
import com.algoanalyzer.analysis.presentation.dto.request.ProblemAnalysisRequestDto;

public interface AnalyzeProblemClient {
    ProblemAnalysis callPythonApi(ProblemAnalysisRequestDto request);
}
