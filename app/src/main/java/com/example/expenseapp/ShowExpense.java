package com.example.expenseapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class ShowExpense extends AppCompatActivity {

    TextView showName, showCategory, showAmount, showDate;
    ImageView showImage;
    Button finish;
    ImageButton showFirst, showLast, showNext, showPrevious;
    int index = 0;

    private ArrayList<Expense> showlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);
        showName = (TextView) findViewById(R.id.showNamevalue);
        showCategory = (TextView) findViewById(R.id.showCategoryValue);
        showAmount = (TextView) findViewById(R.id.showamountvalue);
        showDate = (TextView) findViewById(R.id.showDateValue);
        showImage = (ImageView) findViewById(R.id.showImage);
        finish = (Button) findViewById(R.id.finish_Button);
        showFirst = (ImageButton) findViewById(R.id.showFirst);
        showLast = (ImageButton) findViewById(R.id.showLast);
        showNext = (ImageButton) findViewById(R.id.showNext);
        showPrevious = (ImageButton) findViewById(R.id.showPevious);

        Intent intent = getIntent();
        showlist = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.KEY_VAL);

        Expense expense = showlist.get(0);


        try {
            updateViewsFromExpense(expense);
        } catch (IOException e) {
            e.printStackTrace();
        }

        showLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lengthOfList = showlist.size() - 1;
                index = lengthOfList;
                try {
                    updateViewsFromExpense(showlist.get(index));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        showFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 0;
                try {
                    updateViewsFromExpense(showlist.get(index));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        showNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lengthOfList = showlist.size() - 1;
                index += 1;
                if(index > lengthOfList){
                    index = lengthOfList;
                }
                try {
                    updateViewsFromExpense(showlist.get(index));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        showPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index -= 1;
                if(index < 0){
                    index = 0;
                }
                try {
                    updateViewsFromExpense(showlist.get(index));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void updateViewsFromExpense(Expense expense) throws IOException {
        showName.setText(expense.name);
        showAmount.setText(expense.amount);
        showDate.setText(expense.date);
        showCategory.setText(expense.category);
        showImage.setImageBitmap(StringToBitMap(expense.image));

    }
}
