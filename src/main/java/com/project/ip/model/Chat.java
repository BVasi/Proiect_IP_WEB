package com.project.ip.model;

import java.util.List;

public class Chat
{
    public Integer getId()
    {
        return id;
    }

    public UserContext getOtherUser()
    {
        return otherUser;
    }

    public List<Message> getMessages()
    {
        return messages;
    }

    public void setMessages(List<Message> messages)
    {
        this.messages = messages;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setOtherUser(User otherUser)
    {
        this.otherUser = otherUser;
    }

    private Integer id;
    private UserContext otherUser;
    private List<Message> messages;
}