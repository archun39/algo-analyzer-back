package com.algoanalyzer.analysis.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProblemAnalysisResponseDto {
    private Long problemId;
    private String timeComplexity; // 시간 복잡도
    private String timeComplexityReasoning; // 시간 복잡도를 분석한 근거 설명
    private String spaceComplexity; // 공간 복잡도
    private String spaceComplexityReasoning; // 공간 복잡도를 분석한 근거 설명
    private String algorithmType; // 알고리즘 유형
    private String algorithmTypeReasoning; // 알고리즘 유형을 선택한 근거 설명
    private String dataStructures; // 사용된 자료구조
    private String dataStructuresReasoning; // 사용된 자료구조를 포함한 설명
    private String solutionImplementation; // 문제 해결 방법의 구현 로직
    private String solutionImplementationReasoning; // 문제 해결 
}
