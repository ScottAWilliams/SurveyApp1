package com.nonscirenefas.yeshy.surveyapp1;

/**
 * Created by Yeshy on 8/19/2016.
 */
public class Medication {

    private String name;
    private String frequency;
    private String daysSupply;

    public Medication() {
        //default constructor because why not
    }

    public Medication(String name, String frequency, String daysSupply) {
        this.name = name;
        this.frequency = frequency;
        this.daysSupply = daysSupply;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDaysSupply() {
        return daysSupply;
    }

    public void setDaysSupply(String daysSupply) {
        this.daysSupply = daysSupply;
    }
}
