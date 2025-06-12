package com.algoanalyzer.analysis.infrastructure.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
public class ProblemAnalysisDocumentTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testSaveAndRetrieveProblemAnalysisDocument() {
        // given
        ProblemAnalysisDocument analysisDocument = ProblemAnalysisDocument.builder()
            .problemId(1L)
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
        mongoTemplate.save(analysisDocument);

        // then
        ProblemAnalysisDocument retrievedDocument = mongoTemplate.findById(analysisDocument.getId(), ProblemAnalysisDocument.class);
        assertNotNull(retrievedDocument);
        assertEquals(analysisDocument.getProblemId(), retrievedDocument.getProblemId());
        assertEquals(analysisDocument.getTimeComplexity(), retrievedDocument.getTimeComplexity());
    }
} 