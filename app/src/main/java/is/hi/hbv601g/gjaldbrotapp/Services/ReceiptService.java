package is.hi.hbv601g.gjaldbrotapp.Services;

import android.util.Log;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.OverviewGroup;
import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.Entities.Type;
import is.hi.hbv601g.gjaldbrotapp.Entities.User;

/**
 * Enn hærra abstraction á vefköllum, notar Entity klasa til að auðvelda meðhöndlun.
 * Notar líka singleton hönnunarmynstrið því okkur finnst það passa við network service klasa.
 */
public class ReceiptService {
    private static ReceiptService self; // vísun í sjálfan sig fyrir singleton

    private User loggedUser = new User(); //Hold reference to who's logged in to keep authorization
    private UserService uService; //Use uService to fetch logged user
    private HttpManager httpManager; // eiginlegt bakbein fyrir vefköll

    /**
     * Singleton
     */
    public static ReceiptService getInstance() {
        if (self == null) {
            self = new ReceiptService();
        }
        return self;
    }

    /**
     * Constructor
     */
    public ReceiptService() {
        uService = UserService.getInstance();
        httpManager = HttpManager.getInstance();
    }

    /**
     * Parses String from HTTPManager into list of
     * ReceiptItems
     * @param s result from HTTPManager
     * @return list of ReceiptItems
     */
    private List<ReceiptItem> parseItems(String s){
        throw new UnsupportedOperationException("This function has not been implemented");
    }

    /**
     * Method fetches all receipts from logged in user
     * @return list of their receipts
     */
    public List<ReceiptItem> fetchReceipts(){
        if (!httpManager.hasToken()) {
            Log.e("MANAGER TOKEN", "HttpManager has no token");
            return null;
        }
        return httpManager.fetchReceipts();
    }

    /**
     * Method adds receipt for user
     * @param r the receipt
     */
    public Boolean addReceipt(ReceiptItem r){
        try {
            /*int amount = r.getAmount();
            String type = r.getType();*/
            int amount = r.getAmount();
            String type = r.getType(); // TODO láta type tengjast meira ReceiptType
            String date = r.getFormattedDate();
            String time = r.getTime();
            Log.i("RECEIPT SERVICE", time);
            httpManager.createReceipt(amount, type, r.getTypeId(), date, time);
            Log.i("RECEIPT_SERVICE", "Receipt created");
            return true;
        } catch (Exception e) {
            Log.e("RECEIPT_SEVICE", e.toString());
            return false;
        }
    }

    /**
     * Method adds type for receipt for a given user
     * @param type the name of the Type
     */
    public void addType(String type, int color){
        if(loggedUser == null) uService.getUser();
        try {
            httpManager.setToken(loggedUser.getToken());
            httpManager.createType(type, color);
        } catch (Exception e){
            Log.e("HTTPManager", "Error creating type");
        }
        Log.i("HTTPManager", "Type created");
    }

    /**
     * Method modifies old receipt from User
     * @param r the Receipt to be modified
     */
    public Boolean changeReceipt(ReceiptItem r){
        // if(loggedUser == null) uService.getUser();
        try {
            httpManager.updateReceipt(r.getAmount(), r.getType(), r.getId(), r.getTime(), r.getFormattedDate());
        } catch (Exception e) {
            Log.e("HTTPManager", e.toString());
            return false;
        }
        Log.i("HTTPManager", "Receipt updated");
        return true;
    }

    /**
     * Method fetches users receipt types
     * @return all types for logged user
     */
    public List<Type> fetchReceiptType(){
        if (!httpManager.hasToken()) {
            Log.e("MANAGER TOKEN", "HttpManager has no token");
            return null;
        }
        return httpManager.fetchTypes();
    }

    /**
     * Method updates name for given receipt
     * @param t type to modify
     * @return true if successful, otherwise false
     */
    public Boolean changeType(Type t){
        try {
            httpManager.updateType(t.getId(), t.getName(), t.getColor());
        } catch (Exception e) {
            Log.e("HTTPManager", e.toString());
            return false;
        }
        Log.i("HTTPManager", "Type updated");
        return true;
    }

    public List<OverviewGroup> fetchOverview(){
        if (!httpManager.hasToken()) {
            Log.e("MANAGER TOKEN", "HttpManager has no token");
            return null;
        }
        return httpManager.fetchOverview();
    }
}
