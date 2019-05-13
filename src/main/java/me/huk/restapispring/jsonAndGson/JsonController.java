package me.huk.restapispring.jsonAndGson;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Map;

@RestController
public class JsonController {

    JSONObject jsonObject;
    JSONObject jsonObject2;

    @PostConstruct
    public void init(){
        jsonObject = new JSONObject();
        jsonObject2 = new JSONObject();
        jsonObject.put("Key1","value1");
        jsonObject.put("Key2","value2");

        jsonObject2.put("databody",jsonObject);
        jsonObject2.toString();
    }

    @GetMapping("/json")
    public ResponseEntity<Map<String, Object>> jsonControllerMethod(){
        return ResponseEntity.status(HttpStatus.OK).body(jsonObject2.getJSONObject("databody").toMap());
    }

    @GetMapping("/jsonToObject")
    public JsonModel jsonModelMethod(){
        Gson gson = new Gson();
        JsonModel jsonModel = gson.fromJson(jsonObject2.getJSONObject("databody").toString(),JsonModel.class);
        return jsonModel;
    }
}
