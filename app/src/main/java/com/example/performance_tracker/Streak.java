package com.example.performance_tracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Streak {
    private Integer highPriorityStreak;
    private Integer mediumPriorityStreak;
    private Integer lowPriorityStreak;
    private String lastUpdatedDate;
    private Integer pastTotalStreak;

    static public boolean warningFlag = false;

    private Streak(){
        throw new AssertionError("Can't Create a Streak without the Date- Sorry!");
    }
    public Streak(String currentDate){
        if (!this.lastUpdatedDate.toString().isEmpty()) {
            this.setLastUpdatedDate(currentDate);
            this.setHighPriorityStreak(0);
            this.setMediumPriorityStreak(0);
            this.setLowPriorityStreak(0);
        } else {
            throw new InstantiationError("Streak class has already been instantiated- use the available method");
        }
    }

    public Streak (Streak previousStreak, String currentDate){
        this.setHighPriorityStreak(previousStreak.getHighPriorityStreak());
        this.setMediumPriorityStreak(previousStreak.getMediumPriorityStreak());
        this.setLowPriorityStreak(previousStreak.getLowPriorityStreak());
        this.setLastUpdatedDate(currentDate);
    }

    public void clearStreak(){
        this.setHighPriorityStreak(0);
        this.setMediumPriorityStreak(0);
        this.setLowPriorityStreak(0);
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss", Locale.getDefault());
        String currentDateStringFormat = currentDate.format(new Date());
        this.setLastUpdatedDate(currentDateStringFormat);
        warningFlag = false;
    }

    public Integer totalStreak(){
        return (this.getHighPriorityStreak()+this.getMediumPriorityStreak()+this.getLowPriorityStreak());
    }

    public void addHighStreak(){
        this.setHighPriorityStreak(3);
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss", Locale.getDefault());
        String currentDateStringFormat = currentDate.format(new Date());
        this.setLastUpdatedDate(currentDateStringFormat);
    }
    public void addMediumStreak(){
        this.setMediumPriorityStreak(2);
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss", Locale.getDefault());
        String currentDateStringFormat = currentDate.format(new Date());
        this.setLastUpdatedDate(currentDateStringFormat);
    }
    public void addLowStreak(){
        this.setLowPriorityStreak(1);
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss", Locale.getDefault());
        String currentDateStringFormat = currentDate.format(new Date());
        this.setLastUpdatedDate(currentDateStringFormat);
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    private void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Integer getLowPriorityStreak() {
        return lowPriorityStreak;
    }

    private void setLowPriorityStreak(Integer lowPriorityStreak) {
        this.lowPriorityStreak = lowPriorityStreak;
    }

    public Integer getMediumPriorityStreak() {
        return mediumPriorityStreak;
    }

    private void setMediumPriorityStreak(Integer mediumPriorityStreak) {
        this.mediumPriorityStreak = mediumPriorityStreak;
    }

    public Integer getHighPriorityStreak() {
        return highPriorityStreak;
    }

    private void setHighPriorityStreak(Integer highPriorityStreak) {
        this.highPriorityStreak = highPriorityStreak;
    }
}