package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/*
    Assignment 1
    HW1
    Xu Yang & Kitsada Madsourivong
    
 */


public class MainActivity extends AppCompatActivity {

//    final String TAG = "hw1";
    int checkedId = R.id.radioButton10p; // Get the default id.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization
        EditText editTextEnterBillTotal = findViewById(R.id.editTextEnterBillTotal);
        TextView textViewTip = findViewById(R.id.textViewTip);
        TextView textViewTotal = findViewById(R.id.textViewTotal);
        RadioGroup radioGroupTipRate = findViewById(R.id.radioGroupTipRate);
        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView textViewProgress = findViewById(R.id.seekBarProgress);
        RadioGroup radioGroupSplit = findViewById(R.id.radioGroupSplit);
        TextView textViewTotalPerPerson = findViewById(R.id.textViewTotalPerPerson);
        Button buttonClear = findViewById(R.id.buttonClear);


        // Tip rate radio group event listener
        radioGroupTipRate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId1) {
                checkedId = checkedId1; // Get the id after clicking

                // Events for tip rate radio group
                tipRateRadioGroupEvents(textViewTip,textViewTotal,textViewProgress,
                        textViewTotalPerPerson, editTextEnterBillTotal, radioGroupSplit, seekBar);

                // Events for split by radio group
                int checkedId2 = radioGroupSplit.getCheckedRadioButtonId();
                splitRadioGroupEvents(checkedId2,textViewTotalPerPerson,textViewTotal);

            }
        });

        // Split by radio group event listener
        radioGroupSplit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Events for split by radio group
                splitRadioGroupEvents(checkedId,textViewTotalPerPerson,textViewTotal);
            }
        });



        // Event listener when user input (edit text) is changed
        editTextEnterBillTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Events for tip rate radio group
                tipRateRadioGroupEvents(textViewTip,textViewTotal,textViewProgress,
                        textViewTotalPerPerson, editTextEnterBillTotal, radioGroupSplit, seekBar);

                // Events for split by radio group
                int checkedId2 = radioGroupSplit.getCheckedRadioButtonId();
                splitRadioGroupEvents(checkedId2,textViewTotalPerPerson,textViewTotal);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // Event listener for clear button
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set all the values back to default.
                editTextEnterBillTotal.getText().clear();
                radioGroupTipRate.check(R.id.radioButton10p);
                seekBar.setProgress(40);
                textViewProgress.setText("40%");
                textViewTip.setText("$0.0");
                textViewTotal.setText("$0.0");
                radioGroupSplit.check(R.id.radioButtonOne);
                textViewTotalPerPerson.setText("$0.0");
            }
        });



    }

    /*
        Function takes several parameters and handle all the event when tip rate is changed
     */
    public void tipRateRadioGroupEvents(TextView textViewTip,
                                         TextView textViewTotal,
                                         TextView textViewProgress,
                                         TextView textViewTotalPerPerson,
                                         EditText editTextEnterBillTotal,
                                         RadioGroup radioGroupSplit,
                                         SeekBar seekBar){
        double userInput = 0.0;
        // Make sure user input is not empty before parsing it to double
        if(!editTextEnterBillTotal.getText().toString().matches("")){
            userInput = Double.parseDouble(String.valueOf(editTextEnterBillTotal.getText()));
        }

        // Calculate and display the results for tip and total based on which radio button is selected
        if(checkedId == R.id.radioButton10p){
            setValue(textViewTip,textViewTotal,userInput,0.10);
//            Log.d(TAG, "tipRateRadioGroupHandler: 10% with " + checkedId);
        }else if(checkedId == R.id.radioButton15p){
//            Log.d(TAG, "tipRateRadioGroupHandler: 15% with " + checkedId);
            setValue(textViewTip,textViewTotal,userInput,0.15);
        } else if(checkedId == R.id.radioButton18p){
//            Log.d(TAG, "tipRateRadioGroupHandler: 18% with " + checkedId);
            setValue(textViewTip,textViewTotal,userInput,0.18);
        } else if(checkedId == R.id.radioButtonCustom){
//            Log.d(TAG, "tipRateRadioGroupHandler: Custom with " + checkedId);
            // Do the calculation immediately once "Custom" is selected
            int progress = seekBar.getProgress();
            setValue(textViewTip,textViewTotal, userInput,(double) progress / 100);
            textViewProgress.setText(seekBar.getProgress()+"%");
            // Handle the changing of seek bar
            seekBarEvents(seekBar, radioGroupSplit, textViewTip, textViewTotal,
                    textViewProgress, textViewTotalPerPerson, userInput);
        }
    }


    /*
        Function takes several parameters and handle all the events when the seek bar is scrolled.
     */
    public void seekBarEvents(SeekBar seekBar, RadioGroup radioGroupSplit, TextView textViewTip,
                               TextView textViewTotal, TextView textViewProgress,
                               TextView textViewTotalPerPerson, double userInput){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Check if "Custom" radio button is selected
                if(checkedId == R.id.radioButtonCustom){
                    // Update and display all the values as the seek bar scrolled.
                    int checkedId2 = radioGroupSplit.getCheckedRadioButtonId();
//                    Log.d(TAG, "onProgressChanged: bar scrolled with " + checkedId);
                    setValue(textViewTip,textViewTotal, userInput,(double) progress / 100);
                    textViewProgress.setText(seekBar.getProgress()+"%");
                    splitRadioGroupEvents(checkedId2,textViewTotalPerPerson,textViewTotal);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    // Function handles the event when "split" radio group selection is changed
    public void splitRadioGroupEvents(int checkedId,TextView textViewTotalPerPerson, TextView textViewTotal){
        int numberOfPerson = 1;
        if(checkedId == R.id.radioButtonTwo){
            numberOfPerson = 2;
        } else if(checkedId == R.id.radioButtonThree){
            numberOfPerson = 3;
        } else if (checkedId == R.id.radioButtonFour){
            numberOfPerson = 4;
        } else{
            numberOfPerson = 1;
        }
        // Update the total/person based on the radio button selected.
        double total = Double.valueOf(textViewTotal.getText().toString().replace("$",""));
        textViewTotalPerPerson.setText("$" + String.format("%.2f",(total/numberOfPerson)));
    }



    // Function for displaying tip and total values.
    public void setValue(TextView textViewTip, TextView textViewTotal, double userInput, double tipRate){
        textViewTip.setText("$"+String.format("%.2f",tipRate*userInput));
        textViewTotal.setText("$"+String.format("%.2f",(1+tipRate)*userInput));
    }


}