package hu.zfall.cleancode.munkaido.boundaries.dto;

public class WorkTimeSummary {

    private String workedTime;
    private String remainingTime;

    public WorkTimeSummary(String workedTime, String remainingTime) {
        this.workedTime = workedTime;
        this.remainingTime = remainingTime;
    }

    public String getWorkedTime() {
        return workedTime;
    }

    public void setWorkedTime(String workedTime) {
        this.workedTime = workedTime;
    }

    public String getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
    }

}
