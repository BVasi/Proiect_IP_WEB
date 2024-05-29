package com.project.ip.controller;

import com.project.ip.constants.Constants;
import com.project.ip.model.SensorData;
import com.project.ip.model.SensorType;
import com.project.ip.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GraphController
{
    @GetMapping(Constants.EndPoints.GRAPH + "/{userId}" + "/{numberOfDays}")
    public String getGraph(Model model, HttpSession session,
                           @PathVariable Integer userId,
                           @PathVariable Integer numberOfDays)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if (user == null)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.NOT_LOGGED_IN);
            return Constants.Redirects.LOGIN;
        }

        model.addAttribute(Constants.Attributes.SENSORS_DATA, getSensorsDataFromDB(userId, numberOfDays,
                SensorType.combineMasks(
                    SensorType.BLOOD_PRESSURE, SensorType.PULSE, SensorType.BODY_TEMPERATURE,
                    SensorType.WEIGHT, SensorType.GLUCOSE
                )));
        return Constants.EndPoints.GRAPH;
    }

    private Map<SensorType, List<SensorData>> getSensorsDataFromDB(final Integer userId, final Integer numberOfDays,
                                                               final Integer sensorsBitMask) //to do: refactor
    {
        try
        {
            List<Map<String, Object>> response = restTemplate.postForObject(Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.GET_SENSORS_DATA,
                    generateRequestBodyForGraphData(userId, numberOfDays, sensorsBitMask),
                    List.class);

            assert response != null;
            return mapServerResponseToExpectedParameters(response);
        } catch (HttpClientErrorException.NotFound exception)
        {
            return null;
        }
    }

    private Map<SensorType, List<SensorData>> mapServerResponseToExpectedParameters(final List<Map<String, Object>> response) //to do: refactor
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        Map<SensorType, List<SensorData>> expectedResponseMap = new HashMap<>();
        for (Map<String, Object> responseObject : response) //de modificat cu stream paralel
        {
            LocalDateTime date = LocalDateTime.parse((String)responseObject.get(Constants.ParametersKeys.DATE), dateTimeFormatter);
            responseObject.remove(Constants.ParametersKeys.DATE);
            for (Map.Entry<String, Object> mapEntry : responseObject.entrySet())
            {
                SensorType sensorType = SensorType.valueOf(mapEntry.getKey().toUpperCase());
                expectedResponseMap.putIfAbsent(sensorType, new ArrayList<>());
                try
                {
                    Double data = Double.parseDouble(String.valueOf(mapEntry.getValue())); //nici macar nu puneti intrebari
                    expectedResponseMap.get(sensorType).add(new SensorData(date, data));
                } catch (NumberFormatException exception)
                {
//                    System.out.println(Constants.Logs.EMPTY_SENSOR_VALUE);
                }
            }
        }

        return expectedResponseMap;
    }

    private Map<String, Integer> generateRequestBodyForGraphData(final Integer userId, final Integer numberOfDays,
                                                                 final Integer sensorsBitMask)
    {
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put(Constants.RequestParameters.ID, userId);
        requestBody.put(Constants.RequestParameters.NUMBER_OF_DAYS, numberOfDays);
        requestBody.put(Constants.RequestParameters.SENSOR_TYPES_BIT_MASK, sensorsBitMask);
        return requestBody;
    }

    @Autowired
    private RestTemplate restTemplate;
}