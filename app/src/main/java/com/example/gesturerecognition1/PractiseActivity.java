package com.example.gesturerecognition1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.net.URI;

public class PractiseActivity extends AppCompatActivity {

    public static final String PRACTISE_VIDEO = "selectedGesture";
    public static final String VIDEO_NAME = "videoName";
    public static final String ASU_ID = "asuID";
    String selectedGesture;
    String asuID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise);

        Button backToMain = (Button) findViewById(R.id.backToMain);
        Button record = (Button)  findViewById(R.id.record);
        final EditText videoTextName = (EditText) findViewById(R.id.videoEditText);
        final EditText asuID = (EditText) findViewById(R.id.asuID);

        String practiseVideoName, practiseVideoFile;

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            practiseVideoName= null;
        } else {
            practiseVideoName= extras.getString(PRACTISE_VIDEO).toLowerCase();
        }
        selectedGesture = practiseVideoName;

        Toast.makeText(getApplicationContext(),practiseVideoName, Toast.LENGTH_LONG).show();

        long practiseVideoId = getResources().getIdentifier(practiseVideoName,"raw",getPackageName());

        practiseVideoFile = "android.resource://" + getPackageName() + "/" + practiseVideoId;


        VideoView practiseVideo = (VideoView) findViewById(R.id.practiseVideo);
        practiseVideo.setVideoURI(Uri.parse(practiseVideoFile));
        practiseVideo.setMediaController(new MediaController(this));
        practiseVideo.requestFocus();
        practiseVideo.start();

        practiseVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasCamera()){
                    Toast.makeText(getApplicationContext(),"NO CAMERA", Toast.LENGTH_LONG).show();
                }
                else {
                    if(videoTextName.getText().length() == 0 || asuID.getText().length() == 0){
                        Toast.makeText(getApplicationContext(),"ENTER THE VIDEO NAME and ASU ID", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                        intent.putExtra(PRACTISE_VIDEO, selectedGesture);
                        intent.putExtra(VIDEO_NAME,videoTextName.getText().toString());
                        intent.putExtra(ASU_ID, asuID.getText().toString());
                        finish();
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private boolean hasCamera(){
        return (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY));
    }
}
