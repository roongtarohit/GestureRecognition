package com.example.gesturerecognition1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RecordActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    VideoView gestureVideo;
    public static final String PRACTISE_VIDEO = "selectedGesture";
    String selectedGesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Button backToMain = (Button) findViewById(R.id.backToMain);
        Button upload = (Button) findViewById(R.id.upload);
        gestureVideo = (VideoView) findViewById(R.id.gestureVideo);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            selectedGesture= null;
        } else {
            selectedGesture = extras.getString(PRACTISE_VIDEO).toLowerCase();
        }

        dispatchTakeVideoIntent();

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        //GESTURE_PRACTICE_(Practice Number)_USERLASTNAME.mp4

    }

    private void dispatchTakeVideoIntent() {


        File mediaFile = new File((Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator +
                "ROONGTA" + ".mp4"));

        //Toast.makeText(getApplicationContext(),path.toString(),Toast.LENGTH_LONG).show();
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(mediaFile));
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE)  {
            if (resultCode == RESULT_OK){
                Uri videoUri = intent.getData();
                gestureVideo.setVideoURI(videoUri);
                gestureVideo.setMediaController(new MediaController(this));
                gestureVideo.requestFocus();
                gestureVideo.start();
            }
            else {
                Toast.makeText(getApplicationContext(),"NEGATIVE", Toast.LENGTH_LONG).show();
                Intent back = new Intent(RecordActivity.this, PractiseActivity.class);
                back.putExtra(PRACTISE_VIDEO, selectedGesture);
                finish();
                startActivity(back);
            }
        }
    }





}
