package com.project.ip.controller;

import com.project.ip.constants.Constants;
import com.project.ip.model.AccessType;
import com.project.ip.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class AssignPacientToMedicController
{
    @PostMapping(Constants.EndPoints.ASSIGN_PACIENT_TO_MEDIC)
    public String postAssignPacientToMedic(HttpSession session,
                                           @RequestBody Map<String, String> requestParameters)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if ((user == null) || (user.getAccessType() != AccessType.MEDIC))
        {
            return Constants.Redirects.LOGIN;
        }
        assignPacientToMedicOnRemoteServer(user.getId(),
                Integer.parseInt(requestParameters.get(Constants.RequestParameters.ID)));

        return Constants.Redirects.CHAT + "/" + requestParameters.get(Constants.RequestParameters.CHAT_ID);
    }

    private void assignPacientToMedicOnRemoteServer(final Integer medicId, final Integer pacientId)
    {
        try
        {
            ResponseEntity<Void> response = restTemplate.postForEntity(Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.ADD_PACIENT_TO_MEDIC,
                    Map.of(Constants.RequestParameters.ID, medicId,
                            Constants.RequestParameters.PACIENT_ID, pacientId),
                    Void.class);
        } catch (HttpClientErrorException.Conflict error)
        {
            System.out.println("NOT OK JERRY X2");
        }
    }

    @Autowired
    private RestTemplate restTemplate;
}