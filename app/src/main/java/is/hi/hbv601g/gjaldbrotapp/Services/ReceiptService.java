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
    private static final String TAG = "RECEIPT SERVICE";

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
     * Method fetches all receipts from logged in user
     * @return list of their receipts
     */
    public List<ReceiptItem> fetchReceipts(){
        return httpManager.fetchReceipts();
    }

    /**
     * Method adds receipt for user
     * @param r the receipt
     */
    public Boolean addReceipt(ReceiptItem r){
        try {
            int amount = r.getAmount();
            String type = r.getType(); // TODO láta type tengjast meira ReceiptType
            String date = r.getFormattedDate();
            String time = r.getTime();
            httpManager.createReceipt(amount, type, r.getTypeId(), date, time);
            Log.i(TAG, "Receipt created");
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    /**
     * Method adds type for receipt for a given user
     * @param type the name of the Type
     */
    public Boolean addType(String type, int color){
        try {
            httpManager.createType(type, color);
        } catch (Exception e){
            Log.e(TAG, "Error creating type");
            return false;
        }
        Log.i(TAG, "Type created");
        return true;
    }

    /**
     * Method modifies old receipt from User
     * @param r the Receipt to be modified
     */
    public Boolean changeReceipt(ReceiptItem r){
        try {
            httpManager.updateReceipt(r.getAmount(), r.getType(), r.getId(), r.getTime(), r.getFormattedDate());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
        Log.i(TAG, "Receipt updated");
        return true;
    }

    /**
     * Method fetches users receipt types
     * @return all types for logged user
     */
    public List<Type> fetchReceiptType(){
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
            Log.e(TAG, e.toString());
            return false;
        }
        Log.i(TAG, "Type updated");
        return true;
    }

    public List<OverviewGroup> fetchOverview(){
        return httpManager.fetchOverview();
    }

    public String fetchComparison() {
        return httpManager.fetchComparison();
    }
}
