package com.algoanalyzer.analysis.domain.model;

import lombok.Builder;
import lombok.Getter;

/**
 * 문제 분석 결과를 나타내는 도메인 모델입니다.
 */
@Getter
@Builder
public class ProblemAnalysis {
    private final Long problemId;
    private final String timeComplexity;            // 시간 복잡도
    private final String timeComplexityReasoning;  // 시간 복잡도 근거 설명
    private final String spaceComplexity;           // 공간 복잡도
    private final String spaceComplexityReasoning; // 공간 복잡도 근거 설명
    private final String algorithmType;             // 알고리즘 유형
    private final String algorithmTypeReasoning;   // 알고리즘 유형 근거 설명
    private final String dataStructures;           // 사용된 자료구조
    private final String dataStructuresReasoning;  // 자료구조 근거 설명
    private final String solutionImplementation;   // 구현 로직 요약
    private final String solutionImplementationReasoning; // 구현 로직 설명
}
