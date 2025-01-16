package com.algoanalyzer.domain.problem.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProblemRequestDto {
    @NotNull(message = "문제 번호는 필수입니다.")
    @Positive(message = "문제 번호는 양수여야 합니다.")
    private Long problemId;
} 