package is.hi.hbv601g.gjaldbrotapp.Entities;

import android.nfc.FormatException;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiptItem implements Serializable {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private int mId;
    private int mAmount;
    private String mType;
    private Date mDate;
    //Private ??? date TODO implement some date and time fields

    public ReceiptItem(int id, int amount, String type) {
        this.mId = id;
        this.mAmount = amount;
        this.mType = type;
    }

    public ReceiptItem() {

    }

    public void setId(int id) {
        mId = id;
    }

    public void setAmount(int amount) {
        mAmount = amount;
    }

    public void setType(String type) {
        mType = type;
    }

    public int getId() {
        return mId;
    }

    public int getAmount() {
        return mAmount;
    }

    public String getType() {
        return mType;
    }

    public Date getDate() { return mDate; };

    public void setFormattedDate(String formattedDate) throws Exception {
        mDate = df.parse(formattedDate);
    }

    public String getFormattedDate() {
        return df.format(mDate);
    }





}
