package com.project.ip.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Message
{
    public Message() {}

    public Message(final Integer sendingUserId_, final String content_)
    {
        sendingUserId = sendingUserId_;
        content = content_;
        sendingDate = null;
    }

    public Integer getSendingUserId()
    {
        return sendingUserId;
    }

    public LocalDateTime getSendingDate()
    {
        return sendingDate;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setSendingDate(String dateToConvert)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.ENGLISH);
        this.sendingDate = LocalDateTime.parse(dateToConvert, formatter);
    }

    public void setSendingUserId(Integer sendingUserId)
    {
        this.sendingUserId = sendingUserId;
    }

    private Integer sendingUserId;
    private String content;
    private LocalDateTime sendingDate;
    private final static String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
}