package me.huk.restapispring.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.huk.restapispring.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
// @WebMvcTest     // 슬라이스 테스트이기 때문에 웹용빈만 등록, repository는 등록x
@SpringBootTest     // Springboot Application부터 모든 빈들을 찾아 등록 할 것.
@AutoConfigureMockMvc // 6-3 mock으론 더이상 테스트 불가하므로(model mapper로 변환하기 때문에)
public class EventControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test   // 7-1. DTO에 해당하는 값들만 넣었을 때 확인하기 위하여 Dto로 받는다.
    public void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("jh-event").description("jh-des")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,3,11,16,44))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,3,11,16,50))
                .beginEventDateTime(LocalDateTime.of(2019,3,15,18,30))
                .endEventDateTime(LocalDateTime.of(2019,3,15,20,30))
                .basePrice(100).maxPrice(200).limitOfEnrollment(100).location("수서역")
                .build();
        // --> 이젠 Mocking이 아니고 실제 Repository에 save된 값을 조회할 것이므로
        // dto에 없었던 id,eventStatus는 전달되지 않을 것이다.

            mockMvc.perform(post("/api/events/")
                    // request header 설정
                    .contentType(MediaType.APPLICATION_JSON_UTF8)   // 요청응답 json 으로 보내겠다.
                    .accept(MediaTypes.HAL_JSON_UTF8_VALUE)    // HAL_JSON 타입 응답만 허용하겠다.

                    // request body 설정
                    .content(objectMapper.writeValueAsString(eventDto)))    // object Mapper로 event 요청 json 형식으로 바꾼다.

                    // reponse header 검증
                    .andExpect(status().isCreated())   // created : 201 상태코드
                    .andExpect(header().exists(HttpHeaders.LOCATION))
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_UTF8_VALUE))

                    // response body 검증
                    .andExpect(jsonPath("id").value(Matchers.not(100)))
                    .andExpect(jsonPath("free").value("false"))
                    .andExpect(jsonPath("offline").value("false"))
                    .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT))
                    .andExpect(status().isOk())
                    .andDo(print());
    }

    @Test   // 7-2. 누군가 입력값으로 dto가 설정한 입력값아닌 id,free같은 변수도 준다면? --> BadRequest 설정해서 돌려주기
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100).name("jh-event").description("jh-des")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,3,11,16,44))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,3,11,16,50))
                .beginEventDateTime(LocalDateTime.of(2019,3,15,18,30))
                .endEventDateTime(LocalDateTime.of(2019,3,15,20,30))
                .basePrice(100).maxPrice(200).limitOfEnrollment(100).location("수서역")
                .free(true).offline(true).eventStatus(EventStatus.PUBLISHED)
                .build();
        // --> 이젠 Mocking이 아니고 실제 Repository에 save된 값을 조회할 것이므로
        // dto에 없었던 id,eventStatus는 전달되지 않을 것이다.

        mockMvc.perform(post("/api/events/")
                // request header 설정
                .contentType(MediaType.APPLICATION_JSON_UTF8)   // 요청응답 json 으로 보내겠다.
                .accept(MediaTypes.HAL_JSON_UTF8_VALUE)    // HAL_JSON 타입 응답만 허용하겠다.

                // request body 설정
                .content(objectMapper.writeValueAsString(event)))    // object Mapper로 event 요청 json 형식으로 바꾼다.

                // status Badrequest(400) 검증
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    @TestDescription("8-1. 입력값 바인딩 검증. (bad request 처리)")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(eventDto)))

                // status Badrequest(400) 검증
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @TestDescription("8-2. 입력값 비즈니스로직 검증(beginEvent> close 일 때")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("jh-event").description("jh-des")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,3,11,16,30))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,3,11,16,50))
                .beginEventDateTime(LocalDateTime.of(2019,3,16,18,30))
                .endEventDateTime(LocalDateTime.of(2019,3,15,20,30))
                .basePrice(100).maxPrice(200).limitOfEnrollment(100).location("수서역")
                .build();

        this.mockMvc.perform(post(("/api/events"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());

    }
}