package com.example.retailer_customers.api;


import android.content.Context;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.widget.Toast;


import com.example.retailer_customers.OnCompleteCallBack;
import com.example.retailer_customers.activity.CategoryModel;
import com.example.retailer_customers.interfaces.API;
import com.example.retailer_customers.interfaces.LoginCallback;
import com.example.retailer_customers.models.MemberModel;
import com.example.retailer_customers.models.ProductModel;
import com.example.retailer_customers.utils.CommonUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ApiService {

    Context context;
    LoginCallback.RetailCallback retailCallback;



    public ApiService(Context context, LoginCallback.RetailCallback callback) {

        this.context = context;
        this.retailCallback = callback;

    }
    public ApiService(Context context) {

        this.context = context;


    }


    public void syncProducts(ProductModel productModel) {
        API apiInterface = APIClient.getClient().create(API.class);

        Call call1 = apiInterface.create_products(CommonUtils.toString(productModel.getGroup_code()),productModel.getName(),
                productModel.getMrp().toPlainString(),productModel.getSp().toPlainString(),productModel.getDescription(),productModel.getHsn(),
                productModel.getTax().toPlainString(),CommonUtils.toString(productModel.getPtc()),"1");

        try {
            call1.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    if (response.code() == 200) {
                        if (response.body().toString().length() > 0) {

                            ProductModel model = (ProductModel) response.body();


                            retailCallback.CreateNewProducts(model);
                        } else {
                            retailCallback.PaymentStatusCallbackFiler();
                        }

                    } else {

                        retailCallback.PaymentStatusCallbackFiler();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    retailCallback.PaymentStatusCallbackFiler();
                    Toast.makeText(context, "Network Failed", Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception er) {
            retailCallback.PaymentStatusCallbackFiler();
        }
    }


    public void GetAllpendingMembers(OnCompleteCallBack onCompleteCallBack) {
        API apiInterface = APIClient.getClient().create(API.class);
        Call call1 = apiInterface.getAllProducts();

        try {
            call1.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    if (response.code() == 200) {
                        if (response.body().toString().length() > 0) {

                            ArrayList<MemberModel> model = (ArrayList<MemberModel>) response.body();

                            onCompleteCallBack.onCompleteCallBack(model);
                        } else {
                            onCompleteCallBack.onErrorCallBAck();
                        }

                    } else {

                        onCompleteCallBack.onErrorCallBAck();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    onCompleteCallBack.onErrorCallBAck();
                    Toast.makeText(context, "Network Failed", Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception er) {
            onCompleteCallBack.onErrorCallBAck();
        }
    }

    public void GetAllCAtegories(OnCompleteCallBack onCompleteCallBack) {
        API apiInterface = APIClient.getClient().create(API.class);
        Call call1 = apiInterface.getAllCategories();

        try {
            call1.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    if (response.code() == 200) {
                        if (response.body().toString().length() > 0) {

                            ArrayList<CategoryModel> model = (ArrayList<CategoryModel>) response.body();

                            onCompleteCallBack.onCompleteCallBack(model);
                        } else {
                            onCompleteCallBack.onErrorCallBAck();
                        }

                    } else {

                        onCompleteCallBack.onErrorCallBAck();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    onCompleteCallBack.onErrorCallBAck();
                    Toast.makeText(context, "Network Failed", Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception er) {
            onCompleteCallBack.onErrorCallBAck();
        }
    }

    public void GetAllProductList(OnCompleteCallBack onCompleteCallBack) {
        API apiInterface = APIClient.getClient().create(API.class);
        Call call1 = apiInterface.getAllProducts();

        try {
            call1.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    if (response.code() == 200) {
                        if (response.body().toString().length() > 0) {

                            ArrayList<ProductModel> model = (ArrayList<ProductModel>) response.body();

                            onCompleteCallBack.onCompleteCallBack(model);
                        } else {
                            onCompleteCallBack.onErrorCallBAck();
                        }

                    } else {

                        onCompleteCallBack.onErrorCallBAck();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    onCompleteCallBack.onErrorCallBAck();
                    Toast.makeText(context, "Network Failed", Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception er) {
            onCompleteCallBack.onErrorCallBAck();
        }
    }


    public void createNewMember(MemberModel memberModel, File recordFile, OnCompleteCallBack onCompleteCallBack)  {
        API apiInterface = APIClient.getClient().create(API.class);

       // byte[] bytes = FileUtils.(recordFile);

        InputStream inputStream = null;//You can get an inputStream using any IO API
        try {
            inputStream = new FileInputStream(recordFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output64.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String attachedFile = output.toString();


        Call call1 = apiInterface.create_member(memberModel.getName(),attachedFile,memberModel.getUserName(),
                memberModel.getLogin_pin(),memberModel.getBirth_date(),memberModel.getDistrict(),memberModel.getPin_code(),
                memberModel.getEmail(),memberModel.getPhone_no(),memberModel.getDeviceId(),memberModel.getStatus());
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), recordFile);
//        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", recordFile.getName(), requestBody);
//        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), recordFile.getName());


//        // creates RequestBody instance from file
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), recordFile);
//        // MultipartBody.Part is used to send also the actual filename
//        MultipartBody.Part body = MultipartBody.Part.createFormData("file", recordFile.getName(), requestFile);
//        // adds another part within the multipart request
//        String descriptionString = "Sample description";
//       // RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
//        // executes the request

       // RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), recordFile);
      //  MultipartBody.Part body = MultipartBody.Part.createFormData("upload", recordFile.getName(), reqFile);
//



        File filess=saveImage(context,attachedFile);





        //Call call1 = apiInterface.create_member(attachedFile);


        try {
            call1.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    if (response.code() == 200) {
                        if (response.body().toString().length() > 0) {

                            MemberModel model = (MemberModel) response.body();


                            onCompleteCallBack.onCompleteCallBack(model);
                        } else {
                            onCompleteCallBack.onErrorCallBAck();
                        }

                    } else {

                        onCompleteCallBack.onErrorCallBAck();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    onCompleteCallBack.onErrorCallBAck();
                    Toast.makeText(context, "Network Failed", Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception er) {
            onCompleteCallBack.onErrorCallBAck();
        }
    }


        public static File saveImage(final Context context, final String imageData) {
            final byte[] imgBytesData = Base64.decode(imageData,
                    Base64.DEFAULT);

             File file = null;
            File root = android.os.Environment.getExternalStorageDirectory();
            file = new File(root.getAbsolutePath() + "/Members/Images/gfffg.mp3");
            //file = File.createTempFile("image", null, context.getCacheDir());
            final FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }

            final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    fileOutputStream);
            try {
                bufferedOutputStream.write(imgBytesData);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return file;
        }



}
