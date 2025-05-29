package com.algoanalyzer.analysis.application.service;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import com.algoanalyzer.analysis.application.port.in.AnalyzeProblemUseCase;
import com.algoanalyzer.analysis.application.port.out.AnalyzeProblemClient;
import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;
import com.algoanalyzer.analysis.domain.repository.ProblemAnalysisRepository;
import com.algoanalyzer.analysis.presentation.dto.request.ProblemAnalysisRequestDto;
import com.algoanalyzer.problem.domain.model.Problem;
import com.algoanalyzer.problem.application.port.in.GetProblemUseCase;
import com.algoanalyzer.problem.application.mapper.ProblemMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "analysis")
public class AnalyzeProblemService implements AnalyzeProblemUseCase {
    private final ProblemAnalysisRepository problemAnalysisRepository;
    private final AnalyzeProblemClient client;
    private final GetProblemUseCase getProblemUseCase;
    private final ProblemMapper problemMapper;
    private final StringRedisTemplate redisTemplate;

    @Override
    @Cacheable(key = "#problemId", unless = "#result.problemId == null")
    public ProblemAnalysis analyzeProblem(Long problemId) {
        return problemAnalysisRepository.findByProblemId(problemId)
            .orElseGet(() -> performAnalysisWithLock(problemId));
    }

    private ProblemAnalysis performAnalysisWithLock(Long problemId) {
        String redisKey = "analyzing:problem:" + problemId;
        if (!acquireLock(redisKey)) {
            return waitForAnalysisCompletion(redisKey, problemId);
        }
        return executeAnalysis(redisKey, problemId);
    }

    private boolean acquireLock(String redisKey) {
        return Boolean.TRUE.equals(
            redisTemplate.opsForValue().setIfAbsent(redisKey, "IN_PROGRESS", Duration.ofMinutes(3))
        );
    }

    private ProblemAnalysis waitForAnalysisCompletion(String redisKey, Long problemId) {
        while (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return problemAnalysisRepository.findByProblemId(problemId)
            .orElseThrow(() -> new RuntimeException("문제 분석 결과가 없거나 TTL이 만료됨."));
    }

    private ProblemAnalysis executeAnalysis(String redisKey, Long problemId) {
        try {
            System.out.println("문제 분석 시작");
            Problem problem = getProblemUseCase.getProblem(problemId);
            ProblemAnalysisRequestDto dto = problemMapper.toRequestDto(problem);
            ProblemAnalysis pa = client.callPythonApi(dto);
            problemAnalysisRepository.save(pa);
            return pa;
        } finally {
            redisTemplate.delete(redisKey);
        }
    }
}