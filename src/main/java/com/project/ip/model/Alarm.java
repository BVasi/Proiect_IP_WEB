package com.project.ip.model;

public class Alarm
{
    public Integer getId()
    {
        return id;
    }

    public AlarmType getAlarmType()
    {
        return alarmType;
    }

    public Boolean getIsResolved()
    {
        return isResolved;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public String getAdditionalText()
    {
        return additionalText;
    }

    public void setAdditionalText(String additionalText)
    {
        this.additionalText = additionalText;
    }

    public void setAlarmType(AlarmType alarmType)
    {
        this.alarmType = alarmType;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public void setIsResolved(Boolean resolved)
    {
        isResolved = resolved;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    private Integer id;
    private Integer userId;
    private Boolean isResolved;
    private String additionalText;
    private AlarmType alarmType;
}