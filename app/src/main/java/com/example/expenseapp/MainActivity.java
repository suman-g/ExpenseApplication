

package com.example.expenseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int REQ_CODE = 100;
    public static final int EREQ_CODE = 200;
    public static final int DREQ_CODE = 300;

    public static final String KEY_VAL = "key";
    public static final String EDIT_VAL = "edit";
    public static final String BUNDLE_KEY = "bundle";

    ArrayList<Expense> list = new ArrayList<Expense>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_AddExpense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_add = new Intent(MainActivity.this, AddExpense.class );
                startActivityForResult(intent_add, REQ_CODE);
            }
        });

        findViewById(R.id.button_DeleteExpense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_delete = new Intent(MainActivity.this, DeleteExpense.class );
                intent_delete.putExtra(KEY_VAL,list);
                startActivityForResult(intent_delete,DREQ_CODE);
            }
        });
        findViewById(R.id.button_EditExpense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_edit = new Intent(MainActivity.this, EditExpense.class );
                intent_edit.putExtra(KEY_VAL,list);
                startActivityForResult(intent_edit,EREQ_CODE);
            }
        });
        findViewById(R.id.button_showExpense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_show = new Intent(MainActivity.this, ShowExpense.class );
                intent_show.putExtra(KEY_VAL,list);
                startActivity(intent_show);
            }
        });

        findViewById(R.id.button_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE)
        {
            if(resultCode==RESULT_OK)
            {
                Expense e1 = (Expense) data.getExtras().getSerializable(KEY_VAL);
                list.add(e1);


            }
        }
        else if(requestCode == EREQ_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                Log.d("demo","entered");
                Bundle bundle = (Bundle)data.getExtras().getBundle(BUNDLE_KEY);

                Integer i = bundle.getInt(EDIT_VAL);
                Expense expense = (Expense) bundle.getSerializable(KEY_VAL);
                Log.d("demo",""+expense);


                list.set(i, expense);
            }
        }
        else if(requestCode == DREQ_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                Bundle bundle = (Bundle)data.getExtras().getBundle(BUNDLE_KEY);

                Integer index = bundle.getInt(EDIT_VAL);

                Log.d("asdf", "intttttt main: " + index + " " + list.size());

                //Cast to int so we call remove(int) instead of remove(Object)
                list.remove((int)index);

                Log.d("asdf", "intttttt mainsza: " + list.size());
            }
        }
    }
}
