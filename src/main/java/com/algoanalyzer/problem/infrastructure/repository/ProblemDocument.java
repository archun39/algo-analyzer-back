package com.algoanalyzer.problem.infrastructure.repository;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.Builder;

@Builder
@Data
@Document(collection = "problems")
public class ProblemDocument {
    @Id
    private String id;

    @Indexed(unique = true)
    private Long problemId;

    private String title;
    private String description;
    private String input;
    private String output;
    private String timeLimit;
    private String memoryLimit;
    private List<String> tags;
} 