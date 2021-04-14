package is.hi.hbv601g.gjaldbrotapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.Entities.Type;
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ReceiptEditFragment extends Fragment {
    private static String TAG = "DATE_PICKER";

    private DatePicker mDatePicker;

    private EditText mAmountField;
    private EditText mTimeField;
    private Spinner mTypeSpinner;
    private List<Type> mTypeList;

    private ReceiptItem mReceiptItem;

    public ReceiptEditFragment() {
        mReceiptItem = new ReceiptItem();
        mTypeList = new ArrayList<Type>();
    }

    public ReceiptEditFragment(ReceiptItem receiptItem) {
        mReceiptItem = receiptItem;
        mTypeList = new ArrayList<Type>();
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
        mDatePicker = (DatePicker) view.findViewById(R.id.edit_receipt_datepicker);


        /**
         * Tengja rest af inputum
         */
        mAmountField = (EditText) view.findViewById(R.id.receipt_edit_et_amount);
        mTimeField = (EditText) view.findViewById(R.id.receipt_edit_et_time);
        mTypeSpinner = (Spinner) view.findViewById(R.id.receipt_edit_sp_type);
        ArrayAdapter<CharSequence> typeAdapter =
                new ArrayAdapter(this.getContext(),
                        android.R.layout.simple_spinner_item,
                        new ArrayList(Arrays.asList("Loading ..."))); // todo láta lesa frá notenda upplýsingum

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(typeAdapter);

        new FetchTypesTask(typeAdapter).execute();
        
        initializeForm();

        return view;
    }

    private void initializeForm() {
        setDate(mReceiptItem.getDate());
        mAmountField.setText(String.format("%d", mReceiptItem.getAmount()));
    }

    private String getDateSelected() {
        String year = String.valueOf(mDatePicker.getYear());
        String month = String.valueOf(mDatePicker.getMonth()+1);
        String day = String.valueOf(mDatePicker.getDayOfMonth());
        Log.i(TAG, String.format("%s-%s-%s", year, month, day));
        return String.format("%s-%s-%s", year, month, day);
    }

    private void setDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.updateDate(year, month, day);
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
        String type = getIdOfTypeSelected();

        if (date.equals("") || amount.equals("") || time.equals("") || type.equals("")) {
            throw new Exception("Input error, not all fields are filled in");
        }

        try {
            mReceiptItem.setFormattedDate(date);
        }
        catch (Exception e) {
            throw new Exception("Could not parse date, format yyyy-MM-dd");
        }
        mReceiptItem.setAmount(Integer.parseInt(amount));
        mReceiptItem.setType(type);
        return mReceiptItem;
    }

    private String getIdOfTypeSelected() {
        String typeSelected = mTypeSpinner.getSelectedItem().toString();
        for (Type t : mTypeList) {
            if (t.getName().equals(typeSelected)) {
                return String.format("%d", t.getId());
            }
        }
        return "";
    }

    private class FetchTypesTask extends AsyncTask<ReceiptItem, Void, List<Type>> {
        private ArrayAdapter mTypeAdapter;
        public FetchTypesTask(ArrayAdapter typeAdapter) {
            mTypeAdapter = typeAdapter;
        }

        @Override
        public List<Type> doInBackground(ReceiptItem... params) {
            return ReceiptService.getInstance().fetchReceiptType();
        }

        @Override
        public void onPostExecute(List<Type> result) {
            mTypeAdapter.clear();
            mTypeList.clear();
            for (Type t : result) {
                mTypeAdapter.add(t.getName());
                mTypeList.add(t);
            }
            mTypeAdapter.notifyDataSetChanged();
        }
    }
}