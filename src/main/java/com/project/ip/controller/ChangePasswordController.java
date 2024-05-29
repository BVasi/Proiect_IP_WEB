package com.project.ip.controller;

import com.project.ip.constants.Constants;
import com.project.ip.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ChangePasswordController
{
    @GetMapping(Constants.EndPoints.GET_RESET_EMAIL)
    public String getResetEmail()
    {
        return Constants.EndPoints.GET_RESET_EMAIL;
    }

    @PostMapping(Constants.EndPoints.GET_RESET_EMAIL)
    public String postResetEmail(Model model, HttpSession session,
                                 @RequestParam(Constants.RequestParameters.RESET_EMAIL) String resetEmail)
    {
        session.setAttribute(Constants.Attributes.PASSWORD_RESETER_EMAIL, resetEmail);
        try
        {
            ResponseEntity<Void> response = restTemplate.postForEntity(Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.RESET_PASSWORD_EMAIL,
                    Map.of(Constants.ParametersKeys.EMAIL_ADDRESS, resetEmail), Void.class);
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.Messages.CHECK_EMAIL);
            return Constants.Redirects.CONFIRMATION_CODE;
        }
        catch (HttpClientErrorException.NotFound exception)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.CHECK_EMAIL_ERROR);
            return Constants.EndPoints.GET_RESET_EMAIL;
        }
    }

    @GetMapping(Constants.EndPoints.CONFIRMATION_CODE)
    public String getConfirmationCode(Model model, HttpSession session)
    {
        String resetEmail = (String)session.getAttribute(Constants.Attributes.PASSWORD_RESETER_EMAIL);
        if (resetEmail == null)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.CONFIRMATION_CODE_ERROR);
            return Constants.Redirects.GET_RESET_EMAIL;
        }

        return Constants.EndPoints.CONFIRMATION_CODE;
    }

    @PostMapping(Constants.EndPoints.CONFIRMATION_CODE)
    public String postConfirmationCode(Model model, HttpSession session,
                                 @RequestParam(Constants.RequestParameters.CONFIRMATION_CODE) String confirmationCode)
    {
        String resetEmail = (String)session.getAttribute(Constants.Attributes.PASSWORD_RESETER_EMAIL);
        try
        {
            ResponseEntity<Void> response = restTemplate.postForEntity(Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.CHECK_CONFIRMATION_CODE,
                    generateRequestBodyForCodeChecking(resetEmail, confirmationCode), Void.class);
            session.setAttribute(Constants.Attributes.CAN_CHANGE_PASSWORD, true);
            return Constants.Redirects.RESET_PASSWORD;
        } catch (HttpClientErrorException.NotFound exception)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.WRONG_ERROR_CODE);
            return Constants.EndPoints.CONFIRMATION_CODE;
        }
    }

    @GetMapping(Constants.EndPoints.RESET_PASSWORD)
    public String getResetPassword(Model model, HttpSession session)
    {
        String resetEmail = (String)session.getAttribute(Constants.Attributes.PASSWORD_RESETER_EMAIL);
        Boolean canChangePassword = (Boolean)session.getAttribute(Constants.Attributes.CAN_CHANGE_PASSWORD);
        if (resetEmail == null || canChangePassword == null
                || !canChangePassword)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.RESET_PASSWORD_ERROR);
            return Constants.Redirects.GET_RESET_EMAIL;
        }

        return Constants.EndPoints.RESET_PASSWORD;
    }

    @PostMapping(Constants.EndPoints.RESET_PASSWORD)
    public String postResetPassword(Model model, HttpSession session,
                                       @RequestParam(Constants.RequestParameters.NEW_PASSWORD) String newPassword)
    {
        String resetEmail = (String)session.getAttribute(Constants.Attributes.PASSWORD_RESETER_EMAIL);
        try
        {
            ResponseEntity<Void> response = restTemplate.postForEntity(Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.CHANGE_PASSWORD,
                    generateRequestBodyForChangingThePassword(resetEmail, newPassword), Void.class);
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.Messages.RESET_PASSWORD_SUCCESS);
            session.invalidate();
            return Constants.Redirects.LOGIN;
        } catch (HttpClientErrorException.NotFound exception)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.CHANGE_PASSWORD_ERROR);
            return Constants.EndPoints.RESET_PASSWORD;
        }
    }

    @GetMapping(Constants.EndPoints.CHANGE_PASSWORD)
    public String getChangePassword()
    {
        return Constants.EndPoints.SIGN_UP;
    }

    @PostMapping(Constants.EndPoints.CHANGE_PASSWORD)
    public String postChangePassword(Model model, HttpSession session,
            @RequestParam(Constants.RequestParameters.NEW_PASSWORD) String newPassword)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        try
        {
            ResponseEntity<Void> response = restTemplate.postForEntity(Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.CHANGE_PASSWORD,
                    generateRequestBodyForChangingThePassword(user.getEmailAddress(), newPassword), Void.class);
            session.invalidate();
            return Constants.Redirects.LOGIN;
        } catch (HttpClientErrorException.NotFound exception)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.CHANGE_PASSWORD_ERROR);
            return Constants.EndPoints.CHANGE_PASSWORD;
        }
    }

    private Map<String, String> generateRequestBodyForCodeChecking(final String resetEmail, final String confirmationCode)
    {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(Constants.ParametersKeys.EMAIL_ADDRESS, resetEmail);
        requestBody.put(Constants.ParametersKeys.CONFIRMATION_CODE, confirmationCode);
        return requestBody;
    }

    private Map<String, String> generateRequestBodyForChangingThePassword(final String resetEmail, final String newPassword)
    {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(Constants.ParametersKeys.EMAIL_ADDRESS, resetEmail);
        requestBody.put(Constants.ParametersKeys.NEW_PASSWORD, newPassword);
        return requestBody;
    }

    @Autowired
    private RestTemplate restTemplate;
}