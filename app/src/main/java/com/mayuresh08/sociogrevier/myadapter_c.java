package com.mayuresh08.sociogrevier;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapter_c extends FirebaseRecyclerAdapter<model_c,myadapter_c.myviewholder_c> {

    public myadapter_c(@NonNull FirebaseRecyclerOptions<model_c> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myadapter_c.myviewholder_c holder, int position, @NonNull model_c model)
    {
        holder.fullname.setText(model.getFullname());
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.description.setText(model.getDescription());
        holder.location.setText(model.getLocation());
        Glide.with(holder.img.getContext()).load(model.getCovid_posts()).into(holder.img);
    }

    @NonNull
    @Override
    public myadapter_c.myviewholder_c onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_p,parent,false);
        return new myadapter_c.myviewholder_c(view);
    }

    class myviewholder_c extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView fullname,date,time,description,location;
        public myviewholder_c(@NonNull View itemView) {
            super(itemView);
            location=(TextView)itemView.findViewById(R.id.p_location);
            img=(ImageView)itemView.findViewById(R.id.p_image);
            fullname=(TextView)itemView.findViewById(R.id.p_fullname);
            date=(TextView)itemView.findViewById(R.id.p_date);
            time=(TextView)itemView.findViewById(R.id.p_time);
            description=(TextView)itemView.findViewById(R.id.p_description);

        }
    }


}
