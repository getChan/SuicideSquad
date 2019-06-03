package e.jin.last;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
public interface VideoInterface {
    @Multipart
    @POST("/imagefolder/index.php")
    Call<ResultObject> uploadVideoToServer(@Part MultipartBody.Part video);
}