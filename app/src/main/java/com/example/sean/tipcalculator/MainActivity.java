package com.example.sean.tipcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

public class MainActivity extends AppCompatActivity {


    private String TAG = "MainActivity";
    private boolean billIsValid = false;
    private boolean percentageIsValid = false;
    private boolean peopleIsValid = false;
    private EditText edit_bill, edit_percentage, edit_people;
    private TextView compute_person, compute_bill, compute_tip;

    private void printResults() {
        if (billIsValid && percentageIsValid && peopleIsValid) {
            double billInput = Double.parseDouble(edit_bill.getText().toString());
            double percentageInput = Double.parseDouble(edit_percentage.getText().toString());
            double peopleInput = Double.parseDouble(edit_people.getText().toString());
            double personOutput, tipOutput, billOutput;

            tipOutput = billInput * (percentageInput/100);
            billOutput = tipOutput + billInput;
            personOutput = billOutput/peopleInput;

            Log.e(TAG, "yoooooooo");
            compute_person.setText("$" + personOutput);
            compute_bill.setText("$" + billOutput);
            compute_tip.setText("$" + tipOutput);

        } else {
            compute_person.setText("");
            compute_bill.setText("");
            compute_tip.setText("");
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // END GENERIC ACTIVITY CODE

        // Get all the EditText objects
        edit_bill = (EditText) findViewById(R.id.edit_bill);
        edit_percentage = (EditText) findViewById(R.id.edit_percentage);
        edit_people = (EditText) findViewById(R.id.edit_people);

        // Get all the computed TextView objects
        compute_person = (TextView) findViewById(R.id.compute_person);
        compute_bill = (TextView) findViewById(R.id.compute_bill);
        compute_tip = (TextView) findViewById(R.id.compute_tip);


        // Add create observables that emit data when the EditText objects are changed
        RxTextView.textChanges(edit_bill).subscribe(cs -> {

            // Is the input valid?
            if (!cs.toString().equals("")) {
                billIsValid = true;
            } else {
                billIsValid = false;
            }
            printResults();

        });

        RxTextView.textChanges(edit_percentage).subscribe(cs -> {

            // Is the input valid?
            if (!cs.toString().equals("")) {
                percentageIsValid = true;
            } else {
                percentageIsValid = false;
            }
            printResults();

        });

        RxTextView.textChanges(edit_people).subscribe(cs -> {

            // Is the input valid?
            if (!cs.toString().equals("")) {
                peopleIsValid = true;
            } else {
                peopleIsValid = false;
            }
            printResults();

        });
    }



}
