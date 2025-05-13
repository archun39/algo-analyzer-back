package com.algoanalyzer.analysis.application.service;

import org.springframework.stereotype.Service;

import com.algoanalyzer.analysis.application.port.in.AnalyzeProblemUseCase;
import com.algoanalyzer.analysis.application.port.out.AnalyzeProblemClient;
import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;
import com.algoanalyzer.analysis.domain.repository.ProblemAnalysisRepository;
import com.algoanalyzer.analysis.presentation.dto.request.ProblemAnalysisRequestDto;
import com.algoanalyzer.problem.domain.model.Problem;
import com.algoanalyzer.problem.application.port.in.GetProblemUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyzeProblemService implements AnalyzeProblemUseCase{
    private final ProblemAnalysisRepository problemAnalysisRepository;
    private final AnalyzeProblemClient client;
    private final GetProblemUseCase getProblemUseCase;

    @Override
    public ProblemAnalysis analyzeProblem(Long problemId) {
        //1) 문제 분석 결과가 이미 존재하는지 확인
        return problemAnalysisRepository.findByProblemId(problemId)
            .map(existing -> {
                System.out.println("문제 분석 결과가 이미 존재합니다.");
                return existing;
            })
            .orElseGet(() -> {
                //2) 문제 분석 결과가 없으면 문제 분석 결과를 가져옴
                Problem problem = getProblemUseCase.getProblem(problemId);
                System.out.println("문제 정보 조회 완료");

                ProblemAnalysisRequestDto dto = buildRequestDto(problem);
                ProblemAnalysis pa = client.callPythonApi(dto);
                problemAnalysisRepository.save(pa);
                return pa;
            });
    }

    private ProblemAnalysisRequestDto buildRequestDto(Problem problem) {
        return ProblemAnalysisRequestDto.builder()
            .problemId(problem.getProblemId())
            .description(problem.getDescription())
            .input(problem.getInput())
            .output(problem.getOutput())
            .timeLimit(problem.getTimeLimit())
            .memoryLimit(problem.getMemoryLimit())
            .tags(problem.getTags())
            .build();
    }
}
