
package com.algoanalyzer.problem.infrastructure.repository;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.Builder;

@Builder
@Data
@Document(collection = "problems")  // MongoDB 컬렉션 이름

// 문제 정보를 저장하는 모델
public class ProblemDocument {
    @Id
    private Long problemId;

    private String title;
    private String description;
    private String input;
    private String output;
    private String timeLimit;
    private String memoryLimit;
    private List<String> tags;
} 