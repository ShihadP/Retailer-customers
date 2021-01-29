package com.example.retailer_customers;

public class Recording {
    String Uri, fileName;
    boolean isPlaying = false;
    int headerId;
    String tableName;

    public Recording() {

    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getHeaderId() {
        return headerId;
    }

    public void setHeaderId(int headerId) {
        this.headerId = headerId;
    }

    public Recording(String uri, String fileName, boolean isPlaying) {
        Uri = uri;
        this.fileName = fileName;
        this.isPlaying = isPlaying;
    }

    public String getUri() {
        return Uri;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing){
        this.isPlaying = playing;
    }
}
