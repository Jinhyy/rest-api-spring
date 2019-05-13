package me.huk.restapispring.event;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value= "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){
        // 8-3. data binding 검증(valid 조건 및 요청이 제대로 들어갔나)
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        // 8-4. 비즈니스 검증위한 validator
        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()){
            System.out.println(errors.getAllErrors());
            return ResponseEntity.badRequest().body(errors);
        }

        // 6-2. mapper 활용한 데이터 바인딩(물론 reflection이 발생하여 속도가 좀 느림)
        Event event = modelMapper.map(eventDto,Event.class);

        Event savedEvent = this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(savedEvent.getId()).toUri();
        // created를 하려면 URI가 존재해야하고, Uri는 Hateoas의
        // ControllerLinkBuilder의 linkTo와 methodOn 기능을 사용하면 만들기 쉽다.

        // Hateoas 설정(self-rel, query-events 부분 적용)
        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(new Link(createdUri.toString()).withSelfRel());
        return ResponseEntity.created(createdUri).body(eventResource);
    }
}
