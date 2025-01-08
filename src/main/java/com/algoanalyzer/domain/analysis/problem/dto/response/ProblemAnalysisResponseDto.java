package com.algoanalyzer.domain.analysis.problem.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProblemAnalysisResponseDto {
    private String result;           // LLM 분석 결과
    private String explanation;      // 상세 설명
    private String difficulty;       // 난이도 평가
    private String topics;          // 관련 알고리즘/자료구조 주제들
} 