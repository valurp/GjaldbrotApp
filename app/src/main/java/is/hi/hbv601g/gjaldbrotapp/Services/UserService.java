package is.hi.hbv601g.gjaldbrotapp.Services;

import android.util.Log;

import is.hi.hbv601g.gjaldbrotapp.Entities.User;

public class UserService {
    private User user;
    HttpManager manager = new HttpManager();

    public UserService(){

    }

    /**
     * Method tries to log in user with
     * username and password
     * @param u Username
     * @param p Password
     * @return User if he exists
     * @throws Exception if no such User exists
     */
    public User fetchUser(String u, String p){
        User login = null;
        try {
            login = manager.fetchUser(u, p);
        } catch (Exception e) {
            Log.e("","Error fetching user");
        }
        this.user = login;
        return login;
    }

    /**
     * Method tries to create new user
     * with a username and password
     * @param u Username
     * @param p Password
     * @throws Exception if username is already taken
     */
    public boolean createUser(String u, String p){
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
