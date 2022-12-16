package com.example.myattendence2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;

public class SheetActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);

        //set toolbar
        settoolbar();

        //set table
        showtable();

    }

    private void settoolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.suubtitle_toolbar);
        ImageButton save = toolbar.findViewById(R.id.save);
        TextView savetext = toolbar.findViewById(R.id.save2);
        ImageButton back = toolbar.findViewById(R.id.back);
        title.setText("STATUS");
        subtitle.setVisibility(View.GONE);
        back.setOnClickListener(v -> onBackPressed());
        save.setVisibility(View.INVISIBLE);
        savetext.setVisibility(View.INVISIBLE);
    }

    private void showtable() {
        DBhelper dBhelper = new DBhelper(this);
        TableLayout tableLayout = findViewById(R.id.tablelayout);
        long [] idArray = getIntent().getLongArrayExtra("idArray");
        String [] nameArray = getIntent().getStringArrayExtra("nameArray");
        int [] rollArray = getIntent().getIntArrayExtra("rollArray");
        String month = getIntent().getStringExtra("month");

        int DAY_IN_MONTH = getDayInMonth(month);

        // row setup
        int rowsize = idArray.length + 1;
        TableRow[] rows = new TableRow[rowsize];
        TextView[] roll_tvs = new TextView[rowsize];
        TextView[] name_tvs = new TextView[rowsize];
        TextView[] [] status_tvs = new TextView[rowsize][DAY_IN_MONTH+1];
        for(int i = 0;i<rowsize;i++){
            roll_tvs[i] = new TextView(this);
            name_tvs[i] = new TextView(this);
            for(int j = 1; j<= DAY_IN_MONTH;j++){
                status_tvs[i][j] = new TextView(this);
            }
        }

        // header
        roll_tvs[0].setText("Roll");
        roll_tvs[0].setTypeface(name_tvs[0].getTypeface(), Typeface.BOLD);
        name_tvs[0].setText("Name");
        name_tvs[0].setTypeface(name_tvs[0].getTypeface(), Typeface.BOLD);
        for(int i = 1; i<= DAY_IN_MONTH;i++){
            status_tvs[0][i].setText(String.valueOf(i));
            status_tvs[0][i].setTypeface(status_tvs[0][i].getTypeface(), Typeface.BOLD);
        }
        for(int i = 1; i< rowsize;i++){
            roll_tvs[i].setText(String.valueOf(rollArray[i-1]));
            name_tvs[i].setText(nameArray[i-1]);
            for(int j = 1; j<= DAY_IN_MONTH;j++){
                String day = String.valueOf(j);
                if(day.length()==1)day="0";
                String date = day+"."+month;
                String status = dBhelper.getStatus(idArray[i-1],date);// 10/10/22
                status_tvs[i][j].setText(status);

            }
        }

        for(int i =0;i<rowsize;i++){
            rows[i]=new TableRow(this);
            if(i%2==0)rows[i].setBackgroundColor(Color.parseColor("#EEEEEE"));
            else rows[i].setBackgroundColor(Color.parseColor("#E4E4E4"));;
            roll_tvs[i].setPadding(16,16,16,16);
            name_tvs[i].setPadding(16,16,16,16);
            rows[i].addView(roll_tvs[i]);
            rows[i].addView(name_tvs[i]);
            for(int j = 1;j<=DAY_IN_MONTH;j++){
                status_tvs[i][j].setPadding(16,16,16,16);
                status_tvs[i][j].setTextColor(Color.parseColor("#FF000000"));
                name_tvs[i].setTextColor(Color.parseColor("#FF000000"));
                roll_tvs[i].setTextColor(Color.parseColor("#FF000000"));
                rows[i].addView(status_tvs[i][j]);
            }
            tableLayout.addView(rows[i]);
        }
        tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);

    }

    private int getDayInMonth(String month) {
        int monthIndex = Integer.parseInt(month.substring(0, 2)) - 1;
        int year = Integer.parseInt(month.substring(3));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, monthIndex);
        calendar.set(Calendar.YEAR, year);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}