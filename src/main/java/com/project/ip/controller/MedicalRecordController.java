package com.project.ip.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ip.constants.Constants;
import com.project.ip.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class MedicalRecordController
{
    @GetMapping(Constants.EndPoints.MEDICAL_RECORD + "/{userId}")
    public String getMedicalRecord(Model model, HttpSession session,
                                   @PathVariable Integer userId)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if (user == null)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.NOT_LOGGED_IN);
            return Constants.Redirects.LOGIN;
        }

        model.addAttribute(Constants.Attributes.EXAMINATION, new Examination());
        model.addAttribute(Constants.Attributes.MEDICAL_RECORD, getMedicalRecordFromRemoteServer(userId));
        model.addAttribute(Constants.Attributes.LOGGED_IN_USER_TYPE, user.getAccessType());
        return Constants.EndPoints.MEDICAL_RECORD;
    }

    @PostMapping(Constants.EndPoints.MEDICAL_RECORD + "/{userId}")
    public String postMedicalRecord(Model model, HttpSession session,
                                    @PathVariable Integer userId,
                                    @RequestBody Map<String, String> requestParameters)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if (user == null)
        {
            return Constants.Redirects.LOGIN;
        }
        if (user.getAccessType() == AccessType.PACIENT)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.NOT_STUFF_ERROR);
            return Constants.Redirects.LOGIN;
        }
        addExaminationToMedicalRecordOnRemoteServer(userId, new Examination(requestParameters));

        model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.Messages.MEDICAL_RECORD_UPDATE_SUCCESS);
        return Constants.Redirects.MEDICAL_RECORD + "/" + userId;
    }

    private MedicalRecord getMedicalRecordFromRemoteServer(final Integer userId)
    {
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                Constants.RemoteServerAddresses.CLOUD_SERVER + Constants.RemoteServerEndPoints.GET_MEDICAL_RECORD,
                HttpMethod.POST,
                new HttpEntity<>(Map.of(Constants.RequestParameters.ID, userId)),
                new ParameterizedTypeReference<>() {});

        return createMedicalRecordFromServerResponse(responseEntity.getBody());
    }

    private MedicalRecord createMedicalRecordFromServerResponse(final Map<String, Object> serverResponse)
    {
        User user = new User((Map<String, Object>)serverResponse.get(Constants.ParametersKeys.USER));
        List<Examination> examinations = (List<Examination>)
                serverResponse.get(Constants.ParametersKeys.EXAMINATIONS);
        List<Allergy> allergies = (List<Allergy>)
                serverResponse.get(Constants.ParametersKeys.ALLERGIES);
        return new MedicalRecord(user, allergies, examinations);
    }

    private void addExaminationToMedicalRecordOnRemoteServer(final Integer userId, final Examination examination)
    {
        try
        {
            ResponseEntity<Void> responseEntity = restTemplate.postForEntity(
                    Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.ADD_EXAMINATION,
                    Map.of(Constants.RequestParameters.ID, userId,
                            Constants.RequestParameters.EXAMINATION, examination),
                    void.class
            );
        } catch (HttpClientErrorException.Conflict error)
        {
            System.out.println(Constants.Logs.ADD_EXAMINATION_ERROR);
        }
    }

    @Autowired
    private RestTemplate restTemplate;
}