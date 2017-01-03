package main.java.keep.connections;

import com.mongodb.*;
import com.mongodb.DB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import main.java.keep.domain.User;
import org.bson.Document;

/**
 * Created by suyog on 12/18/2016.
 */
public class MongoConnection {
    public MongoClient mongoClient;
    public DB mongoDatabase;
    public DBCollection UserDBCollection;
    public DBCollection GoalDBCollection;

    public DBCollection getUserDBCollection() {
        return UserDBCollection;
    }

    public void setUserDBCollection(DBCollection userDBCollection) {
        UserDBCollection = userDBCollection;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void setMongoDatabase(DB mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    public DBCollection getGoalDBCollection() {
        return GoalDBCollection;
    }

    public void setGoalDBCollection(DBCollection GoalDBCollection) {
        this.GoalDBCollection = GoalDBCollection;
    }

    public void connectToGoalDB(){
        this.mongoClient= new MongoClient("localhost",27017);
        this.mongoDatabase=mongoClient.getDB("keepdb");
        this.GoalDBCollection= mongoDatabase.getCollection("goal");
    }

    public void connectToUserDB(){
        this.mongoClient= new MongoClient("localhost",27017);
        this.mongoDatabase=mongoClient.getDB("keepdb");
        this.UserDBCollection= mongoDatabase.getCollection("user");
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public DB getMongoDatabase() {
        return mongoDatabase;
    }
}
