package com.example.photolab2;

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

    public void PrevButton(){
        // get prev photo from model
        view.DisplayPhoto();
    }

    public void NextButton(){
        // get next photo from model
        view.DisplayPhoto();
    }

    public void TakePhotoButton(){
        // ?
        view.DisplayPhoto();
    }

    public void PostPhotoButton(){

    }

}
