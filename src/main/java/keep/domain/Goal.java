package main.java.keep.domain;

import java.sql.Time;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by suyog on 12/17/2016.
 */
public class Goal {
    private int _id;
    private int userId;
    private String name;
    private String area;
    private int timeSpan; // 1=single day 2=multiple days
    private Date startDate;
    private Date endDate;
    private Double startValue;
    private Double currentValue;
    private Double endValue;
    private int priority; // 1= low  2= medium 3= high
    private List<Activity> activities;
    private String description;
    private int status; // 1= open 2= done
    private HashMap<String,Date> history;
    private int remainingTime;
    private int SPI; //Schedule Performance Index 0=lagging 1=on time 2=before time
    private Double difference; // No of days lagging or ahead

    public Double getDifference() {
        return difference;
    }

    public void setDifference(Double difference) {
        this.difference = difference;
    }

    public Goal(){
        _id= -1;
        userId=-1;
        name=null;
        area=null;
        timeSpan= -1;
        startDate=null;
        endDate=null;
        startValue=-1.0;
        currentValue=-1.0;
        endValue=-1.0;
        priority=-1;
        activities=null;
        description=null;
        status=-1;
        history=null;
        remainingTime=-1;
        SPI=-1;
        difference=-1.0;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, Date> getHistory() {
        return history;
    }

    public void setHistory(HashMap<String, Date> history) {
        this.history = history;
    }

    public void setId(int id) {
        this._id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setTimeSpan(int timeSpan) {
        this.timeSpan = timeSpan;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getArea() {
        return area;
    }

    public int getTimeSpan() {
        return timeSpan;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getPriority() {
        return priority;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public String getDescription() {
        return description;
    }

    public Double getStartValue() { return startValue;}

    public void setStartValue(Double startValue) { this.startValue = startValue;}

    public Double getCurrentValue() { return currentValue;}

    public void setCurrentValue(Double currentValue) { this.currentValue = currentValue;}

    public Double getEndValue() { return endValue;}

    public void setEndValue(Double endValue) { this.endValue = endValue;}

    public int getRemainingTime() { return remainingTime;}

    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime;}

    public void setSPI(int SPI) { this.SPI = SPI;}

    /**
     * Returns remaining days for a goal from current day.
     * @param endDate
     * @return remaining days
     */
    public int getRemainingTime(Date endDate) {
        LocalDate today = LocalDate.now();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(endDate);
        LocalDate end = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        this.remainingTime=(int) ChronoUnit.DAYS.between(today,end);
        return remainingTime;
    }

    /**
     * Returns total days remaining from a start and end date.
     * @param startDate
     * @param endDate
     * @return Total days remaining
     */
    public int getRemainingTime(Date startDate,Date endDate){
        Calendar start=new GregorianCalendar();
        start.setTime(startDate);
        Calendar end=new GregorianCalendar();
        end.setTime(endDate);
        LocalDate startLocal = LocalDate.of(start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, start.get(Calendar.DAY_OF_MONTH));
        LocalDate endLocal = LocalDate.of(end.get(Calendar.YEAR), end.get(Calendar.MONTH) + 1, end.get(Calendar.DAY_OF_MONTH));
        //Period period = Period.between(startLocal,endLocal);
        return (int) ChronoUnit.DAYS.between(startLocal,endLocal);
        //return period.getDays();

    }

    /**
     * Returns the performance index.
     * SPI=0 if lagging
     * SPI=1 if ahead of schedule
     * SPI=2 if on time
     * @return performance index
     */
    public int getSPI(){
        int increasing=0;
        int totalDays=getRemainingTime(startDate,endDate);
        int daysSpent=0;
        Double expectedRate=0.0;
        Double currentRate=0.0;
        Date currentDate=new Date();
        currentDate=Calendar.getInstance().getTime();
        if(startValue<endValue)
            increasing=1;
        else
            increasing=0;

        if(increasing==1){
            expectedRate=(endValue-startValue)/totalDays;
            currentRate=(currentValue-startValue)/getRemainingTime(startDate,currentDate);
            if(expectedRate>currentRate){
                SPI=0;
                daysSpent=getRemainingTime(startDate,currentDate);
                difference=(expectedRate*daysSpent)-(currentRate*daysSpent);
            }
            else if(expectedRate<currentRate){
                SPI=2;
                daysSpent=getRemainingTime(startDate,currentDate);
                difference=(currentRate*daysSpent)-(expectedRate*daysSpent);
            }
            else if(expectedRate==currentRate){
                SPI=1;
                difference=0.0;
            }
        }
        else{
            expectedRate=(startValue-endValue)/totalDays;
            currentRate=(startValue-currentValue)/getRemainingTime(startDate,currentDate);
            if(expectedRate>currentRate){
                SPI=0;
                daysSpent=getRemainingTime(startDate,currentDate);
                difference=(expectedRate*daysSpent)-(currentRate*daysSpent);
            }
            else if(expectedRate<currentRate){
                SPI=2;
                daysSpent=getRemainingTime(startDate,currentDate);
                difference=(currentRate*daysSpent)-(expectedRate*daysSpent);
            }
            else if(expectedRate==currentRate){
                SPI=1;
                difference=0.0;
            }
        }
        return SPI;
    }

    /**
     * Calculates the % complete.
     * @return Percent complete
     */
    public Double getPercentComplete(){
        Double percent=0.0;
        int increasing=0;
        if(startValue<endValue)
            increasing=1;
        else
            increasing=0;
        if(increasing==1){
            percent=((currentValue-startValue)/(endValue-startValue))*100;
        }
        else{
            percent=((startValue-currentValue)/(startValue-endValue))*100;
        }
        return percent;
    }
}
