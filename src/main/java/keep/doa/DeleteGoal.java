package main.java.keep.doa;

import com.mongodb.BasicDBObject;
import main.java.keep.connections.MongoConnection;

/**
 * Deletes the document from the collection using name and userId.
 * Created by suyog on 12/17/2016.
 */
public class DeleteGoal {
    public DeleteGoal(String name, MongoConnection connection, int userId) {
        BasicDBObject whereName=new BasicDBObject();
        whereName.put("name",name);
        whereName.put("userId",userId);
        connection.getGoalDBCollection().remove(whereName);
    }
}
