package is.hi.hbv601g.gjaldbrotapp.Entities;

public class User {

    private String mName;
    private String mToken;

    public User(){
        
    }

    public User(String name, String token) {
        mName = name;
        mToken = token;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }
}
