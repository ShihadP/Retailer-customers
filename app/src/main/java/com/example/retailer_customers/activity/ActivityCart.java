package com.example.retailer_customers.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.retailer_customers.OnCompleteCallBack;
import com.example.retailer_customers.R;
import com.example.retailer_customers.api.ApiService;
import com.example.retailer_customers.models.ProductModel;
import com.example.retailer_customers.utils.ItemDecorator;

import java.util.List;

public class ActivityCart extends AppCompatActivity implements OnCompleteCallBack {
    RecyclerView recyclerView;
    CartListAdapter cartListAdapter;
    ApiService web;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        web = new ApiService(getApplicationContext());
        initui();

        web.GetAllProductList(new OnCompleteCallBack() {
            @Override
            public void onCompleteCallBack(Object data) {
                List<ProductModel> productModels = (List<ProductModel>) data;
                //progressBar.setVisibility(View.GONE);
                setadaper(productModels);
            }

            @Override
            public void onErrorCallBAck() {
                //progressBar.setVisibility(View.GONE);
            }
        });

        //setadaper();
    }

    private void initui() {
        //progressBar = findViewById(R.id.pb_loading);
        //textView=(TextView) findViewById( R.id.textView );
        recyclerView=(RecyclerView)findViewById( R.id.recyclerView );
        //imageView=(ImageView) findViewById( R.id.addimage );
        recyclerView=(RecyclerView)findViewById( R.id.recyclerView );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new ItemDecorator(getApplicationContext()));

    }


    public void setadaper( List<ProductModel> productModels){
        cartListAdapter = new CartListAdapter(productModels,this);
        recyclerView.setAdapter(cartListAdapter);
        cartListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCompleteCallBack(Object data) {

    }

    @Override
    public void onErrorCallBAck() {

    }
}