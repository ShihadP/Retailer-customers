package com.example.retailer_customers.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retailer_customers.OnCompleteCallBack;
import com.example.retailer_customers.R;
import com.example.retailer_customers.Splash;
import com.example.retailer_customers.api.ApiService;
import com.example.retailer_customers.models.ProductModel;
import com.example.retailer_customers.utils.CommonUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity1 extends AppCompatActivity implements OnCompleteCallBack {
    ApiService web;
    RecyclerView recyclerView;
    //ProductDao productDao;
    ProductListAdapter productListAdapter;
    TextView textView;
    ImageView imageView;
    List<ProductModel> productModels=new ArrayList<>();
    int cartQty;
    TextView cartcount;
    List<CategoryModel> categoryModelList=new ArrayList<>();

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    ProgressBar progressBar;
    TextView txtheader;
    boolean isCategory=true,isProducts=false,isCart=false,isProfile=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list1);



        initui();
        initDb();

        web = new ApiService(getApplicationContext());


        refreshview();
    }


    public void refreshview() {

        progressBar.setVisibility(View.VISIBLE);
        if (isCategory) {
            web.GetAllCAtegories(new OnCompleteCallBack() {
                @Override
                public void onCompleteCallBack(Object data) {
                    categoryModelList = (List<CategoryModel>) data;
                    progressBar.setVisibility(View.GONE);
                    setadaper();
                }

                @Override
                public void onErrorCallBAck() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else {

            web.GetAllProductList(new OnCompleteCallBack() {
                @Override
                public void onCompleteCallBack(Object data) {
                    productModels = (List<ProductModel>) data;
                    progressBar.setVisibility(View.GONE);
                    setadaper();
                }

                @Override
                public void onErrorCallBAck() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        //productModels=productDao.selectall(1);

        //cartcount.setText(CommonUtils.toString(cartQty));
        setadaper();
    }


    public void setadaper() {
        if (isCategory){
            productListAdapter = new ProductListAdapter(categoryModelList, true,this);
        }else {
            productListAdapter = new ProductListAdapter(productModels, this);
        }

        recyclerView.setAdapter(productListAdapter);
        productListAdapter.notifyDataSetChanged();
    }
    public void setadaper(List<ProductModel> productModels) {
        if (isCategory){
            productListAdapter = new ProductListAdapter(categoryModelList, true,this);
        }else {
            productListAdapter = new ProductListAdapter(productModels, this);
        }

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
        for (int i = 0; i < menu.size(); i++) {
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

        return super.onOptionsItemSelected(item);
    }

    private void initui() {
        progressBar = findViewById(R.id.pb_loading);
        viewPager = (ViewPager) findViewById(R.id.viewPager1);
        txtheader = (TextView) findViewById(R.id.header);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        textView = (TextView) findViewById(R.id.textView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //imageView=(ImageView) findViewById( R.id.addimage );
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new ItemDecorator(getApplicationContext()));
        cartcount = (TextView) findViewById(R.id.cartcount);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Intent intent = new Intent(getApplicationContext(), CreateNewProductActivity.class);
//                // Bundle bundle = new Bundle();
//                //intent.putExtra("isFromApprove", true);
//                // intent.putExtra("memberId", memberModel.getId());
//                //  startActivity(intent);
//            }
//        });
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

        EditText search = (EditText) findViewById(R.id.search);
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

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById( R.id.bottom_navigation );

        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_category:
                        isCategory=true;
                        isProducts=false;
                        isCart=false;
                        isProfile=false;
                        txtheader.setText("Select Category");
                        //memberModelList=memberDao.selectall();
                        //setadaper(memberModelList,null);
                        break;
                    case R.id.action_product:
                        isCategory=false;
                        isProducts=true;
                        isCart=false;
                        isProfile=false;
                        txtheader.setText("Select Products");
                        //productModels=productDao.selectall(0);
                        //setadaper(null,productModels);
                        break;
                    case R.id.action_cart:
                        isCategory=false;
                        isProducts=false;
                        isCart=true;
                        isProfile=false;
                        txtheader.setText("Cart");
                        Intent openMainActivity = new Intent(ProductListActivity1.this, ActivityCart.class);
                        startActivity(openMainActivity);
                        finish();
                        //productModels=productDao.selectall(0);
                        //setadaper(null,productModels);
                        break;
                    case R.id.action_profile:
                        isCategory=false;
                        isProducts=false;
                        isCart=false;
                        isProfile=true;
                        txtheader.setText("Profile Details");
                        Intent intent1 = new Intent(ProductListActivity1.this, LoginActivity.class);
                        startActivity(intent1);
                        finish();
                        //productModels=productDao.selectall(0);
                       //refreshview();
                        break;

                }
                refreshview();
                return true;
            }
        } );


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
                if (memberModel.getName() != null && memberModel.getName().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(memberModel);
                }
            }
            filteredList = temp;
        }


        setadaper(filteredList);

    }

    @Override
    public void onCompleteCallBack(Object data) {
        // if (data instanceof MemberModel) {
            ProductModel productModel = (ProductModel) data;

        //Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        //Bundle bundle = new Bundle();
       // intent.putExtra("isFromApprove", true);
        //   intent.putExtra("memberId", memberModel.getId());
        //startActivity(intent);
        // }
        addToCartDialogue(productModel);
    }

    @Override
    public void onErrorCallBAck() {

    }

    private void detailsDialogue(final ProductModel productsModel){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout. add_to_cart, null);
        builder.setView(dialogView);


        final EditText qty,details;
        final TextView name,amount,uom;
        Button save;
        qty=(EditText)dialogView.findViewById(R.id.txtqty);

        name=(TextView) dialogView.findViewById(R.id.name);
        amount=(TextView) dialogView.findViewById(R.id.amount);
        save=(Button) dialogView.findViewById(R.id.save);


        qty.setText(String.valueOf(1));


        save.setVisibility(View.GONE);

        name.setText(productsModel.getName());
//        amount.setText("Price : "+String.valueOf(selectedPrice.getSalesPrice()));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (qty.getText().toString().isEmpty()||qty.getText().toString().equals("")){
                    qty.setError("Enter a Quantity");
                }else {
                    CustomerDiaryLineModel customerDiaryLineModel = new CustomerDiaryLineModel();
                    customerDiaryLineModel.setProduct_name(productsModel.getProduct_name());
                    String qtyValue = qty.getText().toString();
                    if (isEditOption){
                        customerDiaryLineModel.setId(lineId);
                    }
                    customerDiaryLineModel.setQty(CommonUtils.toInt(qtyValue));
                    customerDiaryLineModel.setDetails(details.getText().toString());
                    customerDiaryLineModel.setProduct_id(productsModel.getProduct_id());
                    customerDiaryLineModel.setHeaderId(diaryId);
                    customerDiaryLineModel.setCategory(productsModel.getProduct_category());
                    customerDiaryLineModel.setUomId(selectedUomModel.getUomId());
                    customerDiaryLineModel.setCategoryId(selectedCategory.getProductCategoryId());
                    customerDiaryLineModel.setPrice(selectedPrice.getSalesPrice().multiply(CommonUtils.toBigDecimal(qtyValue)));
                    saveLine(customerDiaryLineModel,isEditOption);
                    qty.getText().clear();
                    details.getText().clear();
                    lineRefresh();
                    initView();
                }

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }


    private void addToCartDialogue(ProductModel productModel) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_to_cart, null);
        builder.setView(dialogView);
        final TextView name = (TextView) dialogView.findViewById(R.id.txtProductName);
        final EditText qty = (EditText) dialogView.findViewById(R.id.txtqty);
        //final CheckBox isActive=(CheckBox)dialogView.findViewById(R.id.isActive);
        Button submit = (Button) dialogView.findViewById(R.id.add_button);

        name.setText(productModel.getName());


//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (!qty.getText().toString().isEmpty() && CommonUtils.toInt(qty.getText().toString()) > 0) {
//                    cartQty = cartQty + CommonUtils.toInt(qty.getText().toString());
//
//                    Toast.makeText(getApplicationContext(), "Product added to Cart", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//        });
        builder.show();
    }


}