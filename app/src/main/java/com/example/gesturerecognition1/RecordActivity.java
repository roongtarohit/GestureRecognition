package com.example.gesturerecognition1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class RecordActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    VideoView gestureVideo;
    public static final String PRACTISE_VIDEO = "selectedGesture";
    public static final String VIDEO_NAME = "videoName";
    public static final String ASU_ID = "asuID";
    public static final String groupID = "25";
    public static final String accept = "1";
    String selectedGesture, videoName, asuID;

    //public static final String UPLOAD_URL = "http://10.218.107.121/cse535/upload_video.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Button backToMain = (Button) findViewById(R.id.backToMain);
        Button upload = (Button) findViewById(R.id.upload);
        gestureVideo = (VideoView) findViewById(R.id.gestureVideo);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            selectedGesture = null;
        } else {
            selectedGesture = extras.getString(PRACTISE_VIDEO).toLowerCase();
            videoName = selectedGesture.toUpperCase() + "_PRACTISE_" + extras.getString(VIDEO_NAME).toLowerCase() + ".mp4";
            asuID = extras.getString(ASU_ID);
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

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadTask tc = new UploadTask();
                tc.execute();
            }
        });

    }

    private void dispatchTakeVideoIntent() {

        // path To video directory
        //Path is Android/data/com.example.gesturerecognition1/files/DCIM/
        File videoPath = getExternalFilesDir(Environment.DIRECTORY_DCIM);

        // create an mp4 at this path
        File videoFile = new File(videoPath, videoName);

        Toast.makeText(getApplicationContext(),"THIS ******* " + videoFile.toString(), Toast.LENGTH_LONG).show();

        //get Uri for the videoFile [authority is defined in the manifest]
        // geApplicationContext().getPackageName() give "com.example.gesturerecognition1" therefore we append .provider
        Uri videoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", videoFile);

        //Toast.makeText(getApplicationContext(), videoUri.toString(), Toast.LENGTH_LONG).show();

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        // Where do I want to place the video
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);

        //pass permission to the camera
        takeVideoIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Uri videoUri = intent.getData();
                Toast.makeText(getApplicationContext(), videoUri.toString(), Toast.LENGTH_LONG).show();
                gestureVideo.setVideoURI(videoUri);
                gestureVideo.setMediaController(new MediaController(this));
                gestureVideo.requestFocus();
                gestureVideo.start();
            } else {
                Toast.makeText(getApplicationContext(), "NEGATIVE", Toast.LENGTH_LONG).show();
                Intent back = new Intent(RecordActivity.this, PractiseActivity.class);
                back.putExtra(PRACTISE_VIDEO, selectedGesture);
                finish();
                startActivity(back);
            }
        }
    }

    public class UploadTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {

            String delimiter = "\r\n";
            String hyphens = "--";
            String split = "*****";

            File videoPath = getExternalFilesDir(Environment.DIRECTORY_DCIM);

            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1024 * 1024;
            try {
                FileInputStream input = new FileInputStream(new File(videoPath, videoName));

                try {

                    URL url = new URL("http://10.218.107.121/cse535/upload_video.php");

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoInput(true); // Allow Inputs
                    urlConnection.setDoOutput(true); // Allow Outputs
                    urlConnection.setUseCaches(false); // Don't use a Cached Copy
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Connection", "Keep-Alive");
                    urlConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    urlConnection.setRequestProperty("Content-Type", "multipart/form-data;split=" + split);

                    DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());


                    //ADDING GROUP ID IN HEADER
                    dos.writeBytes(hyphens + split + delimiter);
                    dos.writeBytes("Content-Disposition: form-data; name=\"groupID\"" + delimiter);
                    dos.writeBytes(delimiter);
                    dos.writeBytes(groupID);
                    dos.writeBytes(delimiter);
                    dos.writeBytes(hyphens + split + delimiter);

                    //ADDING ASU ID ID IN HEADER
                    dos.writeBytes(hyphens + split + delimiter);
                    dos.writeBytes("Content-Disposition: form-data; name=\"asuID\"" + delimiter);
                    dos.writeBytes(delimiter);
                    dos.writeBytes(asuID);
                    dos.writeBytes(delimiter);
                    dos.writeBytes(hyphens + split + delimiter);


                    //ADDING ACCEPT IN HEADER
                    dos.writeBytes(hyphens + split + delimiter);
                    dos.writeBytes("Content-Disposition: form-data; name=\"accept\"" + delimiter);
                    dos.writeBytes(delimiter);
                    dos.writeBytes(accept);
                    dos.writeBytes(delimiter);
                    dos.writeBytes(hyphens + split + delimiter);

                    //ADDING VIDEO IN HEADER
                    dos.writeBytes(hyphens + split + delimiter);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=" + videoName +  delimiter);
                    dos.writeBytes(delimiter);

                    bytesAvailable = input.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    bytesRead = input.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = input.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = input.read(buffer, 0, bufferSize);
                    }


                    dos.writeBytes(delimiter);
                    dos.writeBytes(hyphens + split + hyphens + delimiter);

                    int serverResponseCode = urlConnection.getResponseCode();


                    Log.d("serverResponse", String.valueOf(serverResponseCode));
                    //Toast.makeText(getApplicationContext(), serverResponseCode, Toast.LENGTH_LONG).show();
                    String serverResponseMessage = urlConnection.getResponseMessage();

                    Log.i("CHECKPOINT : ", "HTTP Response is : " + serverResponseMessage + " : " + serverResponseCode);


                    if (serverResponseCode == 200) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("CHECKPOINT : ","File upload complete");
                            }
                        });
                    }
                    else
                        Toast.makeText(getApplicationContext(), "File Upload Complete", Toast.LENGTH_LONG).show();

                    input.close();
                    dos.flush();
                    dos.close();

                }
                catch (Exception exception)
                {
                    Log.d("Exception : ", String.valueOf(exception));
                    publishProgress(String.valueOf(exception));
                }
            }
            catch (Exception exception)
            {
                Log.d("Exception : ", String.valueOf(exception));
                publishProgress(String.valueOf(exception));
            }
            return "null";
        }

        @Override
        protected void onProgressUpdate(String... text){
            //Toast.makeText(getApplicationContext(), "In Background Task" + text[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String text){
            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
        }
    }
}

