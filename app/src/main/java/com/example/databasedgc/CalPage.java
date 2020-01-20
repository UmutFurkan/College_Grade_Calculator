package com.example.databasedgc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalPage extends AppCompatActivity{

    //This part is so crowded because of the issue that I mentioned in MainActivity in Adapter part.
    private EditText finalNote;
    private EditText midtermNote;
    private EditText projectNote;
    private EditText presentationNote;
    private EditText homeWorkNote;
    private EditText quizNote;

    private EditText finalPerc;
    private EditText midtermPerc;
    private EditText projectPerc;
    private EditText presentationPerc;
    private EditText homeWorkPerc;
    private EditText quizPerc;

    public static String strB = "-";
    public static String strB1;
    public static String strB2;

    private TextView textView1;
    private TextView textView2;

    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_page);

        dbHandler = new MyDBHandler(CalPage.this, null, null, 1);

        strB1 = MainActivity.globTemp1;
        strB2 = MainActivity.globTemp2;

        textView1 = (TextView) findViewById(R.id.showClNameZ);
        textView2 = (TextView) findViewById(R.id.showCreditZ);
        textView1.setText(strB1);
        textView2.setText(strB2);

        finalNote = (EditText) findViewById(R.id.editView1Z);
        midtermNote = (EditText) findViewById(R.id.editView2Z);
        projectNote = (EditText) findViewById(R.id.editView3Z);
        presentationNote = (EditText) findViewById(R.id.editView4Z);
        homeWorkNote = (EditText) findViewById(R.id.editView5Z);
        quizNote = (EditText) findViewById(R.id.editView55Z);

        finalPerc = (EditText) findViewById(R.id.editNote1Z);
        midtermPerc = (EditText) findViewById(R.id.editNote2Z);
        projectPerc = (EditText) findViewById(R.id.editNote3Z);
        presentationPerc = (EditText) findViewById(R.id.editNote4Z);
        homeWorkPerc = (EditText) findViewById(R.id.editNote5Z);
        quizPerc = (EditText) findViewById(R.id.editNote55Z);

        Button btCal = findViewById(R.id.buttonCalZ);
        btCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //After taking all information from calculation page it collects them in two String array in order.
                String[] temp1 = {finalPerc.getText().toString(), midtermPerc.getText().toString(), projectPerc.getText().toString(),
                        presentationPerc.getText().toString(),homeWorkPerc.getText().toString(), quizPerc.getText().toString()};
                String[] temp2 = {finalNote.getText().toString(), midtermNote.getText().toString(), projectNote.getText().toString(),
                        presentationNote.getText().toString(),homeWorkNote.getText().toString(), quizNote.getText().toString()};
                //Of course if the total of percentages is equal to a hundred. E.g (Final: %30, Midterm: %20, Quizzes: %20, Presentation: %15, Project: %15).
                if(isHundred(temp1)){
                    strB =  String.valueOf(calNote(temp1,temp2));
                    dbHandler.dataUpdate(MainActivity.globTemp1,strB);
                    strB = "-";

                    openActivity();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Percentage must be equal to a hundred.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public double calNote(String[] one, String[] two){
        //It takes credits and note from ordered String arrays that mentioned above.
        double[] array1 = new double[one.length];
        double[] array2 = new double[two.length];

        //And turns string version of digits into double.
        for (int i=0; i<one.length; i++){
            try{
                array1[i] = Double.valueOf(one[i]);
            }catch (Exception e){
                array1[i] = 00;
            }
        }
        for (int j=0; j<one.length; j++){
            try{
                array2[j] = Double.valueOf(two[j]);
            }catch (Exception e){
                array2[j] = 00;
            }
        }
        double sum = 00;

        //After filling double arrays, if collects the multiplication of class notes and percentage of that assessment and divides by a hundred.
        for (int z=0; z<one.length; z++){

            sum += ((array1[z]*array2[z])/100);
        }
        return sum;
    }

    //This method for the check that if sum of percentages of assessments is equal to a hundred or not.
    public boolean isHundred (String[] array){
        double sum = 0;
        for(int i=0; i<array.length; i++){
            try{
                sum += Double.valueOf(array[i]);
            }catch(Exception e){
                sum += 00;
            }
        }
        if(sum == 100){
            return true;
        }
        return false;
    }
    public void openActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}