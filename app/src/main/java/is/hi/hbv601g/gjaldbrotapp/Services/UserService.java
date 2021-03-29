package is.hi.hbv601g.gjaldbrotapp.Services;

import android.util.Log;

import is.hi.hbv601g.gjaldbrotapp.Entities.User;

public class UserService {
    private static UserService self;
    private User user;
    private HttpManager manager;

    /**
     * Singleton
     */
    public static UserService getInstance() {
        if (self == null) {
            self = new UserService();
        }
        return self;
    }

    public UserService(){
        manager = HttpManager.getInstance();
    }

    /**
     * Sets the user token of the HttpManager for authorized calls
     * @param token String
     */
    public void setToken(String token) {
        manager.setToken(token);
    }

    /**
     * Method tries to log in user with
     * username and password
     * @param u Username
     * @param p Password
     * @return User if he exists, null if no such user exists
     */
    public User fetchUser(String u, String p){
        try {
            return manager.fetchUser(u, p);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method tries to create new user
     * with a username and password
     * @param u Username
     * @param p Password
     * @throws Exception if username is already taken
     */
    public Boolean createUser(String u, String p){
        try {
            manager.createUser(u, p);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Method returns logged in user
     * @return logged in User
     */
    public User getUser(){
        return user;
    }

    public void setUser(User u){
        user = u;
    }

    /**
     * Testing method
     * DELETE WHEN FINISHED
     * @return
     */
    public User mockUser(){
        return new User("Foo", "Bar");
    }
}
