package ml.parshev.healthcontrol;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        getFragmentManager().beginTransaction()
                .replace(R.id.form_content, getFragment(getIntent().getIntExtra("action",-1)))
                .commit();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_accept:
                finish();
                return false;

        }
        return super.onOptionsItemSelected(item);
    }

    private Fragment getFragment(int action) {
        switch(action) {
            case R.id.action_sugar:
                return new SugarFragment();
            case R.id.action_weight:
                return new WeightFragment();
            case R.id.action_pressure:
                return new PressureFragment();
        }
        return null;
    }

}
