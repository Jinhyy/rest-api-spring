package me.huk.restapispring.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequestMapping(value= "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event){
        Event savedEvent = eventRepository.save(event);
        URI createdUri = ControllerLinkBuilder.
                linkTo(EventController.class).slash(savedEvent.getId()).toUri();
        // created를 하려면 URI가 존재해야하고, Uri는 Hateoas의
        // ControllerLinkBuilder의 linkTo와 methodOn 기능을 사용하면 만들기 쉽다.
        return ResponseEntity.created(createdUri).body(savedEvent);
    }
}
