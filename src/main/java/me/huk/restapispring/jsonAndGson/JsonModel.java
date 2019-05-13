package me.huk.restapispring.jsonAndGson;

import lombok.Data;

import java.io.Serializable;

@Data
public class JsonModel<T>{
    private String Key1;
    private String Key2;
}
