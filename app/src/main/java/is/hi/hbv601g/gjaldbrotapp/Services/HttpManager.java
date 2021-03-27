package is.hi.hbv601g.gjaldbrotapp.Services;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.Entities.User;

public class HttpManager {
    private String token;
    private static final String URL = "https://gjaldbrot-rest-service.herokuapp.com/api";

    /**
     * Method for receiving response from server.
     * @param urlSpec URL to query
     * @return
     * @throws IOException
     */
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        if(token != null) conn.addRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestMethod("GET");
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = conn.getInputStream();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(conn.getResponseMessage() + ":with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            conn.disconnect();
        }
    }

    /**
     * Method for getting response from server
     * @param urlSpec URL to query
     * @return String containing JSON response.
     * @throws IOException
     */
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public void writeTo(HttpURLConnection con, String json) throws Exception{
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
            System.out.println(con.getResponseCode());
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8")
        )){
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
    }

    /** TODO Specify how
     * Function returns a User object if the user name and password given matches an existing user
     * @param u the username
     * @param p the password
     * @return a valid User object
     */
    public User fetchUser(String u, String p) {
        User user = new User();
        try {
            String url = Uri.parse(URL)
                    .buildUpon()
                    .appendPath("login")
                    .appendQueryParameter("username", u)
                    .appendQueryParameter("password", p)
                    .appendQueryParameter("nojsoncallback", "1")
                    .build().toString();
            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            user = parseUser(jsonBody);
        } catch (IOException ioe) {
            Log.e("GjaldbrotApp", "Failed to fetch user", ioe);
        } catch (JSONException je) {
            Log.e("GjaldbrotApp", "Failed to parse JSON", je);
        }
        return user;

    }

    /**
     * Creates User object from JSON response
     * @param jsonBody JSON response from back-end
     * @return user and assigns a token
     * @throws IOException
     * @throws JSONException
     */
    private User parseUser(JSONObject jsonBody) throws IOException, JSONException {
        JSONObject userJsonObject = jsonBody.getJSONObject("user");
        String name = userJsonObject.getString("name");
        String token = userJsonObject.getString("token");
        this.token = token;
        return new User(name, token);
    }

    /**
     * Method fetches user's receipt with their token
     * @return
     */
    public List<ReceiptItem> fetchReceipts() {
        Log.i("Receipt http", "starting fetch receipt call");
        token = "d1423fdf-5895-4b2c-8822-0c3fe87b394b"; // TODO gera eitthvað þannig að token er pottþétt sett
        if(token == null){
            return null;
        }
        List<ReceiptItem> receipts = new ArrayList<ReceiptItem>();
        try {
            String url = Uri.parse(URL+"/user/receipt")
                    .buildUpon()
                    .appendQueryParameter("method", "get")
                    .appendQueryParameter("format", "json")
                    .build().toString();
            String jsonString = getUrlString(url);
            JSONArray jsonBody = new JSONArray(jsonString);
            parseReceipts(receipts, jsonBody);
        } catch (IOException ioe) {
            Log.e("GjaldbrotApp", "Failed to fetch user", ioe);
        } catch (JSONException je) {
            Log.e("GjaldbrotApp", "Failed to parse JSON", je);
        }
        return receipts;

    }

    /**
     * Fills List with receipts for a given user
     * @param receipts the (empty) list to fill
     * @param receiptJSONArray the JSON body in which the data can be found
     * @throws IOException
     * @throws JSONException
     */
    private void parseReceipts(List<ReceiptItem> receipts, JSONArray receiptJSONArray) throws IOException, JSONException{
        //JSONObject receiptsJSONObject = jsonBody.getJSONObject("receipts");
        //JSONArray receiptJSONArray = receiptsJSONObject.getJSONArray("receipt");
        for (int i = 0; i < receiptJSONArray.length(); i++) {
            ReceiptItem receipt = new ReceiptItem();

            JSONObject receiptJSON = receiptJSONArray.getJSONObject(i);

            receipt.setAmount(receiptJSON.getInt("amount"));
            receipt.setId(receiptJSON.getInt("id"));
            receipt.setType(receiptJSON.getString("type"));
            receipts.add(receipt);
        }
    }

    /**
     * Method creates POST request to back-end, creating user
     * with username and password
     * @param name username
     * @param password password
     * @throws Exception if URL is invalid, or if connection fails.
     */
    public void createUser(String name, String password) throws Exception{
        String url = Uri.parse(URL)
                .buildUpon()
                .appendPath("/signup")
                .build()
                .toString();
        URL postUrl = new URL(url);
        HttpURLConnection con = (HttpURLConnection) postUrl.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonUser = "{ name:" + name + ",\n password: " + password + "}";
        writeTo(con, jsonUser);
    }

    /**
     * Method POSTs receipt using amount and type, backend requires token
     * and takes care of ID for us.
     * @param amount amount of receipt
     * @param type type of receipt
     * @throws Exception if URL is invalid, or if connection fails.
     */
    public void createReceipt(int amount, String type) throws Exception {
        String url = Uri.parse(URL)
                .buildUpon()
                .appendPath("user")
                .appendPath("receipt")
                .build()
                .toString();
        URL postUrl = new URL(url);
        HttpURLConnection con = (HttpURLConnection) postUrl.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + token);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonReceipt = "{ amount: " + amount + ",\n type: " + type + "}";
        writeTo(con, jsonReceipt);
    }

    public void updateReceipt(int amount, String type, int id) throws Exception {
        String url = Uri.parse(URL)
                .buildUpon()
                .appendPath("users")
                .appendPath("receipt")
                .appendPath(""+ id)
                .build()
                .toString();
        URL patchUrl = new URL(url);
        HttpURLConnection con = (HttpURLConnection) patchUrl.openConnection();
        con.setRequestMethod("PATCH");
        con.setRequestProperty("Authorization", "Bearer " + token);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonReceipt = "{ amount: " + amount + ",\n type: " + type + "}";
        writeTo(con, jsonReceipt);
    }

    public void createType(String type) throws Exception{
        String url = Uri.parse(URL)
                .buildUpon()
                .appendPath("user")
                .appendPath("types")
                .build()
                .toString();
        URL postUrl = new URL(url);
        HttpURLConnection con = (HttpURLConnection) postUrl.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + token);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonType = "{ type: " + type + "}";
        writeTo(con, jsonType);
    }

    public HttpManager(){

    }

    public void setToken(String token) {
        this.token = token;
    }
}
