package com.example.retailer_customers.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retailer_customers.OnCompleteCallBack;
import com.example.retailer_customers.R;
import com.example.retailer_customers.models.ProductModel;


import java.io.File;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private List<ProductModel> productModels;
    private List<CategoryModel> categoryModels;
    OnCompleteCallBack onCompleteCallBack;
    boolean isCategory=false;


    public ProductListAdapter(List<ProductModel> productModels, OnCompleteCallBack completeCallBack) {
        this.onCompleteCallBack = completeCallBack;
        this.productModels = productModels;
    }
    public ProductListAdapter(List<CategoryModel> categoryModels,boolean isCategory, OnCompleteCallBack completeCallBack) {
        this.onCompleteCallBack = completeCallBack;
        this.categoryModels = categoryModels;
        this.isCategory=isCategory;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView product_name;
        public ImageView imageView;



        public MyViewHolder(View view) {
            super( view );
            //textDate = (TextView) view.findViewById( R.id.dateofbirth );
            product_name = (TextView) view.findViewById( R.id.txtProductName );

            imageView = (ImageView) view.findViewById( R.id.image );


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.product_item_list, parent, false );
        return new MyViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

       if (isCategory){
           final CategoryModel eachItem = categoryModels.get(position);


           if (eachItem.getImage() != null) {

               File imgFile = new File(eachItem.getImage());

               if (imgFile.exists()) {

                   Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                   holder.imageView.setImageBitmap(myBitmap);

               }
           }


           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   onCompleteCallBack.onCompleteCallBack(eachItem);
               }
           });

           holder.product_name.setText(eachItem.getCategory());


    }else {
           final ProductModel eachItem = productModels.get(position);


           if (eachItem.getImagePath() != null) {

               File imgFile = new File(eachItem.getImagePath());

               if (imgFile.exists()) {

                   Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                   holder.imageView.setImageBitmap(myBitmap);

               }
           }


           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   onCompleteCallBack.onCompleteCallBack(eachItem);
               }
           });

           holder.product_name.setText(eachItem.getName());
       }

    }


    @Override
    public int getItemCount() {
        if (isCategory){
            return categoryModels.size();
        }else {
            return productModels.size();
        }
    }




    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView( recyclerView );
    }

}
