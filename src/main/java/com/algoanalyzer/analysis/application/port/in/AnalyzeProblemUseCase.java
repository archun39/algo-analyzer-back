package com.algoanalyzer.analysis.application.port.in;

import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;

public interface AnalyzeProblemUseCase {
    ProblemAnalysis analyzeProblem(Long problemId);
}
