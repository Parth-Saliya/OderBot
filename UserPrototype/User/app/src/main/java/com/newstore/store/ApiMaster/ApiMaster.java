package com.newstore.store.ApiMaster;

import com.newstore.store.Model.ProductListModel;
import com.newstore.store.Model.QRModel;
import com.newstore.store.Model.SignUpModel;
import com.newstore.store.Model.StoreListModel;
import com.newstore.store.Model.UserDetailModel;
import com.newstore.store.Model.UserEditModel;
import com.newstore.store.Model.UserLoginModel;
import com.newstore.store.Model.UserOrderCancelModel;
import com.newstore.store.Model.UserOrderDetailModel;
import com.newstore.store.Model.UserOrderListModel;
import com.newstore.store.Model.UserOrderModel;
import com.newstore.store.Model.UserReviewModel;
import com.newstore.store.Model.UserReviewShowModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiMaster {

    //SignUp
    @FormUrlEncoded
    @POST("prototype/userSignUp")
    Call<SignUpModel> signUp(@Field("uFirstName") String uFirstName,
                             @Field("uLastName") String uLastName,
                             @Field("uMobile") String uMobile,
                             @Field("uEmail") String uEmail,
                             @Field("uPassword") String uPassword,
                             @Field("uImage") String uImage);

    //Login
    @FormUrlEncoded
    @POST("prototype/userLogin")
    Call<UserLoginModel> userLogin(@Field("uEmail") String uEmail,
                                   @Field("uPassword") String uPassword);

    //Store Detail
    @FormUrlEncoded
    @POST("prototype/userDetail")
    Call<UserDetailModel> userDetail(@Field("uId") String uId);

    //Store List
    @GET("prototype/storeList")
    Call<StoreListModel> storeList();


    //Product List
    @GET("prototype/storeUserProductList")
    Call<ProductListModel> storeProductList(@Query("sLat") String sLat,
                                            @Query("sLog") String sLog);

    //Order
    @FormUrlEncoded
    @POST("prototype/userOrder")
    Call<UserOrderModel> userOrder(@Field("uId") String uId,
                                   @Field("sId") String sId,
                                   @Field("fAmount") String fAmount,
                                   @Field("pDetail") String pDetail);

    //Order List
    @GET("prototype/userOrderList")
    Call<UserOrderListModel> userOrderList(@Query("uId") String uId);

    //Order Detail
    @GET("prototype/userOrderDetail")
    Call<UserOrderDetailModel> userOrderDetail(@Query("oId") String oId,
                                               @Query("uId") String uId);

    //QR Code
    @GET("prototype/userScan")
    Call<QRModel> userScan(@Query("oId") String oId,
                           @Query("uId") String uId);

    //Order Cancel
    @GET("prototype/userOrderCancel")
    Call<UserOrderCancelModel> userOrderCancel(@Query("oId") String oId,
                                               @Query("uId") String uId);

    //Edit Profile
    @FormUrlEncoded
    @POST("prototype/userEditProfile")
    Call<UserEditModel> userEditProfile(@Field("uFirstName") String uFirstName,
                                        @Field("uLastName") String uLastName,
                                        @Field("uMobile") String uMobile,
                                        @Field("uImage") String uImage,
                                        @Field("uId") String uId);

    //User Review
    @FormUrlEncoded
    @POST("prototype/userReview")
    Call<UserReviewModel> userReview(@Field("oId") String oId,
                                     @Field("sId") String sId,
                                     @Field("uId") String uId,
                                     @Field("pId") String pId,
                                     @Field("qOne") String qOne,
                                     @Field("qTwo") String qTwo,
                                     @Field("review") String review);

    //User Review Show
    @FormUrlEncoded
    @POST("prototype/userReviewShow")
    Call<UserReviewShowModel> userReviewShow(@Field("oId") String oId,
                                             @Field("sId") String sId,
                                             @Field("uId") String uId,
                                             @Field("pId") String pId);

}
