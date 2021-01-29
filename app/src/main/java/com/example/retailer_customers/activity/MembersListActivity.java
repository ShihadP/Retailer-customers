package com.example.retailer_customers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;


import com.example.retailer_customers.OnCompleteCallBack;
import com.example.retailer_customers.R;
import com.example.retailer_customers.models.MemberModel;
import com.example.retailer_customers.models.ProductModel;
import com.example.retailer_customers.utils.ItemDecorator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MembersListActivity extends AppCompatActivity implements OnCompleteCallBack {
      RecyclerView recyclerView;
      //MemberDao memberDao;
      //ProductDao productDao;
      MemberListAdapter memberListAdapter;
    List<MemberModel> memberModelList;
    List<ProductModel> productModels;
    EditText search;
    boolean isCustomerList=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_list);
        initui();
        initDb();
        //memberModelList=memberDao.selectall();
        setadaper(memberModelList,null);
    }







    public void search(String text) {
        List<MemberModel> filteredList;
        if (memberModelList == null || memberModelList.isEmpty()) {
            return;
        }

        if (text == null || text.isEmpty()) {
            filteredList = memberModelList;
        } else {
            List<MemberModel> temp = new ArrayList<>();
            for (MemberModel memberModel : memberModelList) {
                if (memberModel.getPhone_no() != null && memberModel.getPhone_no().toLowerCase().contains( text.toLowerCase() ) ||
                        memberModel.getName() != null && memberModel.getName().toLowerCase().contains( text.toLowerCase() )) {
                    temp.add( memberModel );
                }
            }
            filteredList = temp;
        }


        setadaper(filteredList,null);

    }
    public void searchProducts(String text) {
        List<ProductModel> filteredList;
        if (productModels == null || productModels.isEmpty()) {
            return;
        }

        if (text == null || text.isEmpty()) {
            filteredList = productModels;
        } else {
            List<ProductModel> temp = new ArrayList<>();
            for (ProductModel memberModel : productModels) {
                if (memberModel.getName() != null && memberModel.getName().toLowerCase().contains( text.toLowerCase() ) ) {
                    temp.add( memberModel );
                }
            }
            filteredList = temp;
        }


        setadaper(null,filteredList);

    }


    public void setadaper( List<MemberModel> memberModelList, List<ProductModel> productModels){
        if (isCustomerList){
            memberListAdapter = new MemberListAdapter(memberModelList, this);
        }else {
            memberListAdapter = new MemberListAdapter(productModels, this,true);
        }
        recyclerView.setAdapter(memberListAdapter);
        memberListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
           if (isCustomerList) {
               //memberModelList=memberDao.selectall();
               setadaper(memberModelList,null);
           }else {
               //productModels=productDao.selectall(0);
               setadaper(null,productModels);
           }

        super.onResume();
    }

    private void initui() {

        search=(EditText)findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (isCustomerList) {
                   search(s.toString());
               }else {
                   searchProducts(s.toString());
               }

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
                    case R.id.action_profile:
                        isCustomerList=true;
                        //memberModelList=memberDao.selectall();
                        setadaper(memberModelList,null);
                        break;
                    case R.id.action_product:
                        isCustomerList=false;
                        //productModels=productDao.selectall(0);
                        setadaper(null,productModels);
                        break;

                }
                return true;
            }
        } );


        recyclerView=(RecyclerView)findViewById( R.id.recyclerView );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemDecorator(getApplicationContext()));

    }

    private void initDb() {
        //memberDao=new MemberDao( getApplicationContext() );
        //productDao=new ProductDao( getApplicationContext() );
    }

    @Override
    public void onCompleteCallBack(Object data) {
        if (data instanceof MemberModel) {
            MemberModel memberModel = (MemberModel) data;

            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtra("isFromApprove", true);
            intent.putExtra("memberId", memberModel.getId());
            startActivity(intent);
        }else {
            ProductModel memberModel = (ProductModel) data;

//            Intent intent = new Intent(getApplicationContext(), CreateNewProductActivity.class);
//            Bundle bundle = new Bundle();
//            intent.putExtra("isFromApprove", true);
//            intent.putParcelableArrayListExtra("df",memberModel);
//            intent.putExtra("productId", memberModel.getId());
//            startActivity(intent);
        }

    }

    @Override
    public void onErrorCallBAck() {

    }

//    private void addRollDialogue(){
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout. approve_dialogue, null);
//        builder.setView(dialogView);
//        final EditText rollName=(EditText)dialogView.findViewById(R.id.roll_name);
//        final EditText description=(EditText)dialogView.findViewById(R.id.description);
//        final CheckBox isActive=(CheckBox)dialogView.findViewById(R.id.isActive);
//        Button submit=(Button)dialogView.findViewById(R.id.add_button);
//
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (rollName.getText().toString().isEmpty()||rollName.getText().toString().equals("")){
//                    rollName.setError("please enter a roll name");
//                }else if (description.getText().toString().equals("")||description.getText().toString().isEmpty()){
//                    description.setError("please enter a description");
//                }else {
//                    roleModel.setRoleName(rollName.getText().toString());
//                    roleModel.setDescription(description.getText().toString());
//                    roleModel.setActive(isActive.isChecked());
//                    roleModel.setPriority(1);
//                    synRoll(roleModel);
//                    Toast.makeText(getApplicationContext(),"Roll added successfully",Toast.LENGTH_SHORT).show();
//                    rollName.getText().clear();
//                    description.getText().clear();
//                    getRoll();
//                }
//
//            }
//        });
//        builder.show();
//    }



}
