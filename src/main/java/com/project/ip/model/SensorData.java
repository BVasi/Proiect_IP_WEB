package com.project.ip.model;

import java.time.LocalDateTime;

public class SensorData
{
    public SensorData() {}

    public SensorData(final LocalDateTime date_, final Double data_)
    {
        date = date_;
        data = data_;
    }

    public Double getData()
    {
        return data;
    }

    public LocalDateTime getDate()
    {
        return date;
    }

    public void setData(Double data)
    {
        this.data = data;
    }

    public void setDate(LocalDateTime date)
    {
        this.date = date;
    }

    private Double data;
    private LocalDateTime date;
}