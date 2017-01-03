package main.java.keep.doa;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import main.java.keep.connections.MongoConnection;
import main.java.keep.domain.Goal;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;

/**
 * Updates a goal.
 * Created by suyog on 12/17/2016.
 */
public class UpdateGoal {
    public UpdateGoal(Goal goal, MongoConnection connection,int userId) throws JSONException, IllegalAccessException, ParseException {

        ReadGoal readGoal=new ReadGoal();
        BasicDBObject whereName=new BasicDBObject();
        whereName.put("name",goal.getName());
        whereName.put("userId",userId);
        BasicDBObject updated=new BasicDBObject();
        Goal currentGoal=readGoal.readGoalByName(goal.getName(),connection,userId);
        Field[] fields=Goal.class.getDeclaredFields();

        for(int i=0;i<fields.length;i++){
            String name=fields[i].getName();
            fields[i].setAccessible(true);
            if(fields[i].get(goal)!=null && !fields[i].get(goal).equals(-1) && !fields[i].get(goal).equals(-1.0)){
                updated.put(name,fields[i].get(goal));
            }
            if(fields[i].get(goal)!=null && name.equals("currentValue")){
                Double v= (Double) fields[i].get(goal);
                HashMap<String,Date> m=new HashMap<>();
                m.put((Double.toString(v)).replace(".",","),Calendar.getInstance().getTime());
                if(currentGoal.getHistory()==null){
                    currentGoal.setHistory(m);
                    updated.put("history",m);
                }
                else{
                    currentGoal.getHistory().put((Double.toString(v)).replace(".",","),Calendar.getInstance().getTime());
                    updated.put("history",currentGoal.getHistory());
                }
            }
        }
        connection.getGoalDBCollection().update(whereName,new BasicDBObject("$set",updated));
    }
}
