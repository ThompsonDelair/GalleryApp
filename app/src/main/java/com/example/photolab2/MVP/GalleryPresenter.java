package com.example.photolab2.MVP;

import java.util.Date;

public class GalleryPresenter {

    IGalleryView view;
    Model model;

    public GalleryPresenter(IGalleryView v,Model m){
        view = v;
        model = m;
    }

    void onDestroy(){
        view = null;
    }

    public void DisplayCurrPhoto(){
        view.DisplayPhoto(model.GetCurrPhoto());
    }

//    public void DisplayNullPhoto(){
//        view.DisplayPhoto(null);
//    }

    public void PrevButton(){
        view.DisplayPhoto(model.PrevPhoto());
    }

    public void NextButton(){
        view.DisplayPhoto(model.NextPhoto());
    }

//    public void TakePhotoButton(){
        // ?
        //view.DisplayPhoto();
//    }

    public void RefreshAndDisplay(){
        model.LoadPhotos();
        view.DisplayPhoto(model.GetCurrPhoto());
    }

    public void Search(Date startTimestamp, Date endTimestamp, String keywords, float laty, float longy){
        model.Search(startTimestamp,endTimestamp,keywords,laty,longy);
        view.DisplayPhoto(model.GetCurrPhoto());
    }

    public void UpdateCurrPhoto(String s){
        model.UpdateCurrPhoto(s);
    }
}
