package com.project.ip.model;

import java.util.Map;

public class User implements UserContext
{
    public User() {}

    public User(Map<String, Object> userData)
    {
        id = (Integer)userData.get("id");
        age = (Integer)userData.get("age");
        city = (String)userData.get("city");
        country = (String)userData.get("country");
        county = (String)userData.get("county");
        street = (String)userData.get("street");
        cnp = (String)userData.get("cnp");
        emailAddress = (String)userData.get("emailAddress");
        firstName = (String)userData.get("firstName");
        lastName = (String)userData.get("lastName");
        password = (String)userData.get("password");
        phoneNumber = (String)userData.get("phoneNumber");
        profession = (String)userData.get("profession");
        workplace = (String)userData.get("workplace");
        accessType = AccessType.valueOf((String)userData.get("accessType"));
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public AccessType getAccessType()
    {
        return accessType;
    }

    @Override
    public Integer getAge()
    {
        return age;
    }

    @Override
    public String getCity()
    {
        return city;
    }

    @Override
    public String getCnp()
    {
        return cnp;
    }

    @Override
    public String getCountry()
    {
        return country;
    }

    @Override
    public String getCounty()
    {
        return county;
    }

    @Override
    public String getEmailAddress()
    {
        return emailAddress;
    }

    @Override
    public String getFirstName()
    {
        return firstName;
    }
    @Override
    public String getLastName()
    {
        return lastName;
    }

    @Override
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    @Override
    public String getProfession()
    {
        return profession;
    }

    @Override
    public String getWorkplace()
    {
        return workplace;
    }

    @Override
    public String getStreet()
    {
        return street;
    }

    public void setAccessType(AccessType accessType)
    {
        this.accessType = accessType;
    }

    public void setAge(Integer age)
    {
        this.age = age;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setCnp(String cnp)
    {
        this.cnp = cnp;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public void setCounty(String county)
    {
        this.county = county;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public void setProfession(String profession)
    {
        this.profession = profession;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public void setWorkplace(String workplace)
    {
        this.workplace = workplace;
    }

    private Integer id;
    private String firstName;
    private String lastName;
    private String cnp;
    private Integer age;
    private String street;
    private String city;
    private String county;
    private String country;
    private String phoneNumber;
    private String emailAddress;
    private String profession;
    private String workplace;
    private String password;
    private AccessType accessType;
}