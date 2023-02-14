package com.bitsnest.lifechanger_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Calculator extends AppCompatActivity {

    private EditText profit,partnerInv,totalInv;
    private TextView answer;
    Float total_balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        getSupportActionBar().hide();

        profit=findViewById(R.id.edit_profit);
        partnerInv=findViewById(R.id.edit_pInvest);
        totalInv=findViewById(R.id.edit_tInvest);
        answer=findViewById(R.id.txt_result);
        Button next=findViewById(R.id.btn_calculator);
        Button btn_clear= findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profit.setText("");
                partnerInv.setText("");
                totalInv.setText("");
                answer.setText("00");
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!IsEmptyfield())
                    calculate();
            }
        });
    }

    private void calculate() {
        Float Profit=Float.parseFloat(profit.getText().toString());
        Float TotalInv=Float.parseFloat(totalInv.getText().toString());
        Float PartnerInv=Float.parseFloat(partnerInv.getText().toString());
        Float perc_of_PI=(PartnerInv*100)/(TotalInv);
        total_balance=(Profit/100)*(perc_of_PI);
        answer.setText(total_balance.toString());
    }

    public boolean IsEmptyfield(){
        boolean empty=true;

        if(profit.getText().toString().isEmpty())profit.setError("Enter Profit");
        else if(partnerInv.getText().toString().isEmpty())partnerInv.setError("Enter Partner Investment");
        else if(totalInv.getText().toString().isEmpty())totalInv.setError("Enter Total Investment");
        else empty=false;
        return empty;
    }
}