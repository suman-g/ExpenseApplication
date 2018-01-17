


package com.example.expenseapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class AddExpense extends AppCompatActivity {
    String expense_name;
    String expense_category;
    String expense_amount;
    String expense_date;
    Bitmap expense_image;
    String expense_bitmap_string;
    int PICK_IMAGE_REQUEST = 1;
    private ImageView image_calender;
    int year_x,month_x,day_x;
    static final int DIALOG_ID =0;
    EditText et;
    Expense expense_obj;
    Intent intent_obj;


    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        final Spinner s1 = (Spinner) findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expense_category = s1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//
            }
        });



        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        showDialogOnClick();

        ImageView photo = (ImageView) findViewById(R.id.imageView_gallery);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }

        });


        Button save = (Button) findViewById(R.id.addExpenseButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e1 = (EditText) findViewById(R.id.expenseNameValue);
                expense_name = e1.getText().toString();
                if(expense_name.length() > 50){
                    e1.requestFocus();
                    e1.setError("Your password should be less than 50 characters");
                }else {
                    EditText e2 = (EditText) findViewById(R.id.amountValue);
                    expense_amount = e2.getText().toString();
                    expense_obj = new Expense(expense_name, expense_category,expense_amount,expense_date,expense_bitmap_string);
                    Log.d("DDDDD", expense_obj.toString());
                    intent_obj = new Intent();
                    intent_obj.putExtra(MainActivity.KEY_VAL,(Serializable) expense_obj);
                    setResult( RESULT_OK, intent_obj);
                    finish();
                }


            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                expense_image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView photo2 = (ImageView) findViewById(R.id.imageView_gallery);
                photo2.setImageBitmap(expense_image);
                expense_bitmap_string = BitMapToString(expense_image);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public void showDialogOnClick(){

        image_calender =(ImageView)findViewById(R.id.imageView_calender);
        image_calender.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    protected Dialog onCreateDialog (int id){
        if (id==DIALOG_ID) {
            return new DatePickerDialog(this, dPickerListner, year_x, month_x, day_x);
        }else{
            return null;
        }
    }

    private DatePickerDialog.OnDateSetListener dPickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            year_x = year;
            month_x = month+1;
            day_x = dayOfMonth;

            et = (EditText) findViewById(R.id.date_value);
            et.setText(""+month_x+"/"+day_x+"/"+year_x);
            expense_date = ""+month_x+"/"+day_x+"/"+year_x;

        }
    };


}

