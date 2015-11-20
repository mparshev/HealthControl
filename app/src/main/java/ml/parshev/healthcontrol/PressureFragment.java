package ml.parshev.healthcontrol;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.NumberPicker;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class PressureFragment extends Fragment {

    private NumberPicker numberPickerSys;
    private NumberPicker numberPickerDia;
    private NumberPicker numberPickerPulse;
    private CheckBox checkBoxArrythmia;

    public PressureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pressure, container, false);
        getActivity().setTitle(R.string.title_pressure);
        setHasOptionsMenu(true);
        numberPickerSys = (NumberPicker) view.findViewById(R.id.numberPickerSys);
        numberPickerSys.setMinValue(0);
        numberPickerSys.setMaxValue(999);
        numberPickerSys.setValue(120);
        numberPickerDia = (NumberPicker) view.findViewById(R.id.numberPickerDia);
        numberPickerDia.setMinValue(0);
        numberPickerDia.setMaxValue(999);
        numberPickerDia.setValue(80);
        numberPickerPulse = (NumberPicker) view.findViewById(R.id.numberPickerPulse);
        numberPickerPulse.setMinValue(0);
        numberPickerPulse.setMaxValue(999);
        numberPickerPulse.setValue(60);
        checkBoxArrythmia = (CheckBox) view.findViewById(R.id.checkBoxArrythmia);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case android.R.id.home:
            case R.id.action_accept:
                int sys = numberPickerSys.getValue();
                int dia = numberPickerDia.getValue();
                int pulse = numberPickerPulse.getValue();
                boolean arrythm = checkBoxArrythmia.isChecked();
                int color = getResources().getColor(R.color.colorNorm);
                String text = "";
                if(sys < 90 || dia < 60) {
                    color = getResources().getColor(R.color.colorLow);
                }
                if(sys > 130) {
                    color = getResources().getColor(R.color.colorWarn);
                }
                if(dia > 85) {
                    color = getResources().getColor(R.color.colorWarn);
                }
                if((sys > 130 && dia > 85)||(sys > 140)||(dia > 90)) {
                    color = getResources().getColor(R.color.colorCrit);
                }
                if(arrythm) {
                    color = getResources().getColor(R.color.colorArrythmia);
                    text = getString(R.string.arrythmia);
                }
                getActivity().getContentResolver().insert(HealthDb.MEASURES._URI,
                        HealthDb.MEASURES.getContentValues(
                                new Date(),
                                HealthDb.MEASURE_TYPE_PRESSURE,
                                getString(R.string.title_pressure),
                                String.format("%d/%d %d", sys, dia, pulse),
                                color,
                                text));
                return false;
        }
        return false;
    }

}
