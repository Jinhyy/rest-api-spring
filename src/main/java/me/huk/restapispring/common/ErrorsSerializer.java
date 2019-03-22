package me.huk.restapispring.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

// 9-2. Erros 객체를 json 으로 파싱해주는 클래스
@JsonComponent // Object Mapper에 등록하기 위한 설정.
public class ErrorsSerializer extends JsonSerializer<Errors> {

    /*
        Errors 객체를 serialize 하는 방법을 기술.
        이는 후에 Object Mapper에 Errors 객체에 대하여 overriding 될 것.
     */
    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();

        errors.getFieldErrors().stream().forEach( e -> {
            try {
                gen.writeStartObject();

                gen.writeStringField("field",e.getField());
                gen.writeStringField("objectName",e.getObjectName());
                gen.writeStringField("code",e.getCode());
                gen.writeStringField("defaultMessage",e.getDefaultMessage());
                Object rejectedValue = e.getRejectedValue();

                if(rejectedValue!=null)
                    gen.writeStringField("rejectedValue",rejectedValue.toString());

                gen.writeEndObject();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        gen.writeEndArray();
    }
}
