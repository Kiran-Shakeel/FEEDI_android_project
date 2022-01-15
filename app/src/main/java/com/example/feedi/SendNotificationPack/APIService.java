package com.example.feedi.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAtSyb5Sg:APA91bFozH4D_VdDqrczmgTqeMnIThSzO8Ex6e4bBkAMQ_ykY80FsU3yIWXK4YkleyvXUpz-LYdPS1SvWi-VpFEA_al182xLiJKlTiedjiGgBakR9WqaeMj5uWieNFpX8RgDW_2kK7Hr" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

