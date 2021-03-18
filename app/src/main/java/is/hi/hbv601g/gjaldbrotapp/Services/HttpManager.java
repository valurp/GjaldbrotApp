package is.hi.hbv601g.gjaldbrotapp.Services;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.Entities.User;

public class HttpManager {
    private String token;

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
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
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
    public User fetchUser() {
        User user = new User();
        try {
            String url = Uri.parse("http://[APP SITE NAME HERE]/")
                    .buildUpon()
                    .appendQueryParameter("method", "get")
                    .appendQueryParameter("format", "json")
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

    private User parseUser(JSONObject jsonBody) throws IOException, JSONException {
        JSONObject userJsonObject = jsonBody.getJSONObject("user");
        String name = userJsonObject.getString("name");
        String token = userJsonObject.getString("token");
        return new User(name, token);
    }

    public List<ReceiptItem> fetchReceipts() {
        if(token == null){
            return null;
        }
        List<ReceiptItem> receipts = new ArrayList<ReceiptItem>();
        try {
            String url = Uri.parse("http://[APP SITE NAME HERE]/")
                    .buildUpon()
                    .appendQueryParameter("method", "get")
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .build().toString();
            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
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
     * @param jsonBody the JSON body in which the data can be found
     * @throws IOException
     * @throws JSONException
     */
    private void parseReceipts(List<ReceiptItem> receipts, JSONObject jsonBody) throws IOException, JSONException{
        JSONObject receiptsJSONObject = jsonBody.getJSONObject("receipts");
        JSONArray receiptJSONArray = receiptsJSONObject.getJSONArray("receipt");
        for (int i = 0; i < receiptJSONArray.length(); i++) {
            ReceiptItem receipt = new ReceiptItem();

            JSONObject receiptJSON = receiptJSONArray.getJSONObject(i);

            receipt.setAmount(receiptJSON.getInt("amount"));
            receipt.setId(receiptJSON.getInt("id"));
            receipt.setType(receiptJSON.getString("type"));
            receipts.add(receipt);
        }
    }
}
