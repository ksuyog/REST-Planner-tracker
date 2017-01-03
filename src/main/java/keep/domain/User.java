package main.java.keep.domain;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.connection.Connection;
import com.mongodb.util.JSON;
import main.java.keep.connections.MongoConnection;
import sun.security.util.Password;

/**
 * Created by suyog on 12/17/2016.
 */
public class User {
    public int _id;
    public String name;
    public String secret;
    public Boolean loggedIn;

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void createNewUser(MongoConnection connection, String token){
        setId(getLatestId(connection)+1);
        String[] array=token.split(" ");
        setName(array[0]);
        setSecret(array[1]);
        setLoggedIn(true);
        Gson gson=new Gson();
        BasicDBObject object= (BasicDBObject) JSON.parse(gson.toJson(this));
        connection.UserDBCollection.insert(object);
    }

    public int getLatestId(MongoConnection connection){
        DBCursor cursor=connection.UserDBCollection.find().sort(new BasicDBObject("_id",-1));
        if(cursor.hasNext()) {
            DBObject object = cursor.next();
            if (object.get("_id") != null)
                return (int) object.get("_id");
        }
        return 0;
    }

    public void login(String token){
        String[] array=token.split(" ");
        // authenticate whether the user is using the authorised secret by recalculating the signature.
        // signature might be HMAC(POST + name + secret)
        // if valid user then
        MongoConnection connection=new MongoConnection();
        connection.connectToUserDB();
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("loggedIn", "true"));

        BasicDBObject searchQuery = new BasicDBObject().append("name", array[0]);
        connection.getUserDBCollection().update(searchQuery,newDocument);
    }

    public void logout(String token){
        String[] array=token.split(" ");
        // authenticate whether the user is using the authorised secret by recalculating the signature.
        // signature might be HMAC(POST + name + secret)
        // if valid user then
        MongoConnection connection=new MongoConnection();
        connection.connectToUserDB();
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("loggedIn", "false"));

        BasicDBObject searchQuery = new BasicDBObject().append("name", array[0]);
        connection.getUserDBCollection().update(searchQuery,newDocument);
    }

    public boolean isAuthorized(MongoConnection connection,String token){
        Boolean authorized=false;
        String[] array=token.split(" ");
        BasicDBObject object=new BasicDBObject();
        object.put("name",array[0]);
        DBCursor cursor=connection.UserDBCollection.find(object);
        DBObject dbObject=cursor.next();
        if(dbObject==null){ authorized=false;}
        else{
            User user=(new Gson()).fromJson(dbObject.toString(),User.class);
            _id=user.getId();
            if(user.getLoggedIn())
                authorized=true;
        }
        return authorized;
    }
}
