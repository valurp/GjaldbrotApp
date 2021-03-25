package is.hi.hbv601g.gjaldbrotapp.Services;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.Entities.User;

public class ReceiptService {
    private User loggedUser = new User(); //Hold reference to who's logged in to keep authorization
    private UserService uService = new UserService(); //Use uService to fetch logged user
    private HttpManager httpManager = new HttpManager();

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
        if(loggedUser == null) loggedUser = uService.getUser();
        httpManager.setToken(loggedUser.getToken());
        List<ReceiptItem> receipts = new ArrayList<>();
        receipts = httpManager.fetchReceipts();
        return  receipts;
    }

    /**
     * Method adds receipt for user
     * @param r the receipt
     */
    public void addReceipt(ReceiptItem r){
        if(loggedUser == null) uService.getUser();
        try {
            int amount = r.getAmount();
            String type = r.getType();
            httpManager.setToken(loggedUser.getToken());
            httpManager.createReceipt(amount, type);
        } catch (Exception e) {
            Log.e("HTTPManager", "Error creating receipt");
        }
        Log.i("HTTPManager", "Receipt created");
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
