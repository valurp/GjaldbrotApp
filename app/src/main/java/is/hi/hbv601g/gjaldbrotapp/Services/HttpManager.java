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
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.ComparisonData;
import is.hi.hbv601g.gjaldbrotapp.Entities.OverviewGroup;
import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.Entities.Type;
import is.hi.hbv601g.gjaldbrotapp.Entities.User;

/**
 * Service klasi sem ad tengist vid netþjóninn um URL-ið og er með létt abstraction á
 * POST og GET aðferðir sem að við þurfum.
 * Klasinn fylgir singleton hönnunar mynstrinu til að auðvelda meðferð á token sem er nauðsynleg
 * fyrir authentication í bakendanum.
 */
public class HttpManager {
    private static final String URL = "https://gjaldbrot-rest-service.herokuapp.com/api";
    private static final String TAG = "HttpManager";
    private static HttpManager self;

    private String token;

    public HttpManager() {
    }

    /**
     * Singleton hönnunar mynstrið
     */
    public static HttpManager getInstance() {
        if (self == null) {
            self = new HttpManager();
        }
        return self;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean hasToken() {
        return token != null;
    }

    /**
     * Method for receiving response from server.
     *
     * @param urlSpec URL to query
     * @param method Http method of the query
     * @return
     * @throws IOException
     */
    public byte[] getUrlBytes(String urlSpec, String method) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (token != null) conn.addRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestMethod(method);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = conn.getInputStream();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "Non 200 status");
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
     *
     * @param urlSpec URL to query
     * @return String containing JSON response.
     * @throws IOException
     */
    public String getUrlString(String urlSpec, String method) throws IOException {
        return new String(getUrlBytes(urlSpec, method));
    }

    public void writeTo(HttpURLConnection con, String json) throws Exception {
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            Log.i(TAG, "RESPONSE CODE:\t" + con.getResponseCode());
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8)
        )) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
    }

    /**
     * TODO Specify how
     * Function returns a User object if the user name and password given matches an existing user
     *
     * @param username the username
     * @param password the password
     * @return a valid User object
     */
    public User fetchUser(String username, String password) throws Exception {
        User user;
        // creates the query url
        String url = Uri.parse(URL)
                .buildUpon()
                .appendPath("login")
                .appendQueryParameter("username", username)
                .appendQueryParameter("password", password)
                .build().toString();
        try {
            String jsonString = getUrlString(url, "POST");
            JSONObject jsonBody = new JSONObject(jsonString);
            user = parseUser(jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch user", ioe);
            throw ioe;
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON for user", je);
            throw je;
        }
        return user;
    }

    /**
     * Creates User object from JSON response
     *
     * @param jsonBody JSON response from back-end
     * @return user and assigns a token
     * @throws IOException
     * @throws JSONException
     */
    private User parseUser(JSONObject jsonBody) throws JSONException {
        String token = jsonBody.getString("access_token");
        this.token = token;
        return new User("", token);
    }

    /**
     * Method fetches user's receipt with their token
     *
     * @return
     */
    public List<ReceiptItem> fetchReceipts() {
        Log.i(TAG, "Fetching receipts call");
        if (token == null) {
            Log.e(TAG, "TOKEN ERROR: Token is null");
            return null;
        }
        List<ReceiptItem> receipts = new ArrayList<ReceiptItem>();

        String url = Uri.parse(URL + "/user/receipt")
                .buildUpon()
                .appendQueryParameter("method", "get")
                .appendQueryParameter("format", "json")
                .build().toString();

        try {
            String jsonString = getUrlString(url, "GET");
            JSONArray jsonBody = new JSONArray(jsonString);
            parseReceipts(receipts, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch receipts", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON for receipts", je);
        }
        return receipts;

    }

    /**
     * Fills List with receipts for a given user
     *
     * @param receipts         the (empty) list to fill
     * @param receiptJSONArray the JSON body in which the data can be found
     * @throws IOException
     * @throws JSONException
     */
    private void parseReceipts(List<ReceiptItem> receipts, JSONArray receiptJSONArray) throws JSONException {
        for (int i = 0; i < receiptJSONArray.length(); i++) {
            ReceiptItem receipt = new ReceiptItem();

            JSONObject receiptJSON = receiptJSONArray.getJSONObject(i);
            receipt.setAmount(receiptJSON.getInt("amount"));
            receipt.setId(receiptJSON.getLong("id"));
            receipt.setType(receiptJSON.getString("type"));
            receipt.setTypeId(receiptJSON.getLong("type_id"));
            try {
                String date = receiptJSON.getString("date").split(" ")[0];
                String time = receiptJSON.getString("parsedTime");
                receipt.setFormattedDateWithTime(date + "T" + time);
            } catch (Exception e) {
                Log.e(TAG, "Error parsing date in receipt");
            }
            receipts.add(receipt);
        }
    }

    /**
     * Method creates POST request to back-end, creating user
     * with username and password
     *
     * @param username username
     * @param password password
     * @return int (0 if user was created) (1 if error occured)
     * @throws Exception if URL is invalid, or if connection fails.
     */
    public boolean createUser(String username, String password)  {
        String url = Uri.parse(URL)
                .buildUpon()
                .appendPath("signup")
                .appendQueryParameter("username", username)
                .appendQueryParameter("password", password)
                .build()
                .toString();
        try {
            String response = getUrlString(url, "POST");
        }
        catch (Exception e) {
            Log.e(TAG, "Error creating user");
            return false;
        }
        return true; // user was created
    }

    /**
     * Method POSTs receipt using amount and type, backend requires token
     * and takes care of ID for us.
     *
     * @param amount amount of receipt
     * @param type   type of receipt
     * @throws Exception if URL is invalid, or if connection fails.
     */
    public void createReceipt(int amount, String type, long typeId, String date, String time) throws Exception {
        // builds the url
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
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonReceipt =
                "{ \"amount\":\"" + amount + "\", "
                        + "\"type\":\"" + type + "\","
                        + "\"type_id\":" + typeId + ","
                        + "\"date\":\"" + date + "\","
                        + "\"time\":\"" + time + "\""
                        + "}";
        writeTo(con, jsonReceipt);
    }

    /**
     * Performs a patch call to update the receipt with the id
     * @param amount
     * @param type
     * @param id
     * @param time
     * @param date
     * @throws Exception
     */
    public void updateReceipt(int amount, String type, long id, String time, String date) throws Exception {
        String url = Uri.parse(URL)
                .buildUpon()
                .appendPath("user")
                .appendPath("receipt")
                .appendPath("" + id)
                .build()
                .toString();
        URL patchUrl = new URL(url);
        HttpURLConnection con = (HttpURLConnection) patchUrl.openConnection();
        con.setRequestMethod("PATCH");
        con.setRequestProperty("Authorization", "Bearer " + token);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonReceipt = "{ \"amount\":\"" + amount + "\", "
                + "\"type\":\"" + type + "\","
                + "\"date\":\"" + date + "\","
                + "\"time\":\"" + time + "\""
                + "}";
        writeTo(con, jsonReceipt);
    }

    public void deleteReceipt(long id) throws Exception {
        String url = Uri.parse(URL)
                .buildUpon()
                .appendPath("user")
                .appendPath("receipt")
                .appendPath("" + id)
                .build()
                .toString();
        URL deleteURL = new URL(url);
        HttpURLConnection con = (HttpURLConnection) deleteURL.openConnection();
        con.setRequestProperty("Authorization", "Bearer " + token);
        con.setDoInput(true);
        con.setRequestMethod("DELETE");
        Log.i(TAG, "delete response code: " + con.getResponseCode());
    }

    /**
     * Performs a post call to create a type
     * @param type
     * @param color
     * @throws Exception
     */
    public void createType(String type, int color, int maxBudget) throws Exception {
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
        String jsonType = "{ \"name\": \"" + type + "\","
                + "\"color\": \"" + color + "\","
                + "\"maxBudget\": \"" + maxBudget + "\""
                + "}";
        writeTo(con, jsonType);
    }

    /**
     * Performs a patch call to update the type with the id
     * @param id
     * @param name
     * @param color
     * @throws Exception
     */
    public void updateType(int id, String name, int color, int maxBudget) throws Exception {
        String url = Uri.parse(URL)
                .buildUpon()
                .appendPath("user")
                .appendPath("types")
                .appendPath("" + id)
                .build()
                .toString();
        URL patchUrl = new URL(url);
        HttpURLConnection con = (HttpURLConnection) patchUrl.openConnection();
        con.setRequestMethod("PATCH");
        con.setRequestProperty("Authorization", "Bearer " + token);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonType = "{ \"id\": \"" + id + "\","
                + "\"name\": " + name + "\","
                + "\"color\": " + color + "\","
                + "\"maxBudget\": " + maxBudget + "\""
                + "}";
        writeTo(con, jsonType);
    }

    public void deleteType(int id)  throws IOException {
        String url = Uri.parse(URL)
                .buildUpon()
                .appendPath("user")
                .appendPath("types")
                .appendPath("" + id)
                .build()
                .toString();
        URL deleteURL = new URL(url);
        HttpURLConnection con = (HttpURLConnection) deleteURL.openConnection();
        con.setRequestProperty("Authorization", "Bearer " + token);
        con.setDoInput(true);
        con.setRequestMethod("DELETE");
        Log.i(TAG, "delete response code: " + con.getResponseCode());
    }

    public List<Type> fetchTypes() {
        Log.i(TAG, "Fetching all receipt types");
        if (token == null) {
            Log.e(TAG, "TOKEN ERROR: Token is null");
            return null;
        }
        List<Type> types = new ArrayList<>();
        String url = Uri.parse(URL + "/user/types")
                .buildUpon()
                .appendQueryParameter("method", "get")
                .appendQueryParameter("format", "json")
                .build().toString();
        try {
            String jsonString = getUrlString(url, "GET");
            JSONArray jsonBody = new JSONArray(jsonString);
            parseTypes(types, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch receipt types", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON for types", je);
        }
        return types;
    }

    private void parseTypes(List<Type> types, JSONArray jsonBody) throws JSONException {
        for (int i = 0; i < jsonBody.length(); i++) {
            Type type = new Type();

            JSONObject typeJSON = jsonBody.getJSONObject(i);

            type.setId(typeJSON.getInt("id"));
            type.setName((typeJSON.getString("name")));
            type.setColor(typeJSON.getInt("color"));
            type.setMaxBudget(typeJSON.getInt("maxBudget"));
            types.add(type);
        }
    }

    public List<OverviewGroup> fetchOverview() {
        Log.i(TAG, "Starting fetch overview call");
        if (token == null) {
            Log.e(TAG, "TOKEN ERROR: Token is null");
            return null;
        }
        List<OverviewGroup> overview = new ArrayList<>();
        String url = Uri.parse(URL + "/user/overview")
                .buildUpon()
                .appendQueryParameter("method", "get")
                .appendQueryParameter("format", "json")
                .build().toString();
        try {
            String jsonString = getUrlString(url, "GET");
            JSONObject jsonBody = new JSONObject(jsonString);
            parseOverview(overview, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch overview data", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON for overview data", je);
        }
        return overview;
    }

    private void parseOverview(List<OverviewGroup> overview, JSONObject jsonBody) throws JSONException {
        JSONArray groupsJSON = jsonBody.getJSONArray("group");
        for (int i = 0; i < groupsJSON.length(); i++) {
            OverviewGroup group = new OverviewGroup();
            JSONObject groupJSON = groupsJSON.getJSONObject(i);
            group.setAmount(groupJSON.getInt("amount"));
            group.setCategory(groupJSON.getString("name"));
            group.setColor(groupJSON.getInt("color"));
            group.setMaxBudget(groupJSON.getInt("maxBudget"));
            overview.add(group);
        }
    }

    public ComparisonData fetchComparison() {
        Log.i("Comparison Fetch", "Fetching comparison");
        if (token == null) {
            Log.e(TAG, "Token is null");
            return null;
        }
        try {
            String url = Uri.parse(URL + "/user/comparison")
                    .buildUpon()
                    .build().toString();
            String jsonString = getUrlString(url, "GET");
            JSONObject jsonBody = new JSONObject(jsonString);
            return parseComparison(jsonBody);
        } catch (IOException ioe) {
            Log.e("GjaldbrotApp", "Failed to fetch receipts", ioe);
        } catch (Exception je) {
            Log.e("GjaldbrotApp", "Failed to parse JSON", je);
        }
        return null;
    }

    private ComparisonData parseComparison(JSONObject body) throws JSONException, ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        ComparisonData comparisonData = new ComparisonData();
        Date startDate = df.parse(body.getString("startDate"));
        Date endDate = df.parse(body.getString("endDate"));
        comparisonData.mStartDate = startDate;
        comparisonData.mEndDate = endDate;
        JSONArray groups = body.getJSONArray("groups");
        for(int i = 0; i < groups.length(); i++) {
            JSONObject group = groups.getJSONObject(i);
            String name = group.getString("name");
            int color = group.getInt("color");
            ComparisonData.Group group1 = comparisonData.addGroup(name, color);
            JSONArray amounts = group.getJSONArray("amounts");
            for(int j = 0; j < amounts.length(); j++) {
                JSONObject monthAmount = amounts.getJSONObject(j);
                Date month = df.parse(monthAmount.getString("date"));
                int amount = monthAmount.getInt("amount");
                group1.addMonthAmount(month, amount);
            }
        }
        return comparisonData;
    }
}
