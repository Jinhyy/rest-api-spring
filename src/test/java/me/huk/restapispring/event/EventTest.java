package me.huk.restapispring.event;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test   // 클래스 빌더 확인
    public void builder() {
        Event event = Event.builder()
        .name("이벤트 이름")
        .description("이벤트 설명")
        .build();
        assertThat(event).isNotNull();
    }

    // 자바 빈 스펙 만족하는지 확인
    @Test
    public void javaBeanCheck() {
        Event event = new Event();
        String name="J-event";
        String description="j-description";
        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
}