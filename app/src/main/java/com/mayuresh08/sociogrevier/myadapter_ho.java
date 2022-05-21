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

public class myadapter_ho extends FirebaseRecyclerAdapter<model_ho,myadapter_ho.myviewholder_ho>
{

    public myadapter_ho(@NonNull FirebaseRecyclerOptions<model_ho> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder_ho holder, int position, @NonNull model_ho model)
    {
        holder.fullname.setText(model.getFullname());
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.description.setText(model.getDescription());
        holder.location.setText(model.getLocation());
        Glide.with(holder.img.getContext()).load(model.getHosp_posts()).into(holder.img);
    }

    @NonNull
    @Override
    public myviewholder_ho onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_p,parent,false);
        return new myviewholder_ho(view);
    }

    class myviewholder_ho extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView fullname,date,time,description,location;
        public myviewholder_ho(@NonNull View itemView) {
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

