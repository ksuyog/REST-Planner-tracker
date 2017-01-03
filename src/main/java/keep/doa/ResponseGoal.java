package main.java.keep.doa;

import main.java.keep.domain.Activity;
import main.java.keep.domain.Goal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by suyog on 12/29/2016.
 */
public class ResponseGoal {
    private int _id;
    //private int userId;
    private String name;
    private String area;
    private int timeSpan; // 1=single day 2=multiple days
    private String startDate;
    private String endDate;
    private Double startValue;
    private Double currentValue;
    private Double endValue;
    private int priority; // 1= low  2= medium 3= high
    private List<Activity> activities;
    private String description;
    private int status; // 1= open 2= done
    private HashMap<String,String> history;
    private int remainingTime;
    //Schedule Performance Index 0=lagging 1=on time 2=before time
    private int SPI;
    private Double percentComplete;
    private Double difference;

    public Double getDifference() {
        return difference;
    }

    public void setDifference(Double difference) {
        this.difference = difference;
    }

    public Double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(Double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public ResponseGoal(Goal goal){
        _id=goal.get_id();
        name=goal.getName();
        area=goal.getArea();
        description=goal.getDescription();
        priority=goal.getPriority();
        status=goal.getStatus();
        timeSpan=goal.getTimeSpan();
        if(timeSpan==3){
            setStartDate(goal.getStartDate());
            setEndDate(goal.getEndDate());
            setStartValue(goal.getStartValue());
            setEndValue(goal.getEndValue());
            setCurrentValue(goal.getCurrentValue());
            setHistory(goal.getHistory());
            setRemainingTime(goal.getRemainingTime());
            setSPI(goal.getSPI());
            setActivities(goal.getActivities());
            setPercentComplete(goal.getPercentComplete());
            setDifference(goal.getDifference());
        }
    }
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(int timeSpan) {
        this.timeSpan = timeSpan;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = convertToString(startDate);
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = convertToString(endDate);
    }

    public Double getStartValue() {
        return startValue;
    }

    public void setStartValue(Double startValue) {
        this.startValue = startValue;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }

    public Double getEndValue() {
        return endValue;
    }

    public void setEndValue(Double endValue) {
        this.endValue = endValue;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, String> getHistory() {
        return history;
    }

    public void setHistory(HashMap<String, Date> history) {
        HashMap<String,String> newMap=new HashMap<>();
        if(history==null){ this.history=null;}
        else{
            for(String item:history.keySet()){
                Date date=history.get(item);
                String dateString=convertToString(date);
                newMap.put(item,dateString);
            }
            this.history = newMap;
        }
    }

    private String convertToString(Date date) {
        String dateString;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM/dd/yyyy");
        return dateString=simpleDateFormat.format(date);
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getSPI() {
        return SPI;
    }

    public void setSPI(int SPI) {
        this.SPI = SPI;
    }

}
