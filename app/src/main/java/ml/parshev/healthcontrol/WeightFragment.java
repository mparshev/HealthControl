package ml.parshev.healthcontrol;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeightFragment extends Fragment {

    NumberPicker numberPickerWeight;

    int mHeight;

    public WeightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_weight, container, false);
        numberPickerWeight = (NumberPicker) view.findViewById(R.id.numberPickerWeight);
        numberPickerWeight.setMinValue(0);
        numberPickerWeight.setMaxValue(9999);
        int weight = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(HealthDb.PREF_WEIGHT, getResources().getInteger(R.integer.default_weight));
        numberPickerWeight.setFormatter(new NumberPicker.Formatter() {

            @Override
            public String format(int i) {
                float f = (float) i / 10;
                return String.format("%.1f", f);
            }
        });
        numberPickerWeight.setValue(weight);
        try {
            Method method = numberPickerWeight.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(numberPickerWeight, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mHeight = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(HealthDb.PREF_HEIGHT, 0);
        if (mHeight == 0) {
            Toast.makeText(getActivity(), R.string.enter_your_height_first, Toast.LENGTH_LONG).show();
        }

        getActivity().setTitle(R.string.title_weight);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_weight, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case android.R.id.home:
            case R.id.action_accept:
                if (mHeight == 0) {
                    Toast.makeText(getActivity(), R.string.enter_your_height_first, Toast.LENGTH_LONG).show();
                    return true;
                }
                int weight = numberPickerWeight.getValue();
                getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(HealthDb.PREF_WEIGHT, weight).commit();
                float imt = 1000f * weight / mHeight / mHeight;
                int color = getResources().getColor(R.color.colorNorm);
                int resId =  R.string.imt_norm;
                if (imt < 18.5) {
                    color = getResources().getColor(R.color.colorLow);
                    resId = R.string.imt_low;
                }
                if (imt > 25) {
                    color = getResources().getColor(R.color.colorWarn);
                    resId = R.string.imt_hi;
                }
                if (imt > 30) {
                    color = getResources().getColor(R.color.colorCrit);
                    resId = R.string.imt_fat;
                }
                getActivity().getContentResolver().insert(HealthDb.MEASURES._URI,
                        HealthDb.MEASURES.getContentValues(
                                new Date(),
                                HealthDb.MEASURE_TYPE_WEIGHT,
                                getString(R.string.title_weight),
                                String.format("%.1f", weight / 10f),
                                color, String.format(getString(resId), imt)));
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
