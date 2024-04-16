package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

/*This class constructs the main screen using the activity_main.xml.
It uses shared preferences to store the user selection.
It calls an explicit intent to display a new activity listing the cocktails.
*/
public class MainActivity extends AppCompatActivity {
    //Declare the variables with object types
    RadioGroup radioGroup;
    RadioButton radioButton, vodkaButton, ginButton, whiskeyButton, rumButton, tequilaButton;
    TextView textView;

    //Shared Preferences variables
    SharedPreferences mySharedPreference;
    SharedPreferences.Editor myEditor;

    //Initialise tag for logging
    private static final String TAG = "MainActivity";

    //This method builds the screen of the main activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log message for onCreate
        Log.i(TAG, "onCreate");

        //Restore any saved instance state
        super.onCreate(savedInstanceState);

        //Set the content view
        setContentView(R.layout.activity_main);

        //Initialize UI elements
        radioGroup = findViewById(R.id.radioGroup);
        textView = findViewById(R.id.text_view_selected);

        //Instantiate the share preferences object reference
        mySharedPreference = getSharedPreferences("pref", 0);

        //To help read values of shared preferences
        int alcoholSP = mySharedPreference.getInt("alcoholSP", 5);

        //Instantiate the shared preference editor object by calling edit() method
        myEditor = mySharedPreference.edit();

        //Create variables for each radio button in the activity_main.xml file
        vodkaButton = findViewById(R.id.radio_one);
        ginButton = findViewById(R.id.radio_two);
        whiskeyButton = findViewById(R.id.radio_three);
        rumButton = findViewById(R.id.radio_four);
        tequilaButton = findViewById(R.id.radio_five);

        //Apply the shared preferences
        //Check the value of alcoholSP and select the corresponding radio button
        if(alcoholSP == 0){
            vodkaButton.setChecked(true);
            radioButton = vodkaButton;
        }else if(alcoholSP == 1){
            ginButton.setChecked(true);
            radioButton = ginButton;
        }else if(alcoholSP == 2){
            whiskeyButton.setChecked(true);
            radioButton = whiskeyButton;
        }else if(alcoholSP == 3){
            rumButton.setChecked(true);
            radioButton = rumButton;
        }else if(alcoholSP == 4) {
            tequilaButton.setChecked(true);
            radioButton = tequilaButton;
        }

        //Storing the shared preference values
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == R.id.radio_one){
                    myEditor.putInt("alcoholSP", 0);
                }else if(checkedId == R.id.radio_two){
                    myEditor.putInt("alcoholSP", 1);
                }else if(checkedId == R.id.radio_three){
                    myEditor.putInt("alcoholSP", 2);
                }else if(checkedId == R.id.radio_four){
                    myEditor.putInt("alcoholSP", 3);
                }else if(checkedId == R.id.radio_five){
                    myEditor.putInt("alcoholSP", 4);
                }
                myEditor.commit();
            }
        });
    }

    //Generates a toast message when a radio button is clicked
    public void checkButton(View v) {
        //Log message for checkButton
        Log.i(TAG, "checkButton");
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, "You selected " + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }

    //Creates an options options_menu_activity_main using the options_menu_activity_main.xml file
    public boolean onCreateOptionsMenu(Menu menu){
        //Log message for onCreateOptionsMenu
        Log.i(TAG, "onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_activity_main, menu);
        return true;
    }

    //Deals with the options_menu_activity_main items selected
    public boolean onOptionsItemSelected(MenuItem item) {
        //Log message for onOptionsItemSelected
        Log.i(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.reset:
                radioGroup.clearCheck();
                return true;
            case R.id.exit:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Calls the explicit intent to go to the second activity and display list of cocktails
    public void showCocktails (View v){
        //Log message for showCocktails
        Log.i(TAG, "showCocktails");
        Context context = MainActivity.this;
        Class<CocktailListActivity> destinationActivity = CocktailListActivity.class;

        //Ensures user selects a spirit
        if(radioGroup.getCheckedRadioButtonId()==-1){
            Toast.makeText(this, "Please select a spirit", Toast.LENGTH_SHORT).show();
        }else{
            Intent i = new Intent(context, destinationActivity);
            i.putExtra("spirit", radioButton.getText());
            startActivity(i);
        }
    }
}
