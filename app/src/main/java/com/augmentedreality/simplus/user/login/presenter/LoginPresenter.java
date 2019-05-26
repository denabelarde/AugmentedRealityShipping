package com.augmentedreality.simplus.user.login.presenter;

import android.app.Activity;

import com.augmentedreality.simplus.user.login.view.LoginView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;


/**
 * Created by denabelarde on 27/04/2018.
 */

public interface LoginPresenter extends MvpPresenter<LoginView> {

    void saveUserDataToDiskCache(boolean isLoggedIn,
                                 String email,
                                 String name,
                                 String photo,
                                 String idToken);

    void firebaseAuthWithGoogle(AuthCredential credential,
                                FirebaseAuth firebaseAuth,
                                Activity activity);
}