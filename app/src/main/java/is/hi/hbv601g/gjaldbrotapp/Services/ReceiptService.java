package is.hi.hbv601g.gjaldbrotapp.Services;

import android.util.Log;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
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
     * Construcor
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
            String time = "10:10:10";
            httpManager.createReceipt(amount, type, date, time);
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
    public void addType(String type){
        if(loggedUser == null) uService.getUser();
        try {
            httpManager.setToken(loggedUser.getToken());
            httpManager.createType(type);
        } catch (Exception e){
            Log.e("HTTPManager", "Error creating type");
        }
        Log.i("HTTPManager", "Type created");
    }

    /**
     * Method modifies old receipt from User
     * @param r the Receipt to be modified
     * @param params the attributes to modify
     */
    public void changeReceipt(ReceiptItem r, String params){
        if(loggedUser == null) uService.getUser();
        try {
            httpManager.setToken(loggedUser.getToken());
            httpManager.updateReceipt(r.getAmount(),r.getType(), r.getId());
        } catch (Exception e) {
            Log.e("HTTPManager", "Error updatingreceipt");
        }
        Log.i("HTTPManager", "Receipt updated");
    }

    /**
     * Testing method to create fake Receipts
     * DELETE AFTER FINISHED
     */
    public List<ReceiptItem> mockReceipts(int num){
        String[] types = {"Matur", "Leiga", "Áfengi", "Fatnaður"};
        int[] amounts = {500, 2000, 3000, 5000, 10000, 1000};
        int id = 1;
        int n = types.length;
        int m = amounts.length;
        List<ReceiptItem> mock = new ArrayList<>();
        for(int i = 0; i < num; i++){
            String t = types[(int)(Math.random()*n)];
            int a = amounts[(int)(Math.random()*m)];
            ReceiptItem r = new ReceiptItem(id, a,t);
            mock.add(r);
            id++;
        }
        return mock;
    }
}
