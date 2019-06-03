package e.jin.last;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class StudyMainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final int READ_REQUEST_CODE = 200;
    private Uri uri;
    private String pathToStoredVideo;
    private VideoView displayRecordedVideo;
    private static final String SERVER_PATH = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_study);
        displayRecordedVideo = (VideoView)findViewById(R.id.video_display);
        Button captureVideoButton = (Button)findViewById(R.id.capture_video);
        captureVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(videoCaptureIntent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE){
            uri = data.getData();
            if(EasyPermissions.hasPermissions(StudyMainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                displayRecordedVideo.setVideoURI(uri);
                displayRecordedVideo.start();

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
                displayRecordedVideo.setVideoURI(uri);
                displayRecordedVideo.start();

                pathToStoredVideo = getRealPathFromURIPath(uri, StudyMainActivity.this);
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
        File videoFile = new File(pathToVideoFile);
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
//                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VideoInterface vInterface = retrofit.create(VideoInterface.class);
        Call<ResultObject>  serverCom = vInterface.uploadVideoToServer(vFile);
        serverCom.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = response.body();
                if(!TextUtils.isEmpty(result.getSuccess())){
                    Toast.makeText(StudyMainActivity.this, "Result " + result.getSuccess(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Result " + result.getSuccess());
                }
            }
            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Log.d(TAG, "Error message " + t.getMessage());
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
