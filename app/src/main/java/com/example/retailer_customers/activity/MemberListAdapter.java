package com.example.retailer_customers.activity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.retailer_customers.OnCompleteCallBack;
import com.example.retailer_customers.R;
import com.example.retailer_customers.models.MemberModel;
import com.example.retailer_customers.models.ProductModel;
import com.example.retailer_customers.utils.CommonUtils;

import java.io.File;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MyViewHolder> {

    private List<MemberModel> memberModelList;
    private List<ProductModel> productModels;
    OnCompleteCallBack onCompleteCallBack;
    boolean isProductList=false;


    public MemberListAdapter(List<MemberModel> memberModelList,OnCompleteCallBack completeCallBack) {
        this.onCompleteCallBack = completeCallBack;
        this.memberModelList = memberModelList;
    }

    public MemberListAdapter(List<ProductModel> productModels,OnCompleteCallBack completeCallBack,boolean isProductList) {
        this.onCompleteCallBack = completeCallBack;
        this.productModels = productModels;
        this.isProductList=isProductList;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView customerName, phoneNo, textDate, address,district,dateofbirth,txtlocation;
        public Button btnpick;
        LinearLayout statusView;
        ImageView imageView;
        ImageButton imageButton;


        public MyViewHolder(View view) {
            super( view );
            //textDate = (TextView) view.findViewById( R.id.dateofbirth );
            customerName = (TextView) view.findViewById( R.id.memberName );
            phoneNo = (TextView) view.findViewById( R.id.phoneNo );
            address = (TextView) view.findViewById( R.id.address );
            //district = (TextView) view.findViewById( R.id.district );
            //dateofbirth = (TextView) view.findViewById( R.id.dateofbirth );
            btnpick = (Button) view.findViewById( R.id.add_button );
            //statusView=(LinearLayout)view.findViewById(R.id.status_indication);
            txtlocation = (TextView) view.findViewById( R.id.txtlocation );
            imageView = (ImageView) view.findViewById( R.id.iamge);
            imageButton = (ImageButton) view.findViewById( R.id.mapicon);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.member_item_list, parent, false );
        return new MyViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (isProductList) {
            final ProductModel eachItem = productModels.get(position);

             holder.imageButton.setVisibility(View.GONE);
            holder.btnpick.setBackgroundResource(R.drawable.approve_button);
            holder.btnpick.setTextColor(Color.WHITE);
            if (eachItem.getStatus()==1){

                holder.btnpick.setText("APPROVED");
            }else {
                holder.btnpick.setText("APPROVE");
            }



            holder.address.setText(eachItem.getDescription());
            holder.customerName.setText(eachItem.getName());
            holder.phoneNo.setText("Price: "+eachItem.getSp().toString());
            //holder.district.setText(eachItem.getDistrict());
            holder.txtlocation.setText(eachItem.getPtc()>0?"Product Code: "+ CommonUtils.toString(eachItem.getPtc()):"");

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

            holder.btnpick.setVisibility(View.VISIBLE);


            if (eachItem.getStatus()==1){

                holder.btnpick.setText("APPROVED");
            }else {
                holder.btnpick.setText("APPROVE");
            }

            holder.btnpick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        } else {
            final MemberModel eachItem = memberModelList.get(position);
            holder.imageButton.setVisibility(View.VISIBLE);
            if (eachItem.getStatus().equalsIgnoreCase("1")){

                holder.btnpick.setText("APPROVED");
            }else {
                holder.btnpick.setText("APPROVE");
            }
            holder.btnpick.setBackgroundResource(R.drawable.approve_button);
            holder.btnpick.setTextColor(Color.WHITE);


            holder.address.setText(eachItem.getAddress1());
            holder.customerName.setText(eachItem.getName());
            holder.phoneNo.setText(eachItem.getPhone_no());
            //holder.district.setText(eachItem.getDistrict());
            holder.txtlocation.setText(eachItem.getLocationAddress());

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

            holder.btnpick.setVisibility(View.VISIBLE);


            holder.btnpick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCompleteCallBack.onCompleteCallBack(eachItem);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (isProductList) {
            return productModels.size();
        } else {
            return memberModelList.size();
        }
    }





    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView( recyclerView );
    }

}
