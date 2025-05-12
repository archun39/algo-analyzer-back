package com.algoanalyzer.problem.infrastructure.repository;

import com.algoanalyzer.problem.domain.repository.ProblemRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.algoanalyzer.problem.domain.model.Problem;
import lombok.RequiredArgsConstructor;

// MongoProblemRepository 클래스는 ProblemRepository 인터페이스의 MongoDB 기반 구현체로서,
// 문제 정보를 MongoDB에 저장하고 조회하는 역할을 수행합니다.
// 구체적으로, 주어진 문제 아이디를 이용하여 MongoDB 컬렉션에서 해당 문제 문서를 검색한 후,
// 이를 도메인 모델인 Problem 객체로 변환하여 반환합니다.
// 또한, 필요에 따라 새로운 문제 정보를 MongoDB에 저장하는 기능을 제공할 수 있도록 설계되어 있습니다.
@Repository
@RequiredArgsConstructor
public class MongoProblemRepository implements ProblemRepository {
    // ProblemDocumentRepository는 문제 정보를 MongoDB에 저장하고 조회하는 역할을 수행하는 인터페이스입니다.
    private final ProblemDocumentRepository problemDocumentRepository;

    @Override
    public Optional<Problem> findByProblemId(Long problemId) {
        return problemDocumentRepository.findByProblemId(problemId)
                .map(this::convertToProblem);
    }

    @Override
    public void save(Problem problem) {
        ProblemDocument doc = ProblemDocument.builder()
            .problemId(problem.getProblemId())
            .title(problem.getTitle())
            .description(problem.getDescription())
            .input(problem.getInput())
            .output(problem.getOutput())
            .timeLimit(problem.getTimeLimit())
            .memoryLimit(problem.getMemoryLimit())
            .tags(problem.getTags())
            .build();
        problemDocumentRepository.save(doc);
    }

    private Problem convertToProblem(ProblemDocument doc) {
        return Problem.builder()
            .problemId(doc.getProblemId())
            .title(doc.getTitle())
            .description(doc.getDescription())
            .input(doc.getInput())
            .output(doc.getOutput())
            .timeLimit(doc.getTimeLimit())
            .memoryLimit(doc.getMemoryLimit())
            .tags(doc.getTags())
            .build();
    }
}