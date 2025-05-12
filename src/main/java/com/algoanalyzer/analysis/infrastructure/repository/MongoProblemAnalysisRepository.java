package com.algoanalyzer.analysis.infrastructure.repository;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import java.util.Optional;
import com.algoanalyzer.analysis.domain.repository.ProblemAnalysisRepository;
import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;

@Repository
@RequiredArgsConstructor
public class MongoProblemAnalysisRepository implements ProblemAnalysisRepository {
    private final ProblemAnalysisDocumentRepository problemAnalysisDocumentRepository;

    @Override
    public Optional<ProblemAnalysis> findByProblemId(Long problemId) {
        return problemAnalysisDocumentRepository.findByProblemId(problemId)
            .map(this::convertToDomain);
    }

    @Override
    public void save(ProblemAnalysis analysis) {
        ProblemAnalysisDocument doc = ProblemAnalysisDocument.builder()
            .problemId(analysis.getProblemId())
            .timeComplexity(analysis.getTimeComplexity())
            .timeComplexityReasoning(analysis.getTimeComplexityReasoning())
            .spaceComplexity(analysis.getSpaceComplexity())
            .spaceComplexityReasoning(analysis.getSpaceComplexityReasoning())
            .algorithmType(analysis.getAlgorithmType())
            .algorithmTypeReasoning(analysis.getAlgorithmTypeReasoning())
            .dataStructures(analysis.getDataStructures())
            .dataStructuresReasoning(analysis.getDataStructuresReasoning())
            .solutionImplementation(analysis.getSolutionImplementation())
            .solutionImplementationReasoning(analysis.getSolutionImplementationReasoning())
            .build();
        problemAnalysisDocumentRepository.save(doc);
    }

    private ProblemAnalysis convertToDomain(ProblemAnalysisDocument document) {
        return ProblemAnalysis.builder()
            .problemId(document.getProblemId())
            .timeComplexity(document.getTimeComplexity())
            .timeComplexityReasoning(document.getTimeComplexityReasoning())
            .spaceComplexity(document.getSpaceComplexity())
            .spaceComplexityReasoning(document.getSpaceComplexityReasoning())
            .algorithmType(document.getAlgorithmType())
            .algorithmTypeReasoning(document.getAlgorithmTypeReasoning())
            .dataStructures(document.getDataStructures())
            .dataStructuresReasoning(document.getDataStructuresReasoning())
            .solutionImplementation(document.getSolutionImplementation())
            .solutionImplementationReasoning(document.getSolutionImplementationReasoning())
            .build();
    }
}
