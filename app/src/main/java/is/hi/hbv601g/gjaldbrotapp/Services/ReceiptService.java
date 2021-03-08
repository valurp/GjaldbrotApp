package is.hi.hbv601g.gjaldbrotapp.Services;

import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.Entities.User;

public class ReceiptService {
    private User loggedUser; //Hold reference to who's logged in to keep authorization
    private UserService uService; //Use uService to fetch logged user
    //private HttpManager httpManager;

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
        throw new UnsupportedOperationException("This function has not been implemented");
    }

    /**
     * Method adds receipt for user
     * @param r the receipt
     */
    public void addReceipt(ReceiptItem r){
        throw new UnsupportedOperationException("This function has not been implemented");
    }

    /**
     * Method adds type for receipt for a given user
     * @param type the name of the Type
     */
    public void addType(String type){
        throw new UnsupportedOperationException("This function has not been implemented");
    }

    /**
     * Method modifies old receipt from User
     * @param r the Receipt to be modified
     * @param params the attributes to modify
     */
    public void changeReceipt(ReceiptItem r, String params){
        throw new UnsupportedOperationException("This function has not been implemented");
    }



}
