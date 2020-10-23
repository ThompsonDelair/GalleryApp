package com.example.photolab2.StructuralPatterns;

import java.util.Date;

public interface PhotoInfo {
    public String getFilePath();
    public String getCaption();
    public void setCaption(String newCaption);
    public long getDate();
    public float getLong();
    public float getLat();
}
