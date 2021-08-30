package com.yummoidmkschinky.customerapp.retrofit;


import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST(APIClient.APPEND_URL + "e_reg_user.php")
    Call<JsonObject> createUser(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_login_user.php")
    Call<JsonObject> loginUser(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_address_user.php")
    Call<JsonObject> addressAdd(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_address_list.php")
    Call<JsonObject> addressList(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_home_data.php")
    Call<JsonObject> getHome(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_cart_data.php")
    Call<JsonObject> getcartData(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_cat_data.php")
    Call<JsonObject> getCarData(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_rest_data.php")
    Call<JsonObject> getRestorent(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_fav.php")
    Call<JsonObject> getRestorentFavrit(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_rate_data.php")
    Call<JsonObject> getRestorentRest(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_country_code.php")
    Call<JsonObject> getCodelist(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_check_coupon.php")
    Call<JsonObject> checkCoupon(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_couponlist.php")
    Call<JsonObject> getCouponList(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_order_now.php")
    Call<JsonObject> getOrderNow(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_order_history.php")
    Call<JsonObject> getOrderHistry(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_order_information.php")
    Call<JsonObject> getOrderDetalis(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_map_info.php")
    Call<JsonObject> getOrderMaps(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_fav_list.php")
    Call<JsonObject> getFavrits(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_offerlist.php")
    Call<JsonObject> getOffers(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_faq.php")
    Call<JsonObject> getFaq(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_rate_update.php")
    Call<JsonObject> getSendRates(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_rest_product_search.php")
    Call<JsonObject> getSearchProduct(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_rest_search.php")
    Call<JsonObject> getSearchRestorent(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "e_forget_password.php")
    Call<JsonObject> getForgot(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "e_mobile_check.php")
    Call<JsonObject> getMobileCheck(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "e_pagelist.php")
    Call<JsonObject> getEpagelist(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "e_add_delete.php")
    Call<JsonObject> removeAddress(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "e_order_cancle.php")
    Call<JsonObject> orderCancel(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "e_profile_edit.php")
    Call<JsonObject> updateProfile(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "e_wallet_activate.php")
    Call<JsonObject> walletActivate(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "e_wallet_report.php")
    Call<JsonObject> walletReport(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "e_wallet_up.php")
    Call<JsonObject> walletUpdate(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "e_paymentgateway.php")
    Call<JsonObject> paymentlist(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "e_getdata.php")
    Call<JsonObject> getRefercode(@Body RequestBody object);


}
