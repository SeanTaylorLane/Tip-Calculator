package com.example.sean.tipcalculator;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    // TAG used for log debugging during development
    private String TAG = "MainActivity";

    // These booleans used for keeping track of whether the input is valid or not
    private boolean billIsValid = false;
    private boolean percentageIsValid = false;
    private boolean peopleIsValid = false;

    // UI elements I call in both printResults() and onCreate()
    private EditText edit_bill, edit_percentage, edit_people;
    private TextView output_person, output_bill, output_tip, compute_person, compute_bill, compute_tip;
    private FloatingActionButton fab_share;

    // NumberFormat object for ensuring trailing zeros and currency symbol based on location
    private NumberFormat myCurrencyFormatter = NumberFormat.getCurrencyInstance();

    // Output variables that I need to have scope over the class
    private double personOutput, tipOutput, billOutput, percentageInput;
    private String shareText, formattedPercentage;

    private void printResults() {
        if (billIsValid && percentageIsValid && peopleIsValid) {

            // Make the output and share button visible
            output_person.setText("Cost per Person:");
            output_bill.setText("Total Bill Amount:");
            output_tip.setText("Total Tip Amount:");
            fab_share.setVisibility(View.VISIBLE);


            // Grab the user's input as double values
            double billInput = Double.parseDouble(edit_bill.getText().toString());
            percentageInput = Double.parseDouble(edit_percentage.getText().toString());
            double peopleInput = Double.parseDouble(edit_people.getText().toString());

            // Calculate the output and round it to two decimal places
            tipOutput = (double) Math.round((billInput * (percentageInput/100))*100)/100;
            billOutput = (double) Math.round((tipOutput + billInput)*100)/100;
            personOutput = (double) Math.round((billOutput/peopleInput)*100)/100;

            // Output the results!
            compute_person.setText(myCurrencyFormatter.format(personOutput));
            compute_bill.setText(myCurrencyFormatter.format(billOutput));
            compute_tip.setText(myCurrencyFormatter.format(tipOutput));


        } else {
            // If any of the inputs are changed to invalid, make the output and FAB disappear
            // I could have made the TextViews invisible but I think this is fine
            compute_person.setText("");
            compute_bill.setText("");
            compute_tip.setText("");
            output_person.setText("");
            output_bill.setText("");
            output_tip.setText("");
            fab_share.setVisibility(View.INVISIBLE);
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
        output_person = (TextView) findViewById(R.id.output_person);
        output_bill = (TextView) findViewById(R.id.output_bill);
        output_tip = (TextView) findViewById(R.id.output_tip);
        compute_person = (TextView) findViewById(R.id.compute_person);
        compute_bill = (TextView) findViewById(R.id.compute_bill);
        compute_tip = (TextView) findViewById(R.id.compute_tip);

        // Get the share button
        fab_share = (FloatingActionButton) findViewById(R.id.fab_share);


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
            if (!cs.toString().equals("") && (Double.parseDouble(cs.toString())!=0) ) {
                peopleIsValid = true;
            } else {
                peopleIsValid = false;
            }
            printResults();


        });

        // A replacement for setOnClickListener using Rx Binding
        RxView.clicks(fab_share).subscribe(thisisavoidargument -> {
                // This if statement gets rid of any trailing zeros in the percentage before outputting it to the share Intent
                if (Double.toString(percentageInput).charAt(Double.toString(percentageInput).length()-1) == '0') {
                    formattedPercentage = Integer.toString((int)percentageInput);
                } else {
                    formattedPercentage = Double.toString(percentageInput);
                }

                // The text to be shared when you click the FAB
                shareText = "The bill from our meal:\n\n" + "Total Bill:   " + myCurrencyFormatter.format(billOutput) + "\n" + "Tip Percentage:   " + formattedPercentage + "%\n" + "You pay:   " + myCurrencyFormatter.format(personOutput) + "\nCumulative tip:   " + myCurrencyFormatter.format(tipOutput);

                // The implicit Intent for sharing
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");

                // Debated not including a subject extra but email apps use it
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Our Meal Bill\n");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(shareIntent, "Share bill using"));
        });


    }



}
