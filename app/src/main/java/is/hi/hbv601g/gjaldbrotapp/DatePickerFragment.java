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
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatePickerFragment extends Fragment {

    private Spinner yearSpinner;
    private Spinner monthSpinner;
    private Spinner daySpinner;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance(Context parentContext) {
        DatePickerFragment fragment = new DatePickerFragment();
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
        View view = inflater.inflate(R.layout.fragment_date_picker, container, false);

        yearSpinner = (Spinner) view.findViewById(R.id.datepicker_sp_year);
        monthSpinner = (Spinner) view.findViewById(R.id.datepicker_sp_month);
        daySpinner = (Spinner) view.findViewById(R.id.datepicker_sp_day);

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.years, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setOnItemSelectedListener(new OnMonthSelected());
        monthSpinner.setSelection(3); // TODO setja mánuð og ár default sem current mon/year

        return view;
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
            ArrayAdapter<Integer> dayAdapter = new ArrayAdapter<Integer>(DatePickerFragment.this.getContext(), android.R.layout.simple_spinner_dropdown_item, days);
            dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            daySpinner.setAdapter(dayAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView parent) {

        }
    }
}