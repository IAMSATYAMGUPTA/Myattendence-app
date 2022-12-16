package com.example.myattendence2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    ClassAdapter classAdapter;
    ArrayList<classitem>classitems=new ArrayList<>();
    RecyclerView recyclerView;
    Toolbar toolbar;
    DBhelper dBhelper;
    TextView textView;
    String subjectsname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.notetitle2);
        dBhelper = new DBhelper(this);

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(v -> showDialog());
        
        loaddata();

        // set toolbar
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.suubtitle_toolbar);
        ImageButton save = toolbar.findViewById(R.id.save);
        TextView savetext = toolbar.findViewById(R.id.save2);
        ImageButton back = toolbar.findViewById(R.id.back);
        title.setText("Attendence App");
        title.setTextSize(20);
        subtitle.setVisibility(View.GONE);
        save.setVisibility(View.INVISIBLE);
        savetext.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);

        // set recycleview
        recyclerView = findViewById(R.id.classrecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        classAdapter = new ClassAdapter(this,classitems);
        recyclerView.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

    }

    private void loaddata() {
        Cursor cursor = dBhelper.getClassTable();
        classitems.clear();
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(dBhelper.C_ID));
            String classname = cursor.getString(cursor.getColumnIndexOrThrow(dBhelper.CLASS_NAME_KEY));
            subjectsname = cursor.getString(cursor.getColumnIndexOrThrow(dBhelper.SUBJECT_NAME_KEY));
            classitems.add(new classitem(id,classname,subjectsname));
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.class_dialog,null);
        builder.setView(view);
        EditText acname = view.findViewById(R.id.class_name);
        EditText acsub = view.findViewById(R.id.subject_name);
        Button addbtn = view.findViewById(R.id.add_btn);
        Button cancelbtn = view.findViewById(R.id.cancel_btn);
        AlertDialog alertDialog = builder.show();
        cancelbtn.setOnClickListener(v -> alertDialog.dismiss());
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(View.GONE);
                String alcname = acname.getText().toString();
                String alcsub = acsub.getText().toString();
                if(alcname.length() >=1 && alcsub.length() >= 1){
                    long cid = dBhelper.addClass(alcname,alcsub);
                    classitem classitemm = new classitem(cid,alcname,alcsub);
                    classitems.add(classitemm);
                    alertDialog.dismiss();
                }
                else if (alcname.length() == 0 && alcsub.length() >= 1){
                    acname.setError("");
                }
                else if (alcname.length() >= 1 && alcsub.length() == 0){
                    acsub.setError("");
                }
                else {
                    acname.setError("");
                    acsub.setError("");
                }

            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showupdatedialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showupdatedialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.classupdatedialog,null);
        builder.setView(view);
        EditText uacname = view.findViewById(R.id.uclass_name);
        EditText uacsub = view.findViewById(R.id.usubject_name);
        uacname.setText(classitems.get(position).getClassname());
        uacsub.setText(classitems.get(position).getSubjectname());
        Button uaddbtn = view.findViewById(R.id.uadd_btn);
        Button ucancelbtn = view.findViewById(R.id.ucancel_btn);
        AlertDialog alertDialog = builder.show();
        ucancelbtn.setOnClickListener(v -> alertDialog.dismiss());
        uaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alcname = uacname.getText().toString();
                String alcsub = uacsub.getText().toString();
                dBhelper.updateClass(classitems.get(position).getCid(),alcname,alcsub);
                classitems.get(position).setClassname(alcname);
                classitems.get(position).setSubjectname(alcsub);
                classAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });
    }

    private void deleteClass(int position) {
        dBhelper.deleteClass(classitems.get(position).getCid());
        classitems.remove(position);
        classAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(subjectsname!=null){
            textView.setVisibility(View.GONE);
        }
    }
}