package com.example.eqdetector;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eqdetector.Utilities.DataUtils;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.EarthquakeViewHolder> {

    private final Context mContext;
    private final String[] mData;

    public EarthquakeAdapter(Context context,String[] data){
        mContext = context;
        mData = data;
    }

    @Override
    public EarthquakeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutForListItem = R.layout.earthquake_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForListItem,parent,false);

        return new EarthquakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EarthquakeAdapter.EarthquakeViewHolder holder, int position) {

        String[] data = mData[position].split("!");
        holder.earthquakeIcon.setImageResource(getIconId(data[1]));
        holder.date.setText(data[0]);
        holder.magnitude.setText(data[1]);
        holder.location.setText(data[2]);
        holder.magName.setText("ritcher scale");
        for(String d:data) {
            Log.d("eq-adapt",d );
        }


    }

    private int getIconId(String mag){
        float magnitude = Float.parseFloat(mag);

        if(magnitude >= 6.0){
            return R.drawable.ic_earthquake_red;
        }else if(magnitude >= 5.0 && magnitude < 6.0 ){
            return R.drawable.ic_earthquake_yellow;
        }else {
            return R.drawable.ic_earthquake_green;
        }
    }

    @Override
    public int getItemCount() {
        if(mData == null)return 0;
        return mData.length;
    }

    class EarthquakeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView earthquakeIcon;
        final TextView date;
        final TextView location;
        final TextView magnitude;
        final TextView magName;
        final ConstraintLayout constraintLayout;

        public EarthquakeViewHolder(View itemView) {
            super(itemView);
            earthquakeIcon = itemView.findViewById(R.id.earthquake_icon);
            date = itemView.findViewById(R.id.date);
            location = itemView.findViewById(R.id.earthquake_location);
            magnitude = itemView.findViewById(R.id.magnitude);
            magName = itemView.findViewById(R.id.mag_name);
            constraintLayout = itemView.findViewById(R.id.cl_list);

            constraintLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int pos = getAdapterPosition();
            Toast.makeText(mContext,"Try " + pos,Toast.LENGTH_LONG).show();

        }
    }
}
