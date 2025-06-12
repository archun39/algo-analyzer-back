package com.algoanalyzer.analysis.application.mapper;

import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;
import com.algoanalyzer.analysis.presentation.dto.response.ProblemAnalysisResponseDto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
public class ProblemAnalysisMapperTest {

    @Autowired
    private ProblemAnalysisMapper problemAnalysisMapper;

    @Test
    public void testToResponseDto() {
        // given
        ProblemAnalysis analysis = ProblemAnalysis.builder()
            .problemId(1000L)
            .timeComplexity("O(n)")
            .timeComplexityReasoning("Linear time complexity")
            .spaceComplexity("O(1)")
            .spaceComplexityReasoning("Constant space complexity")
            .algorithmType("Greedy")
            .algorithmTypeReasoning("Greedy algorithm is suitable")
            .dataStructures("Array")
            .dataStructuresReasoning("Array is used for storage")
            .solutionImplementation("Implemented using a loop")
            .solutionImplementationReasoning("Loop is used for iteration")
            .build();

        // when
        ProblemAnalysisResponseDto responseDto = problemAnalysisMapper.toResponseDto(analysis);

        // then
        assertEquals(analysis.getProblemId(), responseDto.getProblemId());
        assertEquals(analysis.getTimeComplexity(), responseDto.getTimeComplexity());
        assertEquals(analysis.getTimeComplexityReasoning(), responseDto.getTimeComplexityReasoning());
        assertEquals(analysis.getSpaceComplexity(), responseDto.getSpaceComplexity());
        assertEquals(analysis.getSpaceComplexityReasoning(), responseDto.getSpaceComplexityReasoning());
        assertEquals(analysis.getAlgorithmType(), responseDto.getAlgorithmType());
        assertEquals(analysis.getAlgorithmTypeReasoning(), responseDto.getAlgorithmTypeReasoning());
        assertEquals(analysis.getDataStructures(), responseDto.getDataStructures());
        assertEquals(analysis.getDataStructuresReasoning(), responseDto.getDataStructuresReasoning());
        assertEquals(analysis.getSolutionImplementation(), responseDto.getSolutionImplementation());
        assertEquals(analysis.getSolutionImplementationReasoning(), responseDto.getSolutionImplementationReasoning());
    }
} 