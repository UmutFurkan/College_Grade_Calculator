package com.example.databasedgc;

//This project is coded by Furkan U. Ç. in 2020/January .

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.databasedgc.CalPage.strB;


public class MainActivity extends AppCompatActivity {

    EditText editText;
    EditText editText2;

    ListView listView;
    ListView listView2;
    ListView listView3;

    MyDBHandler dbHandler;

    public static String globTemp1 = "1";
    public static String globTemp2;
    public static String globTemp3;

    private ArrayList<String> finalarr2;
    private ArrayList<String> finalarr3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.addClassName);
        editText2 = (EditText) findViewById(R.id.addClassCredit);
        listView = (ListView) findViewById(R.id.listView);
        listView2 = (ListView) findViewById(R.id.listView2);
        listView3 = (ListView) findViewById(R.id.listView3);

        //SQLite class object called.
        dbHandler = new MyDBHandler(MainActivity.this, null, null, 1);

        //Listener for picking item on List (To pick class).
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String k ;
                k = (String) listView.getItemAtPosition( position );
                System.out.println(Arrays.asList(k));
                globTemp1 = k;
            }
        });

        //glomTemp1 takes name of Class (Lesson), if any item on list is clicked. Otherwise, stays as String "1".
        Button btCal = (Button) findViewById(R.id.changeAct);
        btCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globTemp1.equals("1")){
                    Toast.makeText(getApplicationContext(),"PLEASE CHOOSE ANY CLASS.", Toast.LENGTH_SHORT).show();
                }else{
                    openActivity();
                }
            }
        });

        //When clicked, prints overall result to screen. (Total credits taken, Successful credits, Overall score out of 4)
        Button finalButton = (Button) findViewById(R.id.callOverAll);
        finalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), finalResult(finalarr2,finalarr3), Toast.LENGTH_SHORT).show();

            }
        });


        //Takes class name from "Class Name" input. (For deleting a class with writing its name.)
        String inputText = editText.getText().toString();
        dbHandler.deleteProduct(inputText);
        //After deleting class from database, it updates user interface again with printDatabase();.
        printDatabase();
    }

    public void  printDatabase(){
        //Creates an ArrayList that contains ArrayLists, it has only three elements : Class Names ArrayList, Class Credits ArrayList and Class Notes ArrayList
        ArrayList<ArrayList<String>> arrB = dbHandler.dbToString();

        //This part for taking that three ArrayLists as separated.
        ArrayList<String> arr1;
        ArrayList<String> arr2;
        ArrayList<String> arr3;

        arr1 = arrB.get(0);
        arr2 = arrB.get(1);
        arr3 = arrB.get(2);

        //Recall of List Views, (Main page has three different List Views that look like one List View.
        ListView listView = (ListView) findViewById(R.id.listView);
        ListView listView2 = (ListView) findViewById(R.id.listView2);
        ListView listView3 = (ListView) findViewById(R.id.listView3);

        //Each List View takes information from ArrayLists that taken from database and implements every element of them as their item via ArrayAdapter.
        //This part is expected as one List View contains items that each item has three entry : one Text View and two Edit Views. But this needs Custom Adapter to implement it.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item,R.id.textView,arr1);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, R.layout.list_item,R.id.textView,arr2);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(this, R.layout.list_item,R.id.textView,arr3);

        listView.setAdapter(arrayAdapter);
        listView2.setAdapter(arrayAdapter2);
        listView3.setAdapter(arrayAdapter3);

        arrayAdapter.notifyDataSetChanged();
        arrayAdapter2.notifyDataSetChanged();
        arrayAdapter3.notifyDataSetChanged();

        //Credits and scores get copied for final calculation, I could call this information via database table but this is cheaper way to do it.
        finalarr2 = arr2;
        finalarr3 = arr3;

        //For testing to see info gotten from database.
        System.out.println(Arrays.asList(arr1));
        System.out.println(Arrays.asList(arr2));
        System.out.println(Arrays.asList(arr3));
    }

    public void addButtonClicked(View view){
        Product products = new Product();
        products.set_className(editText.getText().toString());
        products.set_classCredit(editText2.getText().toString());

        dbHandler.addProduct(products);
        printDatabase();
    }

    public void deleteButtonClicked(View view){
        String inputText = editText.getText().toString();
        dbHandler.deleteProduct(inputText);
        dbHandler.deleteProduct(globTemp1);

        //For preventing Cal Note button from opening activity with name of deleted item.
        globTemp1 = "1";

        printDatabase();
    }

    public void openActivity(){
        Intent intent = new Intent(this, CalPage.class);
        startActivity(intent);
    }

    public String finalResult (ArrayList<String> c , ArrayList<String> d){

        //It takes two ArrayLists that filled in printDatabase();. This method gets called from Overall calculation button.
        String[] a = c.toArray(new String[c.size()]);
        String[] b = d.toArray(new String[d.size()]);

        double[] sum = new double[b.length];
        double x = 0;

        //Sum of total credits.
        double sumCredit = 0;

        //Sum of credits that successfully passed.
        double sumPass = 0;

        double allSum = 0;


        String s;

        int count = 0;

        //This if for the check if there is any class (lesson) left that still doesn't have score yet. If there is int count increases.
        //If it is not "0", this method gives an alert to main page.
        for(int i=0; i<b.length; i++){

            if(b[i].equals("-")){
                count++;
            }
        }
        for(int i=0; i<b.length; i++){
            if(count == 0){
                sumCredit += Double.valueOf(a[i]);

                double y = Double.valueOf(b[i]);

                //This part is for separating class notes to their values. If a score less than 40, then it means "F" which is failed. So, credit of lesson doesn't count as successful.
                if(y>100 ){
                    x = 4;
                    sumPass += Double.valueOf(a[i]);
                }
                else if (100 >= y && y>94){
                    x = 4;
                    sumPass += Double.valueOf(a[i]);
                }
                else if (95>y && y>84){
                    x = 3.7;
                    sumPass += Double.valueOf(a[i]);
                }
                else if (85>y && y>79){
                    x = 3.3;
                    sumPass += Double.valueOf(a[i]);
                }
                else if (80>y && y>74){
                    x = 3;
                    sumPass += Double.valueOf(a[i]);
                }
                else if (75>y && y>64){
                    x = 2.7;
                    sumPass += Double.valueOf(a[i]);
                }
                else if (65>y && y>59){
                    x = 2.3;
                    sumPass += Double.valueOf(a[i]);
                }
                else if (60>y && y>54){
                    x = 2;
                    sumPass += Double.valueOf(a[i]);
                }
                else if (55>y && y>49){
                    x = 1.7;
                    sumPass += Double.valueOf(a[i]);
                }
                else if (50>y && y>44){
                    x = 1.3;
                    sumPass += Double.valueOf(a[i]);
                }
                else if (45>y && y>39){
                    x = 1;
                    sumPass += Double.valueOf(a[i]);
                }
                else if (39>= y){
                    x = 0;
                }

                sum[i] = x;

                allSum += Double.valueOf(a[i])*sum[i];

            }

        }


        //Double multiply double usually has so many decimals e.g 2.00000000000000352362... So, String z takes only 2 decimal to turn it into "2.00".
        String z = String.format("%.02f",(allSum/sumCredit));

        if(count == 0){
            s = ("TOTAL CREDIT : " + sumCredit + ", COMPLETED CREDIT : " + sumPass + ", TERM AVERAGE : " + z);
        }else{
            s =("STILL NON-CALCULATED CLASSES REMAIN!");
        }
        return s;
    }
}
