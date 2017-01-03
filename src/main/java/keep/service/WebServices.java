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
 * API's exposed by WebServvice
 * Created by suyog on 12/17/2016.
 */
@Path("/keep")
public class WebServices {

    /**
     * Creates a newuser with the credentials given in HeaderParams:"Authorization"
     * @param authenticationToken
     * @return Username with success response code.
     */
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

    /**
     * Logins the current user.
     * @param authenticationToken
     * @return response code.
     */
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

    /**
     * Creates a goal in the DB using the JSON goal object passed by user.
     * @param goal
     * @param authenticationToken
     * @return Created ResopnseGoal object with response code.
     */
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

    /**
     * Returns the goal by given goal name.
     * @param name
     * @param authenticationToken
     * @return If successful, returns Goal by given name
     *         If error, Internal server Error with code 500.
     *         If user is not logged In or not valid, Unauthorized access with code 403.
     */
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
            }
        }
        else{
            returnStatus=403;
            returnObject=new String("Unauthorized access.");
        }
        return Response.status(returnStatus).entity(returnObject).build();
    }

    /**
     * Returns all the goals for a user.
     * @param authenticationToken
     * @return If successful, returns Goals.
     *         If error, Internal server Error with code 500.
     *         If user is not logged In or not valid, Unauthorized access with code 403.
     */
    @GET
    @Path("/read/")
    @Produces("application/json")
    public Response readAllGoals(@HeaderParam("Authorization") String authenticationToken) {

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
            try {
                list = readGoal.readAllGoals(connection, user.getId());
                for (Goal goal : list) {
                    responseGoalList.add(new ResponseGoal(goal));
                }
                returnStatus = 200;
                returnObject = responseGoalList;
            }
            catch (ParseException pe){
                returnStatus=500;
                returnObject=new String("Internal Server Error");
            }
        }
        else{
            returnStatus=403;
            returnObject=new String("Unauthorized access.");
        }
        return Response.status(returnStatus).entity(returnObject).build();
    }

    /**
     * Updates the goal given by goal name.
     * @param goal
     * @param authenticationToken
     * @return If successful, returns updated message.
     *         If error, Internal server Error with code 500.
     *         If user is not logged In or not valid, Unauthorized access with code 403.
     */
    @PUT
    @Path("/update/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateGoal(Goal goal,@HeaderParam("Authorization") String authenticationToken) {

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
            } catch (IllegalAccessException e) {
                returnStatus=500;
                returnObject=new String("Internal Server Error");
            } catch (JSONException e) {
                returnStatus=500;
                returnObject=new String("Internal Server Error");
            }
        }
        else{
            returnStatus=403;
            returnObject=new String("Unauthorized access.");
        }
        return Response.status(returnStatus).entity(returnObject).build();
    }

    /**
     * Deletes a goal by name.
     * @param name
     * @param authenticationToken
     * @return If successful, returns deleted message.
     *         If error, Internal server Error with code 500.
     *         If user is not logged In or not valid, Unauthorized access with code 403.
     */
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
