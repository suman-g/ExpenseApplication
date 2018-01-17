package com.example.expenseapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class DeleteExpense extends AppCompatActivity {
    private AlertDialog.Builder alert_dialog;
    private Expense editExpense;
    private Integer i;
    EditText oldname;
    EditText oldamount;
    private Spinner oldSpinnerCategory;
    TextView olddate;
    ImageView oldimage;
    Button cancel;
    Button delete;



    private ArrayList<Expense> editlist;
    Expense nameExpense;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_expense);

        Button select_button = (Button) findViewById(R.id.button_select_id);
        oldname = (EditText) findViewById(R.id.ediExpenseName);
        oldamount= (EditText) findViewById(R.id.editAmountValue);
        oldSpinnerCategory = (Spinner) findViewById(R.id.EditcategorySpinner);
        olddate = (TextView) findViewById(R.id.EditDate);
        oldimage = (ImageView) findViewById(R.id.ImageView);
        cancel = (Button) findViewById(R.id.cancel_Button);
        delete = (Button) findViewById(R.id.save_Button);

        Intent intent = getIntent();
        editlist = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.KEY_VAL);
        Log.d("demo",""+editlist);

        if (editlist == null) {
            return;
        }

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

                    //when a paticular expense is clicked fields must populate the values
                    public void onClick(DialogInterface dialog, int which) {

                        nameExpense = editlist.get(which);
                        if(nameExpense != null)
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                Log.d("asdf", "inttt " + i);

                bundle.putInt(MainActivity.EDIT_VAL, i);

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

    private void updateViewsFromExpense(Expense expense) throws IOException {
        oldname.setText(expense.name);
        oldamount.setText(expense.amount);
        olddate.setText(expense.date);
        setSpinnerCategory(expense.category);
        //Bitmap expense_image = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(expense.image));
        oldimage.setImageBitmap(StringToBitMap(expense.image));
        //Log.d("demo",""+expense.image);

    }
}




