package com.project.ip.model;

public enum SensorType
{
    BLOOD_PRESSURE(0x1),
    PULSE(0x2),
    BODY_TEMPERATURE(0x4),
    WEIGHT(0x8),
    GLUCOSE(0x10),
    LIGHT(0x20),
    ROOM_TEMPERATURE(0x40),
    IS_GAS_PRESENT(0x80),
    HUMIDITY(0x100),
    IS_IN_PROXIMITY(0x200);

    private final Short bitMask;

    SensorType(Integer bitMask_)
    {
        if (bitMask_ < 0 || bitMask_ > Short.MAX_VALUE)
        {
            throw new IllegalArgumentException("Value out of range");
        }
        bitMask = bitMask_.shortValue();
    }

    public static Integer combineMasks(SensorType... sensorTypes)
    {
        Integer combinedMask  = 0;
        for (SensorType sensorType : sensorTypes)
        {
            combinedMask |= sensorType.getBitMask();
        }
        return combinedMask;
    }

    public Short getBitMask()
    {
        return bitMask;
    }
}
