package com.example.gesturerecognition1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String PRACTISE_VIDEO = "selectedGesture";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner gesture_spinner = (Spinner) findViewById(R.id.gestures_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gesture_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner

        gesture_spinner.setAdapter(adapter);

        gesture_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int gestureIndex = adapterView.getSelectedItemPosition();
                String selectedGesture = adapterView.getItemAtPosition(gestureIndex).toString();

                if (!selectedGesture.equals("CHOOSE A GESTURE")) {
                    Toast.makeText(getApplicationContext(), "Selected: " + selectedGesture, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), PractiseActivity.class);
                    intent.putExtra(PRACTISE_VIDEO, selectedGesture);
                    finish();
                    startActivity(intent);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
