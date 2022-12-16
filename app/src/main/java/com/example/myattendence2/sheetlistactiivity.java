package com.example.myattendence2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class sheetlistactiivity extends AppCompatActivity {
     ListView sheetlist;
     ArrayAdapter arrayAdapter;
     ArrayList<String> arrayList = new ArrayList();
     long cid;
     Bundle bundle;
     androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheetlistactiivity);

        // set toolbar
        toolbar();

        bundle = new Bundle();
        bundle = getIntent().getExtras();
        cid = Long.parseLong(bundle.getString("cid", String.valueOf(-1)));
        Log.i("1234567890","onCreate: "+cid);
        loadlistitem();
        sheetlist = findViewById(R.id.sheetlist);
        arrayAdapter = new ArrayAdapter(this,R.layout.sheet_item,R.id.date_list_item,arrayList);
        sheetlist.setAdapter(arrayAdapter);

        sheetlist.setOnItemClickListener((parent, view, position, id) -> opensheetactivity(position));

    }

    private void opensheetactivity(int position) {
        long [] idArray = getIntent().getLongArrayExtra("idArray");
        String [] nameArray = getIntent().getStringArrayExtra("nameArray");
        int [] rollArray = getIntent().getIntArrayExtra("rollArray");

        Intent intent = new Intent(getApplicationContext(),SheetActivity.class);
        intent.putExtra("idArray", idArray);
        intent.putExtra("nameArray", nameArray);
        intent.putExtra("rollArray", rollArray);
        intent.putExtra("month",arrayList.get(position));
        startActivity(intent);
    }

    private void loadlistitem() {
        Cursor cursor = new DBhelper(this).getDistinctMonths(cid);
        Log.i("1234567890","loadlistitem: "+cursor.getCount());
        while (cursor.moveToNext()){
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DBhelper.DATE_KEY));
            arrayList.add(date.substring(3));
        }
    }

    public void toolbar(){
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.suubtitle_toolbar);
        ImageButton save = toolbar.findViewById(R.id.save);
        TextView savetext = toolbar.findViewById(R.id.save2);
        ImageButton back = toolbar.findViewById(R.id.back);
        title.setText("SEE STATUS");
        subtitle.setVisibility(View.GONE);
        save.setVisibility(View.INVISIBLE);
        back.setOnClickListener(v -> onBackPressed());
        save.setVisibility(View.INVISIBLE);
        savetext.setVisibility(View.INVISIBLE);
    }

}