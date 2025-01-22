package com.algoanalyzer.domain.analysis.problem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
@Data
@Document(collection = "analysis_level1")
public class AnalysisLevel1Document {
    @Id
    private Long problemId;
    private String timeComplexity;
    private String timeComplexityReasoning;
    private String spaceComplexity;
    private String spaceComplexityReasoning;
    private String algorithmType;
    private String algorithmTypeReasoning;
    private String dataStructures;
    private String dataStructuresReasoning;
    private String solutionImplementation;
    private String solutionImplementationReasoning;
}