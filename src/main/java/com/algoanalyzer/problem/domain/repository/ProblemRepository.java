// src/main/java/com/algoanalyzer/problem/domain/repository/ProblemRepository.java
package com.algoanalyzer.problem.domain.repository;

import com.algoanalyzer.problem.domain.model.Problem;
import java.util.Optional;

public interface ProblemRepository {
    Optional<Problem> findByProblemId(Long problemId);
    void save(Problem problem);
}