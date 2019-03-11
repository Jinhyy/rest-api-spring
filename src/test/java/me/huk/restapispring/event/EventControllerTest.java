package me.huk.restapispring.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    EventRepository eventRepository;
    // mock 객체이기 때문에 save를 해도 저장되지 않으므로 오류 날것 --> Mockito 사용하여 해결한다.

    @Test
    public void createEvent() {
        Event event = Event.builder()
                .id(100).name("jh-event").description("jh-des")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,3,11,16,44))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,3,11,16,50))
                .beginEventDateTime(LocalDateTime.of(2019,3,15,18,30))
                .endEventDateTime(LocalDateTime.of(2019,3,15,20,30))
                .basePrice(100).maxPrice(200).limitOfEnrollment(100).location("수서역")
                .free(true).offline(false).eventStatus(EventStatus.BEGAN_ENROLLMENT)
                .build();
        // --> 이젠 Mocking이 아니고 실제 Repository에 save된 값을 조회할 것이므로
        // dto에 없었던 id,eventStatus는 전달되지 않을 것이다.


        try {
            mockMvc.perform(post("/api/events/")
                    // request header 설정
                    .contentType(MediaType.APPLICATION_JSON_UTF8)   // 요청응답 json 으로 보내겠다.
                    .accept(MediaTypes.HAL_JSON_UTF8_VALUE)    // HAL_JSON 타입 응답만 허용하겠다.
                    // request body 설정
                    .content(objectMapper.writeValueAsString(event))    // object Mapper로 event 요청 json 형식으로 바꾼다.
            )
                    // reponse header 검증
                    .andExpect(status().isCreated())   // created : 201 상태코드
                    .andExpect(header().exists(HttpHeaders.LOCATION))
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_UTF8_VALUE))
                    .andExpect(jsonPath("id").value(Matchers.not(100)))
                    .andExpect(jsonPath("free").value("true"))
                    .andExpect(jsonPath("offline").value("false"))
                    .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT))
                    .andDo(print());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}