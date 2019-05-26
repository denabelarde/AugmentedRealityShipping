package com.augmentedreality.simplus.user.login.view;

import com.augmentedreality.simplus.user.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface LoginView extends MvpView {

    void updateUI(FirebaseUser firebaseUser);

    void createUserInFirebaseHelper();

    void updateUserInFirebase(User user);

    void showProgressDialog();

    void hideProgressDialog();

    void showErrorAuthenticating();

    void redirectToDashboard();

}
