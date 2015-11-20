package ml.parshev.healthcontrol;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by mparshev on 14.11.15.
 */
public class HeightFragment extends Fragment {

    NumberPicker numberPickerHeight;

    public HeightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_height, container, false);
        numberPickerHeight = (NumberPicker)view.findViewById(R.id.numberPickerHeight);
        numberPickerHeight.setMinValue(100);
        numberPickerHeight.setMaxValue(299);
        int height = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(HealthDb.PREF_HEIGHT,getResources().getInteger(R.integer.default_height));
        numberPickerHeight.setValue(height);

        getActivity().setTitle(R.string.title_height);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                int height = numberPickerHeight.getValue();
                getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(HealthDb.PREF_HEIGHT, height).commit();
                getActivity().getFragmentManager().popBackStack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
