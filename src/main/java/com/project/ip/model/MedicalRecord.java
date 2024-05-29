package com.project.ip.model;

import java.util.List;

public class MedicalRecord
{
    public MedicalRecord() {}

    public MedicalRecord(final UserContext userContext_, List<Allergy> allergies_, List<Examination> examinations_)
    {
        userContext = userContext_;
        allergies = allergies_;
        examinations = examinations_;
    }

    public UserContext getUserContext()
    {
        return userContext;
    }

    public List<Allergy> getAllergies()
    {
        return allergies;
    }

    public List<Examination> getExaminations()
    {
        return examinations;
    }

    public void setExaminations(List<Examination> examinations)
    {
        this.examinations = examinations;
    }

    public void setAllergies(List<Allergy> allergies)
    {
        this.allergies = allergies;
    }

    public void setUserContext(UserContext userContext)
    {
        this.userContext = userContext;
    }

    private UserContext userContext;
    private List<Allergy> allergies;
    private List<Examination> examinations;
}