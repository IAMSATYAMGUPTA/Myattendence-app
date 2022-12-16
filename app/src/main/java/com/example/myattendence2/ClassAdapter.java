package com.example.myattendence2;

import static android.view.Gravity.*;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.Myviewholder> {
    Context context;
    ArrayList<classitem> classitems;

    public ClassAdapter(Context context, ArrayList<classitem> classitems) {
        this.context = context;
        this.classitems = classitems;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.class_item,parent,false);
        return new Myviewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        int num = position;
        holder.cname.setText((classitems.get(position).getClassname()));
        holder.csub.setText((classitems.get(position).getSubjectname()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Studentactivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("toolbartitle",classitems.get(num).getClassname());
                bundle.putString("toolbarsubtitle",classitems.get(num).getSubjectname());
                bundle.putString("position", String.valueOf(num));
                bundle.putString("CID", String.valueOf(classitems.get(num).getCid()));
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return classitems.size();
    }


    public class Myviewholder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView cname,csub;
        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            cname = itemView.findViewById(R.id.nameclass);
            csub = itemView.findViewById(R.id.clss_subject);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),0,0,"EDIT");
            menu.add(getAdapterPosition(),1,0,"DELETE");
        }
    }
}
