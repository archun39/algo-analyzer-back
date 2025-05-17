package com.algoanalyzer.analysis.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
/**
 * 문제 분석 결과를 나타내는 도메인 모델입니다.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemAnalysis {
    private Long problemId;
    private String timeComplexity;            // 시간 복잡도
    private String timeComplexityReasoning;  // 시간 복잡도 근거 설명
    private String spaceComplexity;           // 공간 복잡도
    private String spaceComplexityReasoning; // 공간 복잡도 근거 설명
    private String algorithmType;             // 알고리즘 유형
    private String algorithmTypeReasoning;   // 알고리즘 유형 근거 설명
    private String dataStructures;           // 사용된 자료구조
    private String dataStructuresReasoning;  // 자료구조 근거 설명
    private String solutionImplementation;   // 구현 로직 요약
    private String solutionImplementationReasoning; // 구현 로직 설명
}
