package edu.sjsu.cmpe275.lab2.service;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author varunshah
 * Response Service class for json and xml response
 */
@Service
public class ResponseService {

    /**
     * JSON formatting of ResponseEntity from Services
     * @param message Success or fail message
     * @param status HTTP status
     * @param errorType Custom error type
     * @return JSON object including all parameters
     */
    public String getJSONResponse(String message, HttpStatus status, String errorType){
        JSONObject responseJsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("msg", message);
        jsonObject1.put("code", status.value());
        responseJsonObject.put(errorType, jsonObject1);
        return responseJsonObject.toString();
    }

    /**
     * XML formatting of ResponseEntity from Services
     * @param message Success or fail message
     * @param status HTTP status
     * @param errorType Custom error type
     * @return XML object including all parameters
     */
    public String getXMLResponse(String message, HttpStatus status, String errorType){
        JSONObject responseJsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("msg", message);
        jsonObject1.put("code", status.value());
        responseJsonObject.put(errorType, jsonObject1);
        return XML.toString(responseJsonObject);
    }
}
