package com.example.expenseapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.TimeZone;

public class EditExpense extends AppCompatActivity {

    private AlertDialog.Builder alert_dialog;
    private Expense editExpense;
    int PICK_IMAGE_REQUEST = 1;
    private Integer i;
    EditText oldname;
    EditText oldamount;
    private Spinner oldSpinnerCategory;
    TextView olddate;
    ImageView oldimage;
    Button cancel;
    Button save;
    private Uri imgUri;
    String new_image_uri;

    private ArrayList<Expense> editlist;
    Expense nameExpense;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
        setTitle("Edit Expense");
        Button select_button = (Button) findViewById(R.id.button_select_id);
        oldname = (EditText) findViewById(R.id.ediExpenseName);
        oldamount= (EditText) findViewById(R.id.editAmountValue);
        oldSpinnerCategory = (Spinner) findViewById(R.id.EditcategorySpinner);
        olddate = (TextView) findViewById(R.id.EditDate);
        oldimage = (ImageView) findViewById(R.id.ImageView);
        cancel = (Button) findViewById(R.id.cancel_Button);
        save = (Button) findViewById(R.id.save_Button);

        Intent intent = getIntent();
        editlist = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.KEY_VAL);
        Log.d("demo",""+editlist);

        if (editlist == null) {
            return;
        }

        oldimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        alert_dialog = new AlertDialog.Builder(this);
        alert_dialog.setTitle("choose a expense");

        select_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("demo","select button is clicked");

                alert_dialog.setItems(ExpenseNameArray(editlist), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nameExpense = editlist.get(which);
                        try {
                            updateViewsFromExpense(nameExpense);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        i = which;

                    }
                }).show();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newname = oldname.getText().toString();
                String newamount = oldamount.getText().toString();
                String newspinnercategory = oldSpinnerCategory.getSelectedItem().toString();
                String newdate = olddate.getText().toString();


                Expense expense2 = new Expense(newname, newspinnercategory, newamount, newdate,new_image_uri);

                Bundle bundle = new Bundle();

                bundle.putInt(MainActivity.EDIT_VAL, i);
                bundle.putSerializable(MainActivity.KEY_VAL, expense2);
                Log.d("demo",""+expense2);

                Intent intent = new Intent();
                intent.putExtra(MainActivity.BUNDLE_KEY, bundle);
                setResult(RESULT_OK, intent);

                finish();

            }
        });

    }

    private CharSequence[] ExpenseNameArray(ArrayList<Expense> editlist)
    {
        CharSequence[] namearray = new CharSequence[editlist.size()];
        for(int i = 0; i < editlist.size(); i++)
        {
            namearray[i] = editlist.get(i).name;
        }

        return namearray;
    }

    private void setSpinnerCategory(String category)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        oldSpinnerCategory.setAdapter(adapter);
        if(category != null)
        {
            int pos = adapter.getPosition(category);
           oldSpinnerCategory.setSelection(pos);
        }
    }

    private void setImageReceipt(Uri uri)
    {
        Drawable receiptImage;
        try
        {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            receiptImage = Drawable.createFromStream(inputStream, uri.toString());
        }
        catch (FileNotFoundException e)
        {
            receiptImage = getResources().getDrawable(R.drawable.ic_action_name);
        }
        oldimage.setImageDrawable(receiptImage);
    }

    private void updateViewsFromExpense(Expense expense) throws IOException {

        //Log.d("Teja", expense.toString());
        oldname.setText(expense.name);
        oldamount.setText(expense.amount);
        olddate.setText(expense.date);
        setSpinnerCategory(expense.category);
        new_image_uri = expense.image;
        oldimage.setImageBitmap(StringToBitMap(expense.image));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap expense_image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                //ImageView photo2 = (ImageView) findViewById(R.id.imageView_gallery);
                oldimage.setImageBitmap(expense_image);
                new_image_uri = BitMapToString(expense_image);

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

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}

