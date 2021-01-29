package com.example.retailer_customers.interfaces;


import com.example.retailer_customers.models.MemberModel;
import com.example.retailer_customers.models.ProductModel;

public interface LoginCallback {
    void LoginSuccessCallback(MemberModel data);
    void LoginFailedCallback();

    void UpdateUserIdCallbackSuccess(MemberModel data);
    void UpdateUserIdCallbackFiler();

    interface RetailCallback {



        void CreateNewProducts(ProductModel obj);

        void PaymentStatusCallbackFiler();


    }
}
