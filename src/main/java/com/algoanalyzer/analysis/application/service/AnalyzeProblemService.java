package com.algoanalyzer.analysis.application.service;

import org.springframework.stereotype.Service;

import com.algoanalyzer.analysis.application.port.in.AnalyzeProblemUseCase;
import com.algoanalyzer.analysis.application.port.out.AnalyzeProblemClient;
import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;
import com.algoanalyzer.analysis.domain.repository.ProblemAnalysisRepository;
import com.algoanalyzer.analysis.presentation.dto.request.ProblemAnalysisRequestDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyzeProblemService implements AnalyzeProblemUseCase{
    private final ProblemAnalysisRepository problemAnalysisRepository;
    private final AnalyzeProblemClient client;

    @Override
    public ProblemAnalysis analyzeProblem(ProblemAnalysisRequestDto request) {
        //1) 문제 분석 결과가 이미 존재하는지 확인
        return problemAnalysisRepository.findByProblemId(request.getProblemId())
            .orElseGet(() -> {
                //2) 문제 분석 결과가 없으면 새로 분석
                ProblemAnalysis pa = client.callPythonApi(request);
                problemAnalysisRepository.save(pa);
                return pa;
            });
    }
}
