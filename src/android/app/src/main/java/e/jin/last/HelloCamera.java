package e.jin.last;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HelloCamera extends Activity {

    private static final String TAG = "HelloCamera";

    // 카메라 제어
    private Camera mCamera;
    // 촬영 사진보기
    private ImageView mImage;
    // 처리중

    private boolean mInProgress;
    //카메라에 찍힌 이미지 데이터
    byte[] data;
    DataOutputStream dos;


    // 카메라 미리보기 SurfaceView의 리스너
    private SurfaceHolder.Callback mSurfaceListener =
            new SurfaceHolder.Callback() {

                public void surfaceCreated(SurfaceHolder holder) {
                    // SurfaceView가 생성되면 카메라를 연다.
                    mCamera = Camera.open();
                    Log.i(TAG, "Camera opened");
                    try {
                        mCamera.setPreviewDisplay(holder);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void surfaceDestroyed(SurfaceHolder holder) {
                    // SurfaceView가 삭제되는 시간에 카메라를 개방
                    mCamera.release();
                    mCamera = null;
                    Log.i(TAG, "Camera released");
                }

                public void surfaceChanged(SurfaceHolder holder,
                                           int format,
                                           int width,
                                           int height) {
                    // 미리보기 크기를 설정
                    Camera.Parameters parameters =
                            mCamera.getParameters();
                    parameters.setPreviewSize(width, height);
                    mCamera.setParameters(parameters);
                    mCamera.startPreview();
                    Log.i(TAG, "Camera preview started");
                }
            };



    // 카메라 셔트가 눌러질때
    private Camera.ShutterCallback mShutterListener =
            new Camera.ShutterCallback() {

                // 이미지를 처리하기 전에 호출된다.
                public void onShutter() {
                    Log.i(TAG, "onShutter");
                    if (mCamera != null && mInProgress == false) {
                        // 이미지 검색을 시작한다. 리스너 설정
                        mCamera.takePicture(
                                mShutterListener,  // 셔터 후
                                null, // Raw 이미지 생성 후
                                mPicutureListener); // JPE 이미지 생성 후
                        mInProgress = true;

                    }
                }

            };



    // JPEG 이미지를 생성 후 호출
    private Camera.PictureCallback mPicutureListener =
            new Camera.PictureCallback() {

                public void onPictureTaken(byte[] data, Camera camera) {
                    Log.i(TAG, "Picture Taken");
                    if (data != null) {
                        Log.i(TAG, "JPEG Picture Taken");

                        //  적용할 옵션이 있는 경우 BitmapFactory클래스의 Options()
                        //  메서드로 옵션객체를 만들어 값을 설정하며
                        //  이렇게 만들어진 옵션을 Bitmap 객체를 만들때 네번째
                        //  아규먼트로 사용한다.
                        //
                        //  처리하는 이미지의 크기를 축소
                        //  BitmapFactory.Options options =
                        //      new BitmapFactory.Options();
                        //  options.inSampleSize = IN_SAMPLE_SIZE;
                        HelloCamera.this.data=data;
                        Bitmap bitmap =
                                BitmapFactory.decodeByteArray(data,
                                        0,
                                        data.length,
                                        null);
                        //이미지 뷰 이미지 설정
                        mImage.setImageBitmap(bitmap);
                        doFileUpload();  //서버에 이미지를 전송하는 메서드 호출
                        Toast.makeText(HelloCamera.this, "서버에 파일을 성공적으로 전송하였습니다",
                                Toast.LENGTH_LONG).show();
                        // 정지된 프리뷰를 재개
                        camera.startPreview();
                        mInProgress = false;

                        // 처리중 플래그를 떨어뜨림

                    }
                }

            };


    public void doFileUpload() {
        try {
            URL url = new URL("http://192.168.10.2:8080/image_upload.jsp");
            Log.i(TAG, "http://localhost/image_upload.jsp" );
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            // open connection
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoInput(true); //input 허용
            con.setDoOutput(true);  // output 허용
            con.setUseCaches(false);   // cache copy를 허용하지 않는다.
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // write data
            DataOutputStream dos =
                    new DataOutputStream(con.getOutputStream());
            Log.i(TAG, "Open OutputStream" );
            dos.writeBytes(twoHyphens + boundary + lineEnd);



            // 파일 전송시 파라메터명은 file1 파일명은 camera.jpg로 설정하여 전송
            dos.writeBytes("Content-Disposition: form-data; name=\"file1\";filename=\"camera.jpg\"" +

                    lineEnd);


            dos.writeBytes(lineEnd);
            dos.write(data,0,data.length);
            Log.i(TAG, data.length+"bytes written" );
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush(); // finish upload...

        } catch (Exception e) {
            Log.i(TAG, "exception " + e.getMessage());
            // TODO: handle exception
        }
        Log.i(TAG, data.length+"bytes written successed ... finish!!" );
        try { dos.close(); } catch(Exception e){}


    }

    ImageView view;
    SurfaceView surface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera2_layout);

        mImage = (ImageView) findViewById(R.id.image_view);

        surface =
                (SurfaceView) findViewById(R.id.surface_view);
        findViewById()
        SurfaceHolder holder = surface.getHolder();
        view=(ImageView)findViewById(R.id.image_view);

        // SurfaceView 리스너를 등록
        holder.addCallback(mSurfaceListener);
        // 외부 버퍼를 사용하도록 설정
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    // 키가 눌러졌을때 카메라 셔트가 눌러졌다고 이벤트 처리설정
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if(event.getAction() == KeyEvent.ACTION_DOWN) {
            switch(keyCode) {
                case KeyEvent.KEYCODE_CAMERA:
                    //videoPreview.onCapture(settings);
                    mShutterListener.onShutter();
                    /* ... */
                    return true;
            }
        }
        return false;
    }


}