package main.java.keep.doa;

import com.google.gson.Gson;
import com.mongodb.*;
import main.java.keep.connections.MongoConnection;
import main.java.keep.domain.Activity;
import main.java.keep.domain.Goal;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Reads the Goals from the collection.
 * Created by suyog on 12/17/2016.
 */
public class ReadGoal {

    /**
     * Reads the goal using goal name and userId.
     * @param name
     * @param connection
     * @param userId
     * @return Goal
     * @throws ParseException
     */
    public Goal readGoalByName(String name, MongoConnection connection,int userId) throws ParseException {

        BasicDBObject whereName=new BasicDBObject();
        whereName.put("name",name);
        whereName.put("userId",userId);
        DBCursor cursor=connection.getGoalDBCollection().find(whereName);
        DBObject object=cursor.next();
        Goal goal=convertToGoal(object);
        if(goal.getTimeSpan()==3){
            goal.setRemainingTime(goal.getRemainingTime(goal.getEndDate()));
            goal.setSPI(goal.getSPI());
        }
        return goal;
    }

    /**
     * Reads the goal using goal Id and userId.
     * @param id
     * @param connection
     * @param userId
     * @return Goal
     */
    public Goal readGoalById(int id, MongoConnection connection,int userId) {

        BasicDBObject whereId=new BasicDBObject();
        whereId.put("_id",id);
        whereId.put("userId",userId);
        DBCursor cursor=connection.getGoalDBCollection().find(whereId);
        DBObject object=cursor.next();

        Goal goal = (new Gson()).fromJson(object.toString(), Goal.class);
        if(goal.getTimeSpan()==2){
            goal.setRemainingTime(goal.getRemainingTime(goal.getEndDate()));
            goal.setSPI(goal.getSPI());
        }
        return goal;
    }

    /**
     * Reads all goals for a userId.
     * @param connection
     * @param userId
     * @return List of goals
     * @throws ParseException
     */
    public List<Goal> readAllGoals(MongoConnection connection,int userId) throws ParseException {
        List<Goal> list=new ArrayList<Goal>();
        BasicDBObject whereUserId=new BasicDBObject();
        whereUserId.put("userId",userId);
        DBCursor cursor=connection.GoalDBCollection.find(whereUserId);
        while(cursor.hasNext()){
            DBObject object=cursor.next();
            Goal goal=convertToGoal(object);

            if(goal.getTimeSpan()==3){
                goal.setRemainingTime(goal.getRemainingTime(goal.getEndDate()));
                goal.setSPI(goal.getSPI());
            }
            list.add(goal);
        }
        return list;
    }

    /**
     * Converts DBObject returned by MongoDB to Goal object.
     * @param object
     * @return Goal
     * @throws ParseException
     */
    public Goal convertToGoal(DBObject object) throws ParseException {
        BasicDBObject basicDBObject= (BasicDBObject) object;
        Goal goal=new Goal();
        goal.setName((String) basicDBObject.get("name"));
        goal.setArea((String) basicDBObject.get("area"));
        goal.setTimeSpan((int) basicDBObject.get("timeSpan"));
        goal.setStatus((int) basicDBObject.get("status"));
        goal.setDescription((String) basicDBObject.get("description"));
        if(goal.getTimeSpan()==3){
            goal.setStartDate(convertToDate((String) basicDBObject.get("startDate")));
            goal.setEndDate(convertToDate((String) basicDBObject.get("endDate")));
            goal.setStartValue((Double)basicDBObject.get("startValue"));
            goal.setEndValue((Double)basicDBObject.get("endValue"));
            goal.setCurrentValue((Double)basicDBObject.get("currentValue"));
            if(basicDBObject.get("activities")!=null){
                goal.setActivities((List<Activity>) basicDBObject.get("activities"));
            }
            if(basicDBObject.get("history")!=null){
                goal.setHistory((HashMap<String, Date>) basicDBObject.get("history"));
            }
        }
        return goal;
    }

    /**
     * Converts date returned by MongoDB to Java.Util.Date format.
     * @param mongoString
     * @return Date
     * @throws ParseException
     */
    public Date convertToDate(String mongoString) throws ParseException {
        String[] array=mongoString.split(" ");
        // Dec 19, 2016 7:00:00 PM
        Date date=new SimpleDateFormat("MMMM d yyyy", Locale.US).parse(array[0]+" "+array[1].substring(0,array[1].lastIndexOf(","))+" "+array[2]);
        return date;
    }
}
