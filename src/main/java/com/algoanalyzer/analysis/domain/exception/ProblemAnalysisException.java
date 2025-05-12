package com.algoanalyzer.analysis.domain.exception;

/**
 * 문제 분석 과정에서 발생하는 예외입니다.
 */
public class ProblemAnalysisException extends RuntimeException {
    public ProblemAnalysisException(String message) {
        super(message);
    }

    public ProblemAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
} 