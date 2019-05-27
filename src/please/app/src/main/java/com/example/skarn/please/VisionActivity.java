package com.example.skarn.please;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class VisionActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 400;
    private static final int READ_REQUEST_CODE = 200;

    private String pathToStoredImage;
    private ImageView displayImage;
    private static final String SERVER_PATH = "http://117.16.244.58:3000/";
    String label = "";


    private String imageFilePath;
    private Uri photoUri;
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vision_activity);
        displayImage = (ImageView) findViewById(R.id.image_display);
        Button captureImageButton = (Button) findViewById(R.id.capture_image);
        captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (imageCaptureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try{
                        photoFile = createImageFile();
                    }catch (IOException ex){
                        // Error occurred while creating the File
                    }
                    if(photoFile != null){
                        photoUri = FileProvider.getUriForFile(VisionActivity.this, getPackageName(), photoFile);

                        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(imageCaptureIntent, REQUEST_IMAGE_CAPTURE);
                    }
//                    startActivityForResult(imageCaptureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            displayImage.setImageBitmap(imageBitmap);

            if (EasyPermissions.hasPermissions(VisionActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                displayImage.setImageURI(photoUri);
                Log.d(TAG, "DEBUGZZ"+imageFilePath);
//                pathToStoredImage = getRealPathFromURIPath(photoUri, VisionActivity.this);
//                Log.d(TAG, "Recorded Image Path " + pathToStoredImage);
                //Store the video to your server
                uploadImageToServer(imageFilePath.toString());

            } else {
                EasyPermissions.requestPermissions(VisionActivity.this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private String getFileDestinationPath() {
        String generatedFilename = String.valueOf(System.currentTimeMillis());
        String filePathEnvironment = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File directoryFolder = new File(filePathEnvironment + "/vision/");
        if (!directoryFolder.exists()) {
            directoryFolder.mkdir();
        }
        Log.d(TAG, "Full path " + filePathEnvironment + "/vision/" + generatedFilename + ".jpg");
        return filePathEnvironment + "/vision/" + generatedFilename + ".jpg";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, VisionActivity.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (photoUri != null) {
            if (EasyPermissions.hasPermissions(VisionActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                displayImage.setImageURI(photoUri);

                pathToStoredImage = getRealPathFromURIPath(photoUri, VisionActivity.this);
//                pathToStoredVideo = "label.mp4"

                Log.d(TAG, "Recorded Image Path " + pathToStoredImage);
                //Store the video to your server
                uploadImageToServer(pathToStoredImage);
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "User has denied requested permission");
    }

    private void uploadImageToServer(String pathToImageFile) {
        // progress bar thread
//        ProgressDialog progressdialog = ProgressDialog.show(this, "로딩중", "Loading..", true, false);

        File imageFile = new File(pathToImageFile);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
//        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);
        //TODO : 이미지 이름 설정
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("photo", "ex.jpg", imageBody);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ImageInterface imageInterface= retrofit.create(ImageInterface.class);
        Call<ResultObject> serverCom = imageInterface.uploadImageToServer(vFile);
        serverCom.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = (ResultObject) response.body();
                if (!TextUtils.isEmpty(result.getSuccess())) {
                    String visionId = result.getSuccess();
                    Toast.makeText(VisionActivity.this, "Result " + visionId, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Result " + result.getSuccess());

                    DictionaryResultFragment dictionaryResultFragment= new DictionaryResultFragment();
                    Bundle bundle = new Bundle(1);
                    bundle.putString("id", visionId);
                    dictionaryResultFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.dictionaryfrag, dictionaryResultFragment).commit();

                } else {
                    Toast.makeText(VisionActivity.this, "failure", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "failure");
                }

            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Log.d(TAG, "Error message " + t.getMessage());
                Toast.makeText(VisionActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                Log.d(TAG, t.toString());

                DictionaryResultFragment dictionaryResultFragment= new DictionaryResultFragment();
                Bundle bundle = new Bundle(1);
                bundle.putString("dataid", "visionId");
                dictionaryResultFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.dictionaryfrag, dictionaryResultFragment).commit();
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
