package ml.parshev.healthcontrol;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by mparshev on 03.11.15.
 */
public class RecyclerViewCursorAdapter extends RecyclerView.Adapter<RecyclerViewCursorAdapter.ViewHolder> implements View.OnLongClickListener {

    private Cursor mCursor;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    public RecyclerViewCursorAdapter() {
        mCursor = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        v.setOnLongClickListener(this);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mCursor != null && mCursor.moveToPosition(position)) {
            holder.mView.setTag(HealthDb.getLong(mCursor, HealthDb.MEASURES._ID));
            ImageView imageView = (ImageView) holder.mView.findViewById(R.id.imageView);
            switch(HealthDb.getInt(mCursor, HealthDb.MEASURES.TYPE)) {
                case HealthDb.MEASURE_TYPE_PRESSURE:
                    imageView.setImageResource(R.drawable.ic_action_pressure);
                    break;
                case HealthDb.MEASURE_TYPE_SUGAR:
                    imageView.setImageResource(R.drawable.ic_action_sugar);
                    break;
                case HealthDb.MEASURE_TYPE_WEIGHT:
                    imageView.setImageResource(R.drawable.ic_action_weight);
                    break;
            }
            ((TextView) holder.mView.findViewById(R.id.card_time))
                    .setText(DateFormat.format("d.M H:mm", HealthDb.getDate(mCursor, HealthDb.MEASURES.TIME)));
            ((TextView) holder.mView.findViewById(R.id.card_title)).setText(HealthDb.getString(mCursor, HealthDb.MEASURES.TITLE));
            ((TextView) holder.mView.findViewById(R.id.card_value)).setText(HealthDb.getString(mCursor, HealthDb.MEASURES.VALUE));
            ((TextView) holder.mView.findViewById(R.id.card_text)).setText(HealthDb.getString(mCursor, HealthDb.MEASURES.TEXT));
            holder.mView.setBackgroundColor(HealthDb.getInt(mCursor, HealthDb.MEASURES.COLOR));
        }
    }

    @Override
    public int getItemCount() {
        if(mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    public void swapCursor(Cursor cursor) {
        notifyDataSetChanged();
        mCursor = cursor;
    }

    @Override
    public boolean onLongClick(View view) {
        view.setBackgroundColor(Color.GRAY);
        return true;
    }

}
