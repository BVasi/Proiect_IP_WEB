package com.project.ip.model;

import com.project.ip.constants.Constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class Examination
{
    public Examination() {}

    public Examination(final Map<String, String> examinationMap)
    {
        setExaminationDate(examinationMap.get(Constants.RequestParameters.EXAMINATION_DATE));
        diagnostic = examinationMap.get(Constants.RequestParameters.DIAGNOSTIC);
        cure = examinationMap.get(Constants.RequestParameters.CURE);
        recomandation = examinationMap.get(Constants.RequestParameters.RECOMANDATION);
    }

    public LocalDate getExaminationDate()
    {
        return examinationDate;
    }

    public String getCure()
    {
        return cure;
    }

    public String getDiagnostic()
    {
        return diagnostic;
    }

    public String getRecomandation()
    {
        return recomandation;
    }

    public void setCure(String cure)
    {
        this.cure = cure;
    }

    public void setDiagnostic(String diagnostic)
    {
        this.diagnostic = diagnostic;
    }

    public void setExaminationDate(String dateToConvert)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.ENGLISH);
        examinationDate = LocalDate.parse(dateToConvert, formatter);
    }

    public void setRecomandation(String recomandation)
    {
        this.recomandation = recomandation;
    }

    private LocalDate examinationDate;
    private String diagnostic;
    private String cure;
    private String recomandation;
    private final static String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
}