package com.algoanalyzer.problem.infrastructure.client;

import com.algoanalyzer.problem.application.port.out.ProblemClient;
import com.algoanalyzer.problem.domain.model.Problem;
import com.algoanalyzer.problem.domain.exception.ProblemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import java.util.Collections;

//BOJ 크롤링을 통해 Problem 도메인 객체를 생성하는 Out Port 구현체
@Component
@RequiredArgsConstructor
public class CrawlingProblemClient implements ProblemClient {
    private static final String BOJ_PROBLEM_URL = "https://www.acmicpc.net/problem/";

    @Override
    public Problem fetchProblem(Long problemId) {
        try {
            Document doc = Jsoup.connect(BOJ_PROBLEM_URL + problemId)
                .timeout(5000)
                .get();
            String title = doc.select("h1").text();
            Elements descEl = doc.select("#problem_description");
            String description = descEl.isEmpty() ? "" : descEl.html();
            Elements info = doc.select("table#problem-info tbody tr td");
            String timeLimit = info.size() >= 1 ? info.get(0).text() : "";
            String memoryLimit = info.size() >= 2 ? info.get(1).text() : "";
            return Problem.builder()
                .problemId(problemId)
                .title(title)
                .description(description)
                .timeLimit(timeLimit)
                .memoryLimit(memoryLimit)
                .tags(Collections.emptyList())
                .build();

        } catch (Exception e) {
            throw new ProblemNotFoundException("문제 정보를 불러오는 중 오류: " + problemId, e);
        }
    }
} 