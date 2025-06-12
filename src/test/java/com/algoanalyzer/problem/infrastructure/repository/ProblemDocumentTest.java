package com.algoanalyzer.problem.infrastructure.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
public class ProblemDocumentTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testSaveAndRetrieveProblemDocument() {
        // given
        ProblemDocument problemDocument = ProblemDocument.builder()
            .problemId(1000L)
            .title("Test Problem")
            .description("Test Description")
            .input("Test Input")
            .output("Test Output")
            .timeLimit("1s")
            .memoryLimit("256MB")
            .build();

        // when
        mongoTemplate.save(problemDocument);

        // then
        ProblemDocument retrievedDocument = mongoTemplate.findById(problemDocument.getId(), ProblemDocument.class);
        assertNotNull(retrievedDocument);
        assertEquals(problemDocument.getProblemId(), retrievedDocument.getProblemId());
        assertEquals(problemDocument.getTitle(), retrievedDocument.getTitle());
    }
} 