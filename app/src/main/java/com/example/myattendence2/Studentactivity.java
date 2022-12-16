package com.example.myattendence2;

import static com.example.myattendence2.R.id.studentrecycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Studentactivity extends AppCompatActivity implements onItemClickListner {
    Toolbar toolbar;
    RecyclerView recyclerView;
    Bundle bundle;
    String classname,subjectname,pposition;
    int cid;
    ArrayList<Studentitem>studentitems=new ArrayList<>();
    Studentadapter studentadapter;
    String status;
    DBhelper dBhelper;
    private MyCalender calender;
    TextView subtitle;
    TextView textView ;
    String name;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentactivity);

        textView = findViewById(R.id.notetitle);
        fab = findViewById(R.id.fab_main2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showstudentdialog();
            }
        });

        calender = new MyCalender();
        dBhelper = new DBhelper(this);


        toolbar = findViewById(R.id.toolbar);
        subtitle = toolbar.findViewById(R.id.suubtitle_toolbar);
        // set toolbar
        toolbar();
        loaddata();

        recyclerView = (RecyclerView) findViewById(studentrecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentadapter = new Studentadapter(getApplicationContext(),studentitems,this);
        recyclerView.setAdapter(studentadapter);
        loadstatusdata();
    }

    private void loaddata() {
        Cursor cursor = dBhelper.getStudentTable(cid);
        studentitems.clear();
        while(cursor.moveToNext()){
            long sid = cursor.getLong(cursor.getColumnIndexOrThrow(dBhelper.S_ID));
            int roll = cursor.getInt(cursor.getColumnIndexOrThrow(dBhelper.STUDENT_ROLL_KEY));
            name = cursor.getString(cursor.getColumnIndexOrThrow(dBhelper.STUDENT_NAME_KEY));
            studentitems.add(new Studentitem(sid,roll,name));
        }
        cursor.close();
    }

    public void toolbar(){
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.suubtitle_toolbar);
        ImageButton save = toolbar.findViewById(R.id.save);
        TextView savetext = toolbar.findViewById(R.id.save2);
        ImageButton back = toolbar.findViewById(R.id.back);
        bundle = new Bundle();
        if(bundle != null) {
            bundle = getIntent().getExtras();
            classname = bundle.getString("toolbartitle");
            subjectname = bundle.getString("toolbarsubtitle");
            pposition = bundle.getString("position", String.valueOf(-1));
            cid = Integer.parseInt(bundle.getString("CID", String.valueOf(-1)));
            title.setText(classname);
            subtitle.setText(subjectname + " | " + calender.getdate());
        }
        back.setOnClickListener(v -> onBackPressed());
        save.setOnClickListener(v -> savestatus());
        savetext.setOnClickListener(v -> savestatus());
        toolbar.inflateMenu(R.menu.menubar);
        toolbar.setOnMenuItemClickListener(menuItem -> onMenuItemSelected(menuItem));
    }

    private void savestatus() {
        for(Studentitem studentitem :studentitems){
            String status = studentitem.getStatus();
            if(status.length()==0) {
                status="A";
            }
            long value = dBhelper.addStatus(studentitem.getSid(),cid,calender.getdate(),status);
            if(value == -1)dBhelper.updateStatus(studentitem.getSid(),calender.getdate(),status);

        }
        Toast.makeText(this, "Attendence save...", Toast.LENGTH_SHORT).show();
    }

    public void loadstatusdata(){
        for(Studentitem studentitem :studentitems){
            String status = dBhelper.getStatus(studentitem.getSid(),calender.getdate());
            if(status!=null) studentitem.setStatus(status);
            else studentitem.setStatus("");
        }
        studentadapter.notifyDataSetChanged();
    }

    private boolean onMenuItemSelected(MenuItem menuItem) {
//        if(menuItem.getItemId()==R.id.add_student){
//            showstudentdialog();
//        }
        if(menuItem.getItemId()==R.id.change_date){
            showCalender();
        }
        else if(menuItem.getItemId()==R.id.show_attendence_sheet){
            opensheetlist();
        }
        return true;
    }

    private void opensheetlist() {
        long [] idArray = new long[studentitems.size()];
        String [] nameArray = new String[studentitems.size()];
        int [] rollArray = new int[studentitems.size()];

        for(int i=0;i<idArray.length;i++){
            idArray[i] = studentitems.get(i).getSid();
        }
        for(int i=0;i<nameArray.length;i++){
            nameArray[i] = studentitems.get(i).getName();
        }
        for(int i=0;i<rollArray.length;i++){
            rollArray[i] = studentitems.get(i).getRoll();
        }

        Intent intent = new Intent(getApplicationContext(),sheetlistactiivity.class);
        bundle.putString("cid", String.valueOf(cid));
        intent.putExtra("idArray", idArray);
        intent.putExtra("nameArray", nameArray);
        intent.putExtra("rollArray", rollArray);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showCalender() {
        calender.show(getSupportFragmentManager(),"");
        calender.setOnCalenderOkClickListener((this::onCalenderOkclicked));
    }

    private void onCalenderOkclicked(int year, int month, int day) {
        calender.setdate(year, month, day);
        subtitle.setText(subjectname + " | " + calender.getdate());
        loadstatusdata();
    }

    private void showstudentdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.student_dialog,null);
        builder.setView(view);
        EditText studentroll = view.findViewById(R.id.student_rollno);
        EditText studentname = view.findViewById(R.id.student_name);
        Button addbtn = view.findViewById(R.id.sadd_btn);
        Button cancelbtn = view.findViewById(R.id.scancel_btn);
        AlertDialog alertDialog = builder.show();
        cancelbtn.setOnClickListener(v -> alertDialog.dismiss());
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alsroll = studentroll.getText().toString();
                String alsname = studentname.getText().toString();

                if(alsroll.length() >=1 && alsname.length() >= 1){
                    textView.setVisibility(View.GONE);
                    long sid = dBhelper.addStudent(cid, Integer.parseInt((alsroll)),alsname);
                    Studentitem studentitem = new Studentitem(sid,Integer.parseInt(alsroll),alsname);
                    studentitems.add(studentitem);
                    studentadapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
                else if (alsroll.length() == 0 && alsname.length() >= 1){
                    studentroll.setError("");
                }
                else if (alsroll.length() >= 1 && alsname.length() == 0){
                    studentname.setError("");
                }
                else {
                    studentroll.setError("");
                    studentname.setError("");
                }
//                studentadapter.notifyDataSetChanged(studentitems.size()-1);
            }
        });
    }

    @Override
    public void onItemClick(int pos) {
        status = studentitems.get(pos).getStatus();
        if(status.equals("P")){
            status = "A";
        }
        else{
            status = "P";
        }
        studentitems.get(pos).setStatus(status);
        studentadapter.notifyDataSetChanged();
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
        View view = LayoutInflater.from(this).inflate(R.layout.student_dialog,null);
        builder.setView(view);
        EditText ustudentroll = view.findViewById(R.id.student_rollno);
        EditText ustudentname = view.findViewById(R.id.student_name);
        Button uaddbtn = view.findViewById(R.id.sadd_btn);
        Button ucancelbtn = view.findViewById(R.id.scancel_btn);
        TextView titleview = view.findViewById(R.id.studenttextview);
        titleview.setText("Update Student detail");
        uaddbtn.setText("UPDATE");
        ustudentroll.setText(studentitems.get(position).getRoll()+"");
        ustudentroll.setEnabled(false);
        ustudentroll.setTextColor(getResources().getColor(R.color.lightblack));
        ustudentname.setText(studentitems.get(position).getName());
        AlertDialog alertDialog = builder.show();
        ucancelbtn.setOnClickListener(v -> alertDialog.dismiss());
        uaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ualsname = ustudentname.getText().toString();
                dBhelper.updateStudent(studentitems.get(position).getSid(),ualsname);
                studentitems.get(position).setName(ualsname);
                studentadapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });
    }

    private void deleteClass(int position) {
        dBhelper.deleteStudent(studentitems.get(position).getSid());
        studentitems.remove(position);
        studentadapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (name!=null){
            textView.setVisibility(View.GONE);
        }
    }
}