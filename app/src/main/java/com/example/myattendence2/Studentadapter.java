package com.example.myattendence2;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Studentadapter extends RecyclerView.Adapter<Studentadapter.Myviewholder> {
    Context context;
    ArrayList<Studentitem> studentitems;
    onItemClickListner listner;

    public Studentadapter(Context context, ArrayList<Studentitem> studentitems, onItemClickListner listner) {
        this.context = context;
        this.studentitems = studentitems;
        this.listner = listner;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_item,parent,false);
        return new Myviewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        holder.sroll.setText(studentitems.get(position).getRoll()+"");
        holder.sname.setText(studentitems.get(position).getName());
        holder.status.setText(studentitems.get(position).getStatus());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onItemClick(holder.getAdapterPosition());
            }
        });
        holder.cardView.setCardBackgroundColor(getcolour(position));
    }

    private int getcolour(int position) {
//        Toast.makeText(context, "fffff", Toast.LENGTH_SHORT).show();
        String status = studentitems.get(position).getStatus();
//        String status = "P";
        if(status.equals("P")) {
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context, R.color.present)));}
        else if(status.equals("A")){
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.absent)));}

        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.white)));
    }


    @Override
    public int getItemCount() {
        return studentitems.size();
    }


    public class Myviewholder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView sroll,sname,status;
        CardView cardView;
        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            sroll = itemView.findViewById(R.id.roll);
            sname = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.cardview);
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),0,0,"EDIT");
            menu.add(getAdapterPosition(),1,0,"DELETE");
        }
    }
}
interface onItemClickListner{

    public void onItemClick(int pos);
}
