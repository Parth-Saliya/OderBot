package com.newstore.storeadmin.ApiMaster;

import com.newstore.storeadmin.Model.ProductAddModel;
import com.newstore.storeadmin.Model.ProductDeleteModel;
import com.newstore.storeadmin.Model.ProductEditModel;
import com.newstore.storeadmin.Model.ProductListModel;
import com.newstore.storeadmin.Model.SignUpModel;
import com.newstore.storeadmin.Model.StoreDetailModel;
import com.newstore.storeadmin.Model.StoreEditModel;
import com.newstore.storeadmin.Model.StoreLatLangModel;
import com.newstore.storeadmin.Model.StoreLoginModel;
import com.newstore.storeadmin.Model.StoreOrderDetailModel;
import com.newstore.storeadmin.Model.StoreOrderListModel;
import com.newstore.storeadmin.Model.UserReviewShowModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiMaster {

    //SignUp
    @FormUrlEncoded
    @POST("prototype/storeSignUp")
    Call<SignUpModel> signUp(@Field("sName") String sName,
                             @Field("sAddress") String sAddress,
                             @Field("sMobile") String sMobile,
                             @Field("sEmail") String sEmail,
                             @Field("sPassword") String sPassword,
                             @Field("sImage") String sImage);

    //Store LatLng
    @FormUrlEncoded
    @POST("prototype/storeLatLng")
    Call<StoreLatLangModel> storeLatLang(@Field("sLat") String sLat,
                                         @Field("sLng") String sLng,
                                         @Field("sId") String sId);

    //Login
    @FormUrlEncoded
    @POST("prototype/storeLogin")
    Call<StoreLoginModel> storeLogin(@Field("sEmail") String sEmail,
                                     @Field("sPassword") String sPassword);

    //Store Detail
    @FormUrlEncoded
    @POST("prototype/storeDetail")
    Call<StoreDetailModel> storeDetail(@Field("sId") String sId);

    //Product Add
    @FormUrlEncoded
    @POST("prototype/storeProductAdd")
    Call<ProductAddModel> storeProductAdd(@Field("pName") String pName,
                                          @Field("pDesc") String pDesc,
                                          @Field("pQty") String pQty,
                                          @Field("pAmount") String pAmount,
                                          @Field("pImage") String pImage,
                                          @Field("sId") String sId);

    //Product List
    @GET("prototype/storeProductList")
    Call<ProductListModel> storeProductList(@Query("sId") String sId);

    //Product Delete
    @FormUrlEncoded
    @POST("prototype/storeProductDelete")
    Call<ProductDeleteModel> storeProductDelete(@Field("sId") String sId,
                                                @Field("pId") String pId);

    //Product Add
    @FormUrlEncoded
    @POST("prototype/storeProductEdit")
    Call<ProductEditModel> storeProductEdit(@Field("pName") String pName,
                                            @Field("pDesc") String pDesc,
                                            @Field("pQty") String pQty,
                                            @Field("pAmount") String pAmount,
                                            @Field("pImage") String pImage,
                                            @Field("pId") String pId,
                                            @Field("sId") String sId);

    //Order List
    @GET("prototype/storeOrderList")
    Call<StoreOrderListModel> storeOrderList(@Query("sId") String sId);

    //Order Detail
    @GET("prototype/storeOrderDetail")
    Call<StoreOrderDetailModel> storeOrderDetail(@Query("oId") String oId,
                                                 @Query("sId") String sId);

    //Edit Profile
    @FormUrlEncoded
    @POST("prototype/storeEditProfile")
    Call<StoreEditModel> storeEditProfile(@Field("sName") String sName,
                                          @Field("sAddress") String sAddress,
                                          @Field("sMobile") String sMobile,
                                          @Field("sLat") String sLat,
                                          @Field("sLog") String sLog,
                                          @Field("sImage") String sImage,
                                          @Field("sId") String sId);

    //User Review Show
    @FormUrlEncoded
    @POST("prototype/userReviewShow")
    Call<UserReviewShowModel> userReviewShow(@Field("oId") String oId,
                                             @Field("sId") String sId,
                                             @Field("uId") String uId,
                                             @Field("pId") String pId);


}
