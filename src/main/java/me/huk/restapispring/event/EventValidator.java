package me.huk.restapispring.event;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    /*
        8-3. eventDto를 검증하고 발생한 에러는 errors 객체에 담는다.
     */
    public void validate(EventDto eventDto, Errors errors){

        if(eventDto.getBasePrice()>eventDto.getMaxPrice()){
            errors.rejectValue("basePrice","wrongValue","Base price must smaller than Max price");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEventDateTime())){
            errors.rejectValue("endEventDateTime","wrongValue","End enrollment time must be later than Begin endroll time");
        }

        // TODO 다른 비즈니스 로직 검증 필요
    }
}
