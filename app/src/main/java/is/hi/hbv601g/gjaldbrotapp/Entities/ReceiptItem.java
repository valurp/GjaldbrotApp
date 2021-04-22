package is.hi.hbv601g.gjaldbrotapp.Entities;

import android.nfc.FormatException;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Entity klasi sem geymir upplýsingar um kvittanir.
 */
public class ReceiptItem implements Serializable {

    private static DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat mDateFormatWithTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static DateFormat mTimeFormat = new SimpleDateFormat("HH:mm:ss");

    private long mId;
    private int mAmount;
    private String mType;
    private long mTypeId;
    private Date mDate;


    public ReceiptItem() {
        mDate = new Date();
    }

    public ReceiptItem(int id, int amount, String type) {
        mId = id;
        mAmount = amount;
        mType = type;
    }

    public void setId(long id) {
        mId = id;
    }

    public void setAmount(int amount) {
        mAmount = amount;
    }

    public void setType(String type) {
        mType = type;
    }

    public void setTypeId(long typeId) { mTypeId = typeId; }

    public long getId() {
        return mId;
    }

    public int getAmount() {
        return mAmount;
    }

    public String getType() {
        return mType;
    }

    public long getTypeId() { return mTypeId; }

    public Date getDate() { return mDate; };

    public void setFormattedDate(String formattedDate) throws Exception {
        mDate = mDateFormat.parse(formattedDate);
    }

    public String getFormattedDate() {
        return mDateFormat.format(mDate);
    }

    public String getTime() {
        return mTimeFormat.format(mDate);
    }

    public void setFormattedDateWithTime(String formattedDateWithTime) throws Exception {
        mDate = mDateFormatWithTime.parse(formattedDateWithTime);
    }
}
