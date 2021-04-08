package is.hi.hbv601g.gjaldbrotapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReceiptEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceiptEditFragment extends Fragment {
    private static String TAG = "DATE_PICKER";

    private Spinner mYearSpinner;
    private Spinner mMonthSpinner;
    private Spinner mDaySpinner;
    private int day = 1; // þessi breyta er til að uppfæra daySpinner eftir að monthSpinner hefur verið valinn

    private EditText mAmountField;
    private EditText mTimeField;
    private Spinner mTypeSpinner;

    private EditText amount;

    public ReceiptEditFragment() {
    }
    public ReceiptEditFragment(ReceiptItem receiptItem) {
        // some initialization
    }
    public static ReceiptEditFragment newInstance(Context parentContext, Bundle bundle) {
        ReceiptEditFragment fragment = new ReceiptEditFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receipt_edit, container, false);
        /**
         * Setja upp dagsetningar spinner-a
         */
        mYearSpinner = (Spinner) view.findViewById(R.id.receipt_edit_sp_year);
        mMonthSpinner = (Spinner) view.findViewById(R.id.receipt_edit_sp_month);
        mDaySpinner = (Spinner) view.findViewById(R.id.receipt_edit_sp_day);

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.years, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mYearSpinner.setAdapter(yearAdapter);

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMonthSpinner.setAdapter(monthAdapter);
        mMonthSpinner.setOnItemSelectedListener(new OnMonthSelected());
        setDate(new Date()); // todo rétt timezone

        /**
         * Tengja rest af inputum
         */
        mAmountField = (EditText) view.findViewById(R.id.receipt_edit_et_amount);
        mTimeField = (EditText) view.findViewById(R.id.receipt_edit_et_time);
        mTypeSpinner = (Spinner) view.findViewById(R.id.receipt_edit_sp_type);
        ArrayAdapter<CharSequence> typeAdapter =
                new ArrayAdapter(this.getContext(),
                        android.R.layout.simple_spinner_item,
                        new ArrayList(Arrays.asList(1,2,3))); // todo láta lesa frá notenda upplýsingum
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(typeAdapter);
        return view;
    }

    private String getDateSelected() {
        String year = mYearSpinner.getSelectedItem().toString();
        String month = mMonthSpinner.getSelectedItemPosition()+1+"";
        String day = mDaySpinner.getSelectedItem().toString();
        Log.i(TAG, String.format("%s-%s-%s", year, month, day));
        return String.format("%s-%s-%s", year, month, day);
    }

    private void setDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.i(TAG+" month", month+"");
        Log.i(TAG+" day", day-1+"");
        // TODO upphafsstilla árið í yearSpinner
        this.day = day-1;
        mMonthSpinner.setSelection(month);
    }

    /**
     * Main function of this fragment, returns the receipt that the user has entered into the fields
     * @return
     * @throws Exception
     */
    public ReceiptItem getReceiptItem() throws Exception {
        String date = getDateSelected();
        String amount = mAmountField.getText().toString();
        String time = mTimeField.getText().toString();
        String type = mTypeSpinner.getSelectedItem().toString();

        if (date.equals("") || amount.equals("") || time.equals("") || type.equals("")) {
            throw new Exception("Input error, not all fields are filled in");
        }
        ReceiptItem receiptItem = new ReceiptItem();

        try {
            receiptItem.setFormattedDate(date);
        }
        catch (Exception e) {
            throw new Exception("Could not parse date, format yyyy-MM-dd");
        }
        receiptItem.setAmount(Integer.parseInt(amount));
        receiptItem.setType(type);
        return receiptItem;
    }

    private class OnMonthSelected implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView parent, View view, int pos, long id) {
            // TODO geyma hvaða item er selected og setja það eftir að breytt er um dagafjölda
            // notar id af mánuði sem er valinn til að setja réttan dagafjölda í daySpinner
            // allir mánuðir hafa amk 28 daga
            List<Integer> days = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28));
            List<Integer> thirtyOneDayMonths = new ArrayList<Integer>(Arrays.asList(0,2,4,6,7,9,11));
            if(pos == 1) { // TODO factor in leap year for february
                // do nothing if month is february
            }
            else { // all other month have atleast 30 days
                days.add(29);
                days.add(30);
            }
            if(thirtyOneDayMonths.contains(pos)) {
                days.add(31);
            }
            ArrayAdapter<Integer> dayAdapter =
                    new ArrayAdapter<Integer>(ReceiptEditFragment.this.getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            days);
            dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mDaySpinner.setAdapter(dayAdapter);
            mDaySpinner.setSelection(day);
        }

        @Override
        public void onNothingSelected(AdapterView parent) {
        }
    }
}