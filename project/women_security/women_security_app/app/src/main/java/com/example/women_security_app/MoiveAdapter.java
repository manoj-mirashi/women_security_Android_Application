package com.example.women_security_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MoiveAdapter extends  RecyclerView.Adapter<MoiveAdapter.MoiveHolder> {

    private Context context;
    private List<petrolpump> petrolpumpList;

    public MoiveAdapter(Context context, List<petrolpump> petrolpumpList) {
        this.context = context;
        this.petrolpumpList = petrolpumpList;
    }


    @NonNull
    @Override
    public MoiveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MoiveHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoiveHolder holder, int position) {

        petrolpump petrolpump = petrolpumpList.get(position);
        holder.txtrating.setText(petrolpump.getRating().toString());
        holder.txtname.setText(petrolpump.getName());
        holder.txtopen.setText(petrolpump.getOpen_now());

//        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context,details.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("name",petrolpump.getName());
//                bundle.putDouble("rating",petrolpump.getRating());
//                bundle.putString("vicinity",petrolpump.getOpen_now());
//
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return petrolpumpList.size();
    }

    public class MoiveHolder extends RecyclerView.ViewHolder{

    TextView txtname,txtopen,txtrating;
    ConstraintLayout constraintLayout;


    public MoiveHolder(@NonNull View itemView) {
        super(itemView);

        txtname = itemView.findViewById(R.id.title_tv);
        txtopen = itemView.findViewById(R.id.overview_tv);
        txtrating = itemView.findViewById(R.id.rating);
        constraintLayout = itemView.findViewById(R.id.main_layout);
    }
}

}
