package main.java.keep.doa;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import main.java.keep.connections.MongoConnection;
import main.java.keep.domain.Goal;

import java.util.Calendar;

import static com.mongodb.client.model.Aggregates.limit;

/**
 * Creates a single day or multiple day task and stores to the database.
 * Created by suyog on 12/17/2016.
 */
public class CreateGoal {
    public CreateGoal(Goal goal, MongoConnection connection,int userId) {
        goal.setId(getLatestId(connection)+1);
        goal.setUserId(userId);
        // single day task
        if(goal.getTimeSpan()==1){
            goal.setStatus(1);
        }
        // Multiple days
        else{
            goal.setRemainingTime(goal.getRemainingTime(Calendar.getInstance().getTime()));
            goal.setSPI(1);
            goal.setStatus(1);
        }

        Gson gson=new Gson();
        BasicDBObject object= (BasicDBObject)JSON.parse(gson.toJson(goal));
        connection.GoalDBCollection.insert(object);
    }

    /**
     * Returns the ID of the recent document entry in the collection. Returns 0 for empty collection.
     * @param connection
     * @return Latest Id in the DB
     */
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
