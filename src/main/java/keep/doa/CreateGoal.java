package main.java.keep.doa;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import main.java.keep.connections.MongoConnection;
import main.java.keep.domain.Goal;

import java.util.Calendar;

import static com.mongodb.client.model.Aggregates.limit;

/**
 * Created by suyog on 12/17/2016.
 */
public class CreateGoal {

    public CreateGoal(Goal goal, MongoConnection connection,int userId) {
        goal.setId(getLatestId(connection)+1);
        goal.setUserId(userId);
        // To-Do task
        if(goal.getTimeSpan()==1){
            goal.setStatus(1);
        }
        // Multiple days
        else{
            goal.setRemainingTime(goal.getRemainingTime(Calendar.getInstance().getTime()));
            goal.setSPI(1);
            goal.setStatus(1);
        }

        //goal.setUserId(connection.getCurrentUser().getId());
        Gson gson=new Gson();
        BasicDBObject object= (BasicDBObject)JSON.parse(gson.toJson(goal));
        connection.GoalDBCollection.insert(object);
    }

    public int getLatestId(MongoConnection connection){
        DBCursor cursor=connection.GoalDBCollection.find().sort(new BasicDBObject("_id",-1));
        if(cursor.hasNext()) {
            DBObject object = cursor.next();
            if (object.get("_id") != null)
                return (int) object.get("_id");
        }
        return 0;
    }
}
