package com.algoanalyzer.problem.infrastructure.client;

import com.algoanalyzer.problem.application.port.out.ProblemClient;
import com.algoanalyzer.problem.domain.model.Problem;
import com.algoanalyzer.problem.domain.exception.ProblemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.client.RestTemplate;

import org.springframework.stereotype.Component;

//BOJ 크롤링을 통해 Problem 도메인 객체를 생성하는 Out Port 구현체
@Component
@RequiredArgsConstructor
public class CrawlingProblemClient implements ProblemClient {
    private static final String BOJ_PROBLEM_URL = "https://www.acmicpc.net/problem/";
    private static final String SOLVED_AC_API_URL = "https://solved.ac/api/v3/problem/show?problemId=";
    private final RestTemplate restTemplate;
    
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
            Elements inputEls = doc.select("div#problem_input");
            String input = inputEls.isEmpty() ? "" : inputEls.first().html();
            Elements outputEls = doc.select("div#problem_output");
            String output = outputEls.isEmpty() ? "" : outputEls.first().html();
           
            // tags 추출 via Solved.ac API (fallback to page crawling)
            List<String> tags = fetchTagsFromApi(problemId);

            return Problem.builder()
                .problemId(problemId)
                .title(title)
                .description(description)
                .input(input)
                .output(output)
                .timeLimit(timeLimit)
                .memoryLimit(memoryLimit)
                .tags(tags)
                .build();

        } catch (Exception e) {
            throw new ProblemNotFoundException("문제 정보를 불러오는 중 오류: " + problemId, e);
        }
    }

    /**
     * Solved.ac API를 통해 문제 태그를 가져옵니다. 실패 시 빈 리스트 반환.
     */
    private List<String> fetchTagsFromApi(Long problemId) {
        try {
            JsonNode root = restTemplate.getForObject(SOLVED_AC_API_URL + problemId, JsonNode.class);
            if (root != null && root.has("tags")) {
                List<String> list = new ArrayList<>();
                for (JsonNode tagNode : root.get("tags")) {
                    if (tagNode.has("displayNames")) {
                        JsonNode dn = tagNode.get("displayNames");
                        if (dn.isArray() && dn.size() > 0 && dn.get(0).has("name")) {
                            list.add(dn.get(0).get("name").asText());
                            continue;
                        }
                    }
                    if (tagNode.has("name")) {
                        list.add(tagNode.get("name").asText());
                    }
                }
                if (!list.isEmpty()) {
                    return list;
                }
            }
        } catch (Exception ignored) {
        }
        // fallback to page crawling
        try {
            return new ArrayList<>(
                Jsoup.connect(BOJ_PROBLEM_URL + problemId)
                    .timeout(5000)
                    .get()
                    .select("#problem_tags a.spoiler-link")
                    .eachText()
            );
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }
} 