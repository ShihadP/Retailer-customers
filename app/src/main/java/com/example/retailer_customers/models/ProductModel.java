package com.example.retailer_customers.models;


import android.os.Parcelable;


import com.example.retailer_customers.Recording;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Stream;

import androidx.annotation.NonNull;


public class ProductModel  {
    int id;
    int product_id;
    String name;
    String category;
    int group_code;
    BigDecimal mrp;
    BigDecimal sp;
    String description;
    String hsn;
    BigDecimal tax;
    int ptc;
    String image;
    String voice_note;
    int status;
    String imagePath;
    int qty;
    BigDecimal total;

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    ArrayList<Recording> recordingList;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ArrayList<Recording> getRecordingList() {
        return recordingList;
    }

    public void setRecordingList(ArrayList<Recording> recordingList) {
        this.recordingList = recordingList;
    }

    public int getGroup_code() {
        return group_code;
    }

    public void setGroup_code(int group_code) {
        this.group_code = group_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMrp() {
        return mrp;
    }

    public void setMrp(BigDecimal mrp) {
        this.mrp = mrp;
    }

    public BigDecimal getSp() {
        return sp;
    }

    public void setSp(BigDecimal sp) {
        this.sp = sp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public int getPtc() {
        return ptc;
    }

    public void setPtc(int ptc) {
        this.ptc = ptc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVoice_note() {
        return voice_note;
    }

    public void setVoice_note(String voice_note) {
        this.voice_note = voice_note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
