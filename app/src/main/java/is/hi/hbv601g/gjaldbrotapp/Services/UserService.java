package is.hi.hbv601g.gjaldbrotapp.Services;

import is.hi.hbv601g.gjaldbrotapp.Entities.User;

public class UserService {
    private User user;

    /**
     * Method tries to log in user with
     * username and password
     * @param u Username
     * @param p Password
     * @return User if he exists
     * @throws Exception if no such User exists
     */
    public User fetchUser(String u, String p){
        throw new UnsupportedOperationException("This function has not been implemented");
    }

    /**
     * Method tries to create new user
     * with a username and password
     * @param u Username
     * @param p Password
     * @throws Exception if username is already taken
     */
    public void createUser(String u, String p){
        throw new UnsupportedOperationException("This function has not been implemented");
    }

    /**
     * Parses String from HTTPManager into User
     * @param s result from HTTPManager
     * @return User object parsed from string
     */
    private User parseItem(String s){
        throw new UnsupportedOperationException("This function has not been implemented");
    }

    /**
     * Method returns logged in user
     * @return logged in User
     */
    public User getUser(){
        return user;
    }
}
