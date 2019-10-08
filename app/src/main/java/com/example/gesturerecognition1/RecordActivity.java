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
    String selectedGesture;

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
        File videoFile = new File(videoPath, "ROONGTA.mp4");

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

            Log.d("CHUTIYA1","caca");

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            File videoPath = getExternalFilesDir(Environment.DIRECTORY_DCIM);
            File videoFile = new File(videoPath, "ROONGTA.mp4");

            Log.d("CHUTIYA1",videoFile.toString());

            //File SDCardRoot = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            //File directory = new File(SDCardRoot, "/my_output_folder/"); //create directory to keep your downloaded file
            //final String fileName = "buy" + ".mp4";

            //final String fileName = selectedGesture + "_PRACTICE_" + "_" + ".mp4";

            final String group_id = "25";
            final String id = "123";
            final String accept = "1";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1024 * 1024;
            try {
                FileInputStream input = new FileInputStream(new File(videoPath, "ROONGTA.mp4"));

                try {

                    URL url = new URL("http://192.168.0.24/GestureRecognition/upload_video.php");

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoInput(true); // Allow Inputs
                    urlConnection.setDoOutput(true); // Allow Outputs
                    urlConnection.setUseCaches(false); // Don't use a Cached Copy
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Connection", "Keep-Alive");
                    urlConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    urlConnection.setRequestProperty("uploaded_file", "ROONGTA.mp4");
                    urlConnection.setRequestProperty("group_id", "21");
                    urlConnection.setRequestProperty("id","123");
                    urlConnection.setRequestProperty("accept","1");

                    DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());



                    //group_id
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"group_id\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(group_id);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);

                    //id
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"id\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(id);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);


                    //accept
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"accept\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(accept);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);

                    //video
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=" + "ROONGTA.mp4" +  lineEnd);
                    dos.writeBytes(lineEnd);



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


                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    int serverResponseCode = urlConnection.getResponseCode();


                    Log.d("serverResponse", String.valueOf(serverResponseCode));
                    //Toast.makeText(getApplicationContext(), serverResponseCode, Toast.LENGTH_LONG).show();
                    String serverResponseMessage = urlConnection.getResponseMessage();



                    Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + " : " + serverResponseCode);


                    if (serverResponseCode == 200) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("uploadFile2","File upload complete");
                                String msg = "File Upload Completed.\n\n See uploaded file here : \n\n" + " http://www.androidexample.com/media/uploads/" + "ROONGTA.mp4";
                                Toast.makeText(getApplicationContext(), "File Upload Complete", Toast.LENGTH_LONG).show();
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
                    Log.d("Error1", String.valueOf(exception));
                    publishProgress(String.valueOf(exception));
                }
            }
            catch (Exception exception)
            {
                Log.d("Error2", String.valueOf(exception));
                publishProgress(String.valueOf(exception));
            }
            return "null";
        }

        @Override
        protected void onProgressUpdate(String... text){
            Toast.makeText(getApplicationContext(), "In Background Task" + text[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String text){
            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
            //Intent intent3 = new Intent(Main3Activity.this, MainActivity.class);
            //startActivity(intent3);
        }
    }
}

