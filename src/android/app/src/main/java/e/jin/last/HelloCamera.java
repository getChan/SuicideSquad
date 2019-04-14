package e.jin.last;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.PersistableBundle;
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
    private static  final String TAG = "Camera";

    // camera control
    private Camera mCamera;
    // view Camera
    private ImageView mImage;
    // progress
    private  Boolean mInProgress;
    // Image Data
    byte[] data;
    DataOutputStream dos;


    // 카메라 미리보기 SurfaceView 의 리스너
    private SurfaceHolder.Callback mSurfaceListener =
            new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    // SurfaceView가 생성되면 카메라를 연다
                    mCamera = Camera.open();
                    Log.i(TAG, "Camera opend");
                    try {
                        mCamera.setPreviewDisplay(holder);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    // 미리보기 크기를 설정
                    Camera.Parameters parameters = mCamera.getParameters();
                    parameters.setPreviewSize(width, height);
                    mCamera.setParameters(parameters);
                    mCamera.startPreview();
                    Log.i(TAG, "Camera Preview Started");
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    // Surface가 삭제되는 시간에 카메라를 개방
                    mCamera.release();
                    mCamera = null;
                    Log.i(TAG, "Camera Released");
                }
            };
    private Camera.ShutterCallback mShutterListener = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            Log.i(TAG, "onShutter");
            if (mCamera != null && mInProgress == false){
                // 이미지 검색을 시작한다. 리스너 설정
                mCamera.takePicture(
                        mShutterListener, // 셔터 후
                        null, // Raw 이미지 생성 후
                        mPictureListener);  // JPEG 이미지 생성 후
                mInProgress = true;
            }
        }
    };

    // JPEG 이미지를 생성 후 호출
    private Camera.PictureCallback mPictureListener =
            new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    Log.i(TAG, "Picture Taken");
                    if (data!=null){
                        Log.i(TAG, "JPEG Picture Taken");

                        HelloCamera.this.data = data;
                        Bitmap bitmap =
                                BitmapFactory.decodeByteArray(data, 0, data.length, null);
                        // 이미지 뷰 이미지 설정
                        mImage.setImageBitmap(bitmap);
                        doFileUpload(); // 서버에 이미지를 전송하는 메서드 호출
                        Toast.makeText(HelloCamera.this, "서버에 파일 전송 성공",
                                Toast.LENGTH_LONG).show();
                        // 정지된 프리뷰를 재개
                        camera.startPreview();
                        mInProgress = false;
                        // 처리중 플래그를 떨어트림

                    }
                }
            };

    public void doFileUpload(){
        try {
            URL url = new URL("http://192.168.10.2:8080/image_upload.jsp");
            Log.i(TAG, "http://localhost/image_upload/");
            String lineend = "\r\n";
            String twoHypens = "--";
            String boundary = "*****";

            // open connection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true); // Input 허용
            con.setDoOutput(true); // output 허용
            con.setUseCaches(false); // cache copy를 허용하지 않는다.
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // write data
            DataOutputStream dos =
                    new DataOutputStream(con.getOutputStream());
            Log.i(TAG, "Open OutputStream");
            dos.writeBytes(twoHypens + boundary + lineend);
            dos.flush(); // finish uploads..
        }catch (Exception e){
            Log.i(TAG, "exception" +e.getMessage());
            // TODO : handle Exception
        }
        Log.i(TAG, data.length+"bytes written successed...finish!");
        try{dos.close();} catch (Exception e){}
    }

    ImageView view;
    SurfaceView surface;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.fragment_notifications);

        mImage = (ImageView)findViewById(R.id.image_view);

        surface = (SurfaceView)findViewById(R.id.surface_view);
        SurfaceHolder holder = surface.getHolder();
        view = (ImageView)findViewById(R.id.image_view);

        //SurfaceView Listener를 등록
        holder.addCallback(mSurfaceListener);
        // 외부 버퍼를 사용하도록 설정
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //TODO Auto-generated method stub

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode){
                case KeyEvent.KEYCODE_CAMERA:
                    //videoPreview.onCapture(settings);
                    mShutterListener.onShutter();

                    return true;

            }
        }
        return false;
    }
}
