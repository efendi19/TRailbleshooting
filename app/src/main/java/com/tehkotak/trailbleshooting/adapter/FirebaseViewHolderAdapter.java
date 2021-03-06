package com.tehkotak.trailbleshooting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tehkotak.trailbleshooting.DataModel;
import com.tehkotak.trailbleshooting.R;

import java.util.ArrayList;
import java.util.List;

public class FirebaseViewHolderAdapter extends RecyclerView.Adapter<FirebaseViewHolderAdapter.Vholder> {

    Context context;
    List<DataModel> arrayListModel;
    private OnItemClickListener mListener;

    public FirebaseViewHolderAdapter(Context context, ArrayList<DataModel> arrayListModel) {
        this.context = context;
        this.arrayListModel = arrayListModel;
    }

    @NonNull
    @Override
    public FirebaseViewHolderAdapter.Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return new Vholder(LayoutInflater.from(context).inflate(R.layout.item_daily_report, parent, false));
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_daily_report, parent, false);
        Vholder report = new Vholder(view, mListener);
        return report;

    }

    @Override
    public void onBindViewHolder(@NonNull FirebaseViewHolderAdapter.Vholder holder, int position) {
        DataModel model = arrayListModel.get(position);

        TextView strDateTime = holder.tvDatetime;
        String imageProblem = arrayListModel.get(position).getImageURL();
        TextView strKomen = holder.tvKomen;
        TextView strLatitude = holder.tvLati;
        TextView strLongitude = holder.tvLongi;

        strDateTime.setText(model.getDatetime());
        Picasso.get()
                .load(imageProblem)
                .placeholder(R.drawable.bg_people)
                .into(holder.imgRep);
        strKomen.setText(model.getKomentar());
        strLatitude.setText(model.getLatitude());
        strLongitude.setText(model.getLongitude());
    }

    @Override
    public int getItemCount() {
        return arrayListModel.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        //void onItemButtonClick if needed
    }


    public class Vholder extends RecyclerView.ViewHolder {

        TextView tvKomen, tvLati;
        TextView tvLongi, tvDatetime;
        ImageView imgRep;

        public Vholder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            tvDatetime = (TextView) itemView.findViewById(R.id.tvdatetime);
            imgRep = (ImageView) itemView.findViewById(R.id.img_report);
            tvKomen = (TextView) itemView.findViewById(R.id.tv_kom);
            tvLati = (TextView) itemView.findViewById(R.id.tv_lat_inp);
            tvLongi = (TextView) itemView.findViewById(R.id.tv_longi_inp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

}
