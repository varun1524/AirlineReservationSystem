package edu.sjsu.cmpe275.lab2.service;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {
    public String getErrorJSONResponse(String message, HttpStatus status, String errorType){
        JSONObject responseJsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("msg", message);
        jsonObject1.put("code", status.value());
        responseJsonObject.put(errorType, jsonObject1);
        return responseJsonObject.toString();
    }
}
