package com.example.retailer_customers.interfaces;


import com.example.retailer_customers.activity.CategoryModel;
import com.example.retailer_customers.models.MemberModel;
import com.example.retailer_customers.models.ProductModel;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Query;

public interface API {

//    @GET(ApplicationURL.LoginUser)
//    Call<UserModel> loginUser(@Query("deviceId") String deviceId, @Query("username") String username, @Query("pswd") String Password);




    @GET(ApplicationURL.CreateProduct)
    Call<ProductModel> create_products(@Query("group_code") String group_code, @Query("product_name") String product_name,
                                       @Query("mrp") String mrp, @Query("sp") String standard_price,
                                       @Query("description") String description, @Query("hsn") String hsnCode,
                                       @Query("tax") String tax, @Query("ptc") String product_code, @Query("status") String status);
    @GET(ApplicationURL.CreateMember)
    Call<MemberModel> create_member( @Query("member_name") String name,@Query("audio") String recordFile, @Query("user_name") String userName,
                                    @Query("password") String login_pin, @Query("dob") Date birth_date,
                                    @Query("district") String district, @Query("pin") Integer pin_code,
                                    @Query("email") String email, @Query("phone") String phone_no, @Query("device_id") String deviceId,
                                    @Query("status") String status);

    @GET(ApplicationURL.CreateMember)
    Call<MemberModel> create_smember(  @Query("audio") String attachedFile);

    @GET(ApplicationURL.GetAllCategories)
    Call<ArrayList<CategoryModel>> getAllCategories();

    @GET(ApplicationURL.GetAllProducts)
    Call<ArrayList<ProductModel>> getAllProducts();





}
