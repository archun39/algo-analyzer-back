package com.algoanalyzer.analysis.infrastructure.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
@Document(collection = "analysis_level_1")
public class ProblemAnalysisDocument {
    @Id
    private Long problemId;

    private String timeComplexity;
    private String timeComplexityReasoning;
    private String spaceComplexity;
    private String spaceComplexityReasoning;
    private String algorithmType;
    private String algorithmTypeReasoning;
    private String dataStructures;
    private final String dataStructuresReasoning;
    private final String solutionImplementation;
    private final String solutionImplementationReasoning;
}
