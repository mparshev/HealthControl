package ml.parshev.healthcontrol;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class SugarFragment extends Fragment {

    NumberPicker numberPickerSugar;
    RadioButton radioButtonSugar0;
    RadioButton radioButtonSugar1;
    RadioButton radioButtonSugar2;

    public SugarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_sugar, container, false);
        numberPickerSugar = (NumberPicker) view.findViewById(R.id.numberPickerSugar);
        radioButtonSugar0 = (RadioButton) view.findViewById(R.id.radioButtonSugar0);
        radioButtonSugar1 = (RadioButton) view.findViewById(R.id.radioButtonSugar1);
        radioButtonSugar2 = (RadioButton) view.findViewById(R.id.radioButtonSugar2);
        numberPickerSugar.setMinValue(0);
        numberPickerSugar.setMaxValue(999);
        numberPickerSugar.setFormatter(new NumberPicker.Formatter() {

            @Override
            public String format(int i) {
                float f = (float) i / 10;
                return String.format("%.1f", f);
            }
        });
        numberPickerSugar.setValue(46);
        try {
            Method method = numberPickerSugar.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(numberPickerSugar, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        getActivity().setTitle(R.string.title_sugar);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case android.R.id.home:
            case R.id.action_accept:
                float sugar = numberPickerSugar.getValue()/10f;
                boolean sugar0 = radioButtonSugar0.isChecked();
                boolean sugar1 = radioButtonSugar1.isChecked();
                boolean sugar2 = radioButtonSugar2.isChecked();
                int color = getResources().getColor(R.color.colorNorm);
                String text = getString(R.string.sugar0);
                if (sugar < 3.3f) {
                    color = getResources().getColor(R.color.colorLow);
                }
                if (sugar0 && sugar > 5.5f) {
                    color = getResources().getColor(R.color.colorWarn);
                }
                if (sugar0 && sugar > 6.0f) {
                    color = getResources().getColor(R.color.colorCrit);
                }
                if(sugar1) text = getString(R.string.sugar1);
                if (sugar1 && sugar > 7.7f) {
                    color = getResources().getColor(R.color.colorWarn);
                }
                if (sugar1 && sugar > 11f) {
                    color = getResources().getColor(R.color.colorCrit);
                }
                if(sugar2) text = getString(R.string.sugar2);
                if (sugar2 && sugar > 5.5f) {
                    color = getResources().getColor(R.color.colorWarn);
                }
                if (sugar2 && sugar > 6.0f) {
                    color = getResources().getColor(R.color.colorCrit);
                }
                getActivity().getContentResolver().insert(HealthDb.MEASURES._URI,
                        HealthDb.MEASURES.getContentValues(
                                new Date(),
                                HealthDb.MEASURE_TYPE_SUGAR,
                                getString(R.string.title_sugar),
                                String.format("%.1f", sugar),
                                color,
                                text));
                return false;
            case R.id.action_height:
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.form_content, new HeightFragment())
                        .addToBackStack(null)
                        .commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
