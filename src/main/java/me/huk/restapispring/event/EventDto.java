package me.huk.restapispring.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/*
    6-1. 입력값을 제한하기 위하여 Dto로 입력값을 제한하여 받는다.
    --> Event Model로도 할 수 있겠지만, Validate등 어노테이션이 들어갈 것 이므로
    많은 Annotation 때문에 생기는 혼란을 줄이기 위하여 분리한다.
    --> but, Model인 Event의 데이터를 똑같이 받는 것이기 때문에 중복이 좀 생길것.
 */
@Builder @NoArgsConstructor @AllArgsConstructor
@Data
public class EventDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private LocalDateTime beginEnrollmentDateTime;
    @NotNull
    private LocalDateTime closeEnrollmentDateTime;
    @NotNull
    private LocalDateTime beginEventDateTime;
    @NotNull
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    @Min(0)
    private int basePrice; // (optional)
    @Min(0)
    private int maxPrice; // (optional)
    @Min(0)
    private int limitOfEnrollment;
}

