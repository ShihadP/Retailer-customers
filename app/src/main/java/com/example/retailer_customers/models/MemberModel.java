package com.example.retailer_customers.models;



import com.example.retailer_customers.Recording;

import java.util.ArrayList;
import java.util.Date;

public class MemberModel {

    int id;
    int member_id;
    String name;
    String userName;
    String login_pin;
    Date birth_date;
    String district;
    int pin_code;
    String latitude;
    String address1;
    String address2;
    String loungitude;
    String email;
    String phone_no;
    int varified_memberId;
    String varified_memberName;
    String status;
    boolean isAgent;
    ArrayList<Recording> recordingList;
    String imagePath;
    String locationAddress;
    String deviceId;
    String audio;

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
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

    public int getVarified_memberId() {
        return varified_memberId;
    }

    public void setVarified_memberId(int varified_memberId) {
        this.varified_memberId = varified_memberId;
    }

    public String getVarified_memberName() {
        return varified_memberName;
    }

    public void setVarified_memberName(String varified_memberName) {
        this.varified_memberName = varified_memberName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public boolean isAgent() {
        return isAgent;
    }

    public void setAgent(boolean agent) {
        isAgent = agent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogin_pin() {
        return login_pin;
    }

    public void setLogin_pin(String login_pin) {
        this.login_pin = login_pin;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getPin_code() {
        return pin_code;
    }

    public void setPin_code(int pin_code) {
        this.pin_code = pin_code;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLoungitude() {
        return loungitude;
    }

    public void setLoungitude(String loungitude) {
        this.loungitude = loungitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public int getvarified_memberId() {
        return varified_memberId;
    }

    public void setvarified_memberId(int varified_memberId) {
        this.varified_memberId = varified_memberId;
    }
}
