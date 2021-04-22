package is.hi.hbv601g.gjaldbrotapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.sql.Time;
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
public class ReceiptEditFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {
    private static String TAG = "DATE_PICKER";

    private DatePicker mDatePicker;

    private EditText mAmountField;
    private Button mTimeField;
    private int mHour, mMinute;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receipt_edit, container, false);
        view.setBackgroundColor(Color.WHITE);
        // Dagsetningar veljarinn.
        mDatePicker = (DatePicker) view.findViewById(R.id.edit_receipt_datepicker);
        // Svæði fyrir upphæðina.
        mAmountField = (EditText) view.findViewById(R.id.receipt_edit_et_amount);
        // Hnappur til að velja tímann.
        mTimeField = (Button) view.findViewById(R.id.receipt_edit_et_time);
        mTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        // Spinner með
        mTypeSpinner = (Spinner) view.findViewById(R.id.receipt_edit_sp_type);
        ArrayAdapter<CharSequence> typeAdapter =
                new ArrayAdapter(this.getContext(),
                        android.R.layout.simple_spinner_item,
                        new ArrayList(Arrays.asList("Loading ...")));

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(typeAdapter);

        new FetchTypesTask(typeAdapter).execute();
        
        initializeForm();

        return view;
    }

    public long getReceiptId() {
        return mReceiptItem.getId();
    }

    /**
     * Aðalfall klasans. Les úr þeim upplýsingum sem að notandinn hefur slegið inn og skilar
     * ReceiptItem út frá þeim.
     * @return
     * @throws Exception
     */
    public ReceiptItem getReceiptItem() throws Exception {
        String date = getDateSelected();
        String amount = mAmountField.getText().toString();
        String time = mTimeField.getText().toString();
        long type = getIdOfTypeSelected();

        if (date.equals("") || amount.equals("") || time.equals("") || type == -1) {
            throw new Exception("Input error, not all fields are filled in");
        }

        try {
            mReceiptItem.setFormattedDateWithTime(date);
        }
        catch (Exception e) {
            throw new Exception("Could not parse date, format yyyy-MM-dd'T'hh:mm:ss");
        }

        mReceiptItem.setAmount(Integer.parseInt(amount));
        mReceiptItem.setType(mTypeSpinner.getSelectedItem().toString());
        mReceiptItem.setTypeId(type);
        return mReceiptItem;
    }



    /**
     * Upphafstillir upplýsingarnar í forminu eftir kvittuninni
     */
    private void initializeForm() {
        setDate(mReceiptItem.getDate());
        if(mReceiptItem.getAmount() != 0) {
            mAmountField.setText(String.format("%d", mReceiptItem.getAmount()));
        }
    }

    /**
     * Skilar dagsetningu á forminu yyyy-MM-dd'T'hh:mm:ss eftir því hvað er valið í mDatePicker og
     * mTimeField
     * @return
     */
    private String getDateSelected() {
        String year = String.valueOf(mDatePicker.getYear());
        String month = String.valueOf(mDatePicker.getMonth()+1);
        String day = String.valueOf(mDatePicker.getDayOfMonth());
        Log.i(TAG, String.format("%s-%s-%sT%d:%d", year, month, day, mHour, mMinute));
        return String.format("%s-%s-%sT%d:%d:00", year, month, day, mHour, mMinute);
    }

    /**
     * Upphafsstillir mDatePicker og mTimeField eftir dagssetningu.
     * @param date
     */
    private void setDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        mDatePicker.updateDate(year, month, day);
        mTimeField.setText((mHour < 10 ? "0": "" )+ mHour + ":" + (mMinute<10 ? "0": "")+ mMinute);
    }

    /**
     * Fer í gegnum listann af kvittunar-tegundum sem notandinn er með skilgreindar og skilar
     * id þeirrar sem er valin í mTypeSpinner.
     * @return
     */
    private long getIdOfTypeSelected() {
        String typeSelected = mTypeSpinner.getSelectedItem().toString();
        for (Type t : mTypeList) {
            if (t.getName().equals(typeSelected)) {
                return t.getId();
            }
        }
        return -1;
    }

    /**
     * Fall sem að birtir TimePickerDialog. Þarf að vera hér til að hafa aðgang að this.getContext()
     */
    private void showTimePicker() {
        TimePickerDialog timePickerDialog =
                new TimePickerDialog(this.getContext(),
                        this,
                        mHour,
                        mMinute,
                        true);
        timePickerDialog.show();
    }

    /**
     * Útfærir onTimeSet fyrir TimePickerDialog.OnTimeSetListener
     * Uppfærir textan á tíma hnappinum sem tímann sem var valinn.
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mMinute = minute;
        mHour = hourOfDay;
        mTimeField.setText((mHour < 10 ? "0": "" )+ hourOfDay + ":" + (mMinute<10 ? "0": "")+ minute);
    }

    /**
     * Innri klasi sem að sækir kvittunar-tegundir á vefþjónustu og uppfærir stökin mTypeSpinner
     */
    private class FetchTypesTask extends AsyncTask<ReceiptItem, Void, List<Type>> {
        private final ArrayAdapter mTypeAdapter;
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