package com.example.retailer_customers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.retailer_customers.OnCompleteCallBack;
import com.example.retailer_customers.R;
import com.example.retailer_customers.api.ApiService;
import com.example.retailer_customers.models.ProductModel;
import com.example.retailer_customers.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListActivity extends AppCompatActivity implements OnCompleteCallBack {
    RecyclerView recyclerView;
    //ProductDao productDao;
    ProductListAdapter productListAdapter;
    TextView textView;
    ImageView imageView;
    List<ProductModel> productModels=new ArrayList<>();
    int cartQty;
    TextView cartcount;
    ApiService web;
    List<CategoryModel> categoryModelList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productlist_activity1);
        web = new ApiService(getApplicationContext());

        web.GetAllCAtegories(new OnCompleteCallBack() {
            @Override
            public void onCompleteCallBack(Object data) {
                 categoryModelList=(List<CategoryModel>)data;
            }

            @Override
            public void onErrorCallBAck() {

            }
        });

        web.GetAllProductList(new OnCompleteCallBack() {
            @Override
            public void onCompleteCallBack(Object data) {
                productModels=(List<ProductModel>)data;
                refreshview();
            }

            @Override
            public void onErrorCallBAck() {

            }
        });
        initui();
        initDb();
        refreshview();
    }


    public void refreshview() {


         //productModels=productDao.selectall(1);

        cartcount.setText(CommonUtils.toString(cartQty));
        setadaper(productModels);
    }


    public void setadaper( List<ProductModel> productModels){
        productListAdapter = new ProductListAdapter(productModels,this);
        recyclerView.setAdapter(productListAdapter);
        productListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {

        refreshview();

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        for(int i = 0; i < menu.size(); i++) {
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
         if (i == R.id.add) {
            //Intent intent = new Intent( this, CreateNewProductActivity.class );
           // ProductListActivity.this.startActivity( intent );
        }

        return super.onOptionsItemSelected( item );
    }

    private void initui() {
        textView=(TextView) findViewById( R.id.textView );
        recyclerView=(RecyclerView)findViewById( R.id.recyclerView );
        //imageView=(ImageView) findViewById( R.id.addimage );
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new ItemDecorator(getApplicationContext()));
        cartcount=(TextView) findViewById( R.id.cartcount );
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(getApplicationContext(), CreateNewProductActivity.class);
               // Bundle bundle = new Bundle();
                //intent.putExtra("isFromApprove", true);
               // intent.putExtra("memberId", memberModel.getId());
              //  startActivity(intent);
            }
        });
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), CreateNewProductActivity.class);
//                Bundle bundle = new Bundle();
//                //intent.putExtra("isFromApprove", true);
//                // intent.putExtra("memberId", memberModel.getId());
//                startActivity(intent);
//            }
//        });

        EditText search=(EditText)findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    search(s.toString());


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initDb() {
        //productDao=new ProductDao( getApplicationContext() );
    }

    public void search(String text) {
        List<ProductModel> filteredList;
        if (productModels == null || productModels.isEmpty()) {
            return;
        }

        if (text == null || text.isEmpty()) {
            filteredList = productModels;
        } else {
            List<ProductModel> temp = new ArrayList<>();
            for (ProductModel memberModel : productModels) {
                if (memberModel.getName() != null && memberModel.getName().toLowerCase().contains( text.toLowerCase() )) {
                    temp.add( memberModel );
                }
            }
            filteredList = temp;
        }


        setadaper(filteredList);

    }

    @Override
    public void onCompleteCallBack(Object data) {
       // if (data instanceof MemberModel) {
        //    MemberModel memberModel = (MemberModel) data;

            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtra("isFromApprove", true);
         //   intent.putExtra("memberId", memberModel.getId());
            startActivity(intent);
       // }

    }

    @Override
    public void onErrorCallBAck() {

    }

    private void addRollDialogue(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout. add_to_cart, null);
        builder.setView(dialogView);
        final TextView name=(TextView)dialogView.findViewById(R.id.txtProductName);
        final EditText qty=(EditText)dialogView.findViewById(R.id.txtqty);
        //final CheckBox isActive=(CheckBox)dialogView.findViewById(R.id.isActive);
        Button submit=(Button)dialogView.findViewById(R.id.add_button);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!qty.getText().toString().isEmpty() && CommonUtils.toInt(qty.getText().toString()) > 0) {
                    cartQty=cartQty+ CommonUtils.toInt(qty.getText().toString());

                    Toast.makeText(getApplicationContext(), "Product added to Cart", Toast.LENGTH_SHORT).show();

                }
            }

        });
        builder.show();
    }



}
