package main.java.keep.service;

import main.java.keep.doa.*;
import main.java.keep.domain.User;
import org.json.JSONException;

import main.java.keep.connections.MongoConnection;
import main.java.keep.domain.Goal;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suyog on 12/17/2016.
 */
@Path("/keep")
public class WebServices {

    @GET
    @Path("/newuser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(@HeaderParam("Authorization") String authenticationToken){
        MongoConnection connection=new MongoConnection();
        connection.connectToUserDB();
        User user= new User();
        user.createNewUser(connection,authenticationToken);
        return Response.status(200).entity(user.getName()).build();
    }

    @GET
    @Path("/login")
    public Response login(@HeaderParam("Authorization") String authenticationToken ){
        MongoConnection connection=new MongoConnection();
        connection.connectToUserDB();
        User user=new User();
        user.login(authenticationToken);
        return Response.status(200).entity("Logged In").build();
    }

    @POST
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") String authenticationToken){
        User user=new User();
        user.logout(authenticationToken);
        return Response.status(200).entity("Logged out").build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGoal(Goal goal,@HeaderParam("Authorization") String authenticationToken){

        MongoConnection connection=new MongoConnection();
        connection.connectToUserDB();
        connection.connectToGoalDB();
        User user=new User();
        int returnStatus=0;
        Object returnObject = null;
        if(user.isAuthorized(connection,authenticationToken)){
            CreateGoal createGoal=new CreateGoal(goal,connection,user._id);
            ResponseGoal responseGoal=new ResponseGoal(goal);
            returnStatus=200;
            returnObject=responseGoal;
        }
        else{
            returnStatus=403;
            returnObject=new String("Unauthorized access.");
        }
        return Response.status(returnStatus).entity(returnObject).build();
    }

    @GET
    @Path("/read/{name}")
    @Produces("application/json")
    public Response readGoals(@PathParam("name") String name, @HeaderParam("Authorization") String authenticationToken){

        MongoConnection connection=new MongoConnection();
        connection.connectToUserDB();
        connection.connectToGoalDB();
        User user=new User();
        int returnStatus=0;
        Object returnObject = null;
        if(user.isAuthorized(connection,authenticationToken)){
            ReadGoal readGoal=new ReadGoal();
            try {
                Goal goal= null;
                goal = readGoal.readGoalByName(name,connection,user.getId());
                ResponseGoal responseGoal=new ResponseGoal(goal);
                returnStatus=200;
                returnObject=responseGoal;
            } catch (ParseException e) {
                returnStatus=500;
                returnObject=new String("Internal Server Error");
                //e.printStackTrace();
            }
        }
        else{
            returnStatus=403;
            returnObject=new String("Unauthorized access.");
        }
        return Response.status(returnStatus).entity(returnObject).build();
    }

    @GET
    @Path("/read/")
    @Produces("application/json")
    public Response readAllGoals(@HeaderParam("Authorization") String authenticationToken) throws ParseException {

        MongoConnection connection=new MongoConnection();
        connection.connectToUserDB();
        connection.connectToGoalDB();
        ReadGoal readGoal=new ReadGoal();
        List<Goal> list=new ArrayList<Goal>();
        List<ResponseGoal> responseGoalList=new ArrayList<>();
        User user=new User();
        int returnStatus=0;
        Object returnObject = null;
        if(user.isAuthorized(connection,authenticationToken)){
            list=readGoal.readAllGoals(connection,user.getId());
            for(Goal goal:list){
                responseGoalList.add(new ResponseGoal(goal));
            }
            returnStatus=200;
            returnObject=responseGoalList;
        }
        else{
            returnStatus=403;
            returnObject=new String("Unauthorized access.");
        }
        return Response.status(returnStatus).entity(returnObject).build();
    }

    @PUT
    @Path("/update/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateGoal(Goal goal,@HeaderParam("Authorization") String authenticationToken) throws JSONException, IllegalAccessException {

        MongoConnection connection=new MongoConnection();
        connection.connectToUserDB();
        connection.connectToGoalDB();
        User user=new User();
        int returnStatus=0;
        Object returnObject = null;
        if(user.isAuthorized(connection,authenticationToken)){
            try {
                UpdateGoal updateGoal=new UpdateGoal(goal,connection,user.getId());
                returnStatus=200;
                returnObject=new String("Updated");
            } catch (ParseException e) {
                returnStatus=500;
                returnObject=new String("Internal Server Error");
                //e.printStackTrace();
            }
        }
        else{
            returnStatus=403;
            returnObject=new String("Unauthorized access.");
        }
        return Response.status(returnStatus).entity(returnObject).build();
    }

    @DELETE
    @Path("/delete/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteGoal(@PathParam("name") String name,@HeaderParam("Authorization") String authenticationToken){

        MongoConnection connection=new MongoConnection();
        connection.connectToUserDB();
        connection.connectToGoalDB();
        User user=new User();
        int returnStatus=0;
        Object returnObject = null;
        if(user.isAuthorized(connection,authenticationToken)){
            DeleteGoal deleteGoal=new DeleteGoal(name,connection,user.getId());
            returnStatus=200;
            returnObject=new String("Deleted");
        }
        else{
            returnStatus=403;
            returnObject=new String("Unauthorized access.");
        }
        return Response.status(returnStatus).entity(returnObject).build();}
}
