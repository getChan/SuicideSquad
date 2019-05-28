package com.example.skarn.please;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.FloatProperty;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.JsonArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class StudyMainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private static final String TAG =  MainActivity.class.getSimpleName();
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final int READ_REQUEST_CODE = 200;
    private Uri uri;
    private String pathToStoredVideo;
//    private VideoView displayRecordedVideo;
    private static final String SERVER_PATH = "http://117.16.244.58:3000/";
    String label = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        label += intent.getStringExtra("label");
//        Toast.makeText(getBaseContext(), label, Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main_study);
        Button captureVideoButton = findViewById(R.id.capture_video);
        captureVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(videoCaptureIntent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                }
            }
        });
        // problabel
        ListView problabelview = findViewById(R.id.problabel);
        String[] problabel = {"귀엽다", "안녕하세요","사랑", "감사", "기다리다"};
        ArrayAdapter<String> problabeladapter = new ArrayAdapter<String >(
                getBaseContext(), android.R.layout.simple_list_item_1, problabel
        );
        problabelview.setAdapter(problabeladapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoView videoView = findViewById(R.id.video);
        Uri videouri;
        if(label.equals("0")){
            //귀엽다
            videouri = Uri.parse("http://sldict.korean.go.kr/multimedia/multimedia_files/convert/20160102/239131/MOV000248760_700X466.mp4");
            videoView.setVideoURI(videouri);
        }else if(label.equals("1")){
            //안녕
            videouri = Uri.parse("http://sldict.korean.go.kr/multimedia/multimedia_files/convert/20151223/233171/MOV000244910_700X466.mp4");
            videoView.setVideoURI(videouri);
        }else if(label.equals("2")){
            //사랑
            videouri = Uri.parse("http://sldict.korean.go.kr/multimedia/multimedia_files/convert/20160106/241738/MOV000253928_700X466.mp4");
            videoView.setVideoURI(videouri);
        }else if(label.equals("3")){
            //감사
            videouri = Uri.parse("http://sldict.korean.go.kr/multimedia/multimedia_files/convert/20160102/238965/MOV000248428_700X466.mp4");
            videoView.setVideoURI(videouri);
        }else {
            //기다리다
            videouri = Uri.parse("http://sldict.korean.go.kr/multimedia/multimedia_files/convert/20160103/239573/MOV000249602_700X466.mp4");
            videoView.setVideoURI(videouri);
        }
        videoView.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE){
            uri = data.getData();
            if(EasyPermissions.hasPermissions(StudyMainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
//                displayRecordedVideo.setVideoURI(uri);
//                displayRecordedVideo.start();
                Log.d(TAG, "URI : "+uri);
                pathToStoredVideo = getRealPathFromURIPath(uri, StudyMainActivity.this);
                Log.d(TAG, "Recorded Video Path " + pathToStoredVideo);
                //Store the video to your server
                uploadVideoToServer(pathToStoredVideo);

            }else{
                EasyPermissions.requestPermissions(StudyMainActivity.this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }
    private String getFileDestinationPath(){
        String generatedFilename = String.valueOf(System.currentTimeMillis());
        String filePathEnvironment = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File directoryFolder = new File(filePathEnvironment + "/video/");
        if(!directoryFolder.exists()){
            directoryFolder.mkdir();
        }
        Log.d(TAG, "Full path " + filePathEnvironment + "/video/" + generatedFilename + ".mp4");
        return filePathEnvironment + "/video/" + generatedFilename + ".mp4";
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, StudyMainActivity.this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(uri != null){
            if(EasyPermissions.hasPermissions(StudyMainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
//                displayRecordedVideo.setVideoURI(uri);
//                displayRecordedVideo.start();

                pathToStoredVideo = getRealPathFromURIPath(uri, StudyMainActivity.this);
//                pathToStoredVideo = "label.mp4"

                Log.d(TAG, "Recorded Video Path " + pathToStoredVideo);
                //Store the video to your server
                uploadVideoToServer(pathToStoredVideo);

            }
        }
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "User has denied requested permission");
    }

    private void uploadVideoToServer(String pathToVideoFile){
        // progress bar thread
//        ProgressDialog progressdialog = ProgressDialog.show(this, "로딩중", "Loading..", true, false);

        File videoFile = new File(pathToVideoFile);
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
//        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", label, videoBody);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VideoInterface vInterface = retrofit.create(VideoInterface.class);
        Call<ResultObject> serverCom = vInterface.uploadVideoToServer(vFile);
        serverCom.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = (ResultObject)response.body();
                if(!TextUtils.isEmpty(result.getSuccess())){
                    String ox = result.getSuccess();
                    Log.d(TAG, "Result " + result.getSuccess());

                    // TODO : 받아온 OX, 확률값 레이아웃 띄우기
                    ImageView imageox = findViewById(R.id.oxview);
                    if(ox.equals("True")){
                        imageox.setImageResource(R.drawable.o);
                    }
                    else{
                        imageox.setImageResource(R.drawable.x);
                    }

                    // prob 띄우기
                    ListView problist = findViewById(R.id.problist);
                    ArrayList<String> prob = result.getProb();

//                    for(int i=0; i<5; i++){
//                        Float.parseFloat(prob.get(i));
//                        Log.d(TAG, "Result " + Float.parseFloat(prob.get(i)));
//                    }
                    ArrayAdapter<String> probadapter = new ArrayAdapter<String>(
                            getBaseContext(), android.R.layout.simple_list_item_1, prob
                    );
                    problist.setAdapter(probadapter);

                }
                else{
                    Toast.makeText(StudyMainActivity.this, "failure", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "failure");
                }

            }
            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Log.d(TAG, "Error message " + t.getMessage());
                Toast.makeText(StudyMainActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                Log.d(TAG, t.toString());
            }
        });
    }
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}