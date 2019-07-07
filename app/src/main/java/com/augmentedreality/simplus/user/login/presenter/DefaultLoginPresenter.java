package com.augmentedreality.simplus.user.login.presenter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.augmentedreality.simplus.Constants;
import com.augmentedreality.simplus.user.login.view.LoginView;
import com.augmentedreality.simplus.user.model.User;
import com.augmentedreality.simplus.util.cache.DiskCache;
import com.augmentedreality.simplus.util.rx.RxSchedulerUtils;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;


import static com.augmentedreality.simplus.Constants.DiskCacheKey.EMAIL;
import static com.augmentedreality.simplus.Constants.DiskCacheKey.ID_TOKEN;
import static com.augmentedreality.simplus.Constants.DiskCacheKey.IS_LOGGED_IN;
import static com.augmentedreality.simplus.Constants.DiskCacheKey.NAME;
import static com.augmentedreality.simplus.Constants.DiskCacheKey.PHOTO;


public class DefaultLoginPresenter extends MvpBasePresenter<LoginView> implements LoginPresenter {

    private static final String TAG = "LoginPresenter";

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;

    private RxSchedulerUtils rxSchedulerUtils;

    private DiskCache diskCache;

    private LoginView view;

    private Disposable disposable;

    private Intent intentToRedirect;

    @Inject
    public DefaultLoginPresenter(FirebaseAuth firebaseAuth,
                                 RxSchedulerUtils rxSchedulerUtils,
                                 DiskCache diskCache) {
        this.firebaseAuth = firebaseAuth;
        this.diskCache = diskCache;
        this.rxSchedulerUtils = rxSchedulerUtils;
        this.firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void attachView(LoginView view) {
        super.attachView(view);
        this.view = view;
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        dispose();
    }

    @Override
    public void saveUserDataToDiskCache(boolean isLoggedIn,
                                        String email,
                                        String name,
                                        String photo,
                                        String idToken) {
        diskCache.saveBoolean(IS_LOGGED_IN, isLoggedIn)
                 .doOnError(
                     error ->
                         Timber.w(error,
                                  "Failed saving isLoggedIn isLoggedIn to disk cache"))
                 .onErrorComplete()
                 .subscribe();

        diskCache.saveString(EMAIL, email)
                 .doOnError(
                     error ->
                         Timber.w(error, "Failed saving Firebase email to disk cache"))
                 .onErrorComplete()
                 .subscribe();

        diskCache.saveString(NAME, name)
                 .doOnError(
                     error ->
                         Timber.w(error, "Failed saving Firebase name to disk cache"))
                 .onErrorComplete()
                 .subscribe();

        diskCache.saveString(PHOTO, photo)
                 .doOnError(
                     error ->
                         Timber.w(error, "Failed saving Firebase photo to disk cache"))
                 .onErrorComplete()
                 .subscribe();

        diskCache.saveString(ID_TOKEN, idToken)
                 .doOnError(
                     error ->
                         Timber.w(error, "Failed saving Firebase idToken to disk cache"))
                 .onErrorComplete()
                 .subscribe();
    }


    @Override
    public void firebaseAuthWithGoogle(AuthCredential credential,
                                       FirebaseAuth firebaseAuth,
                                       Activity activity) {
        view.showProgressDialog();
        firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(activity, task -> {
                        if (task.isSuccessful()) {
                            getFirebaseUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            view.showErrorAuthenticating();
                            view.hideProgressDialog();
                        }
                    });
    }



    private void getFirebaseUser() {
        String userId = firebaseAuth.getCurrentUser()
                                    .getUid();
        Timber.d(userId + " <<< firebaseUserId");
        firebaseDatabase
            .getReference(Constants.FirebaseReference.USERS)
            .child(userId)
            .addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            view.createUserInFirebaseHelper();
                            view.hideProgressDialog();
                        } else {
                            User user = dataSnapshot.getValue(User.class);
                            user.setFbUserId(dataSnapshot.getKey());
                            view.updateUserInFirebase(user);
                            view.hideProgressDialog();
                        }
                        view.redirectToDashboard();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // TODO: Add error message
                    }
                });
    }


    void dispose() {
        boolean canDispose = disposable != null && !disposable.isDisposed();
        if (canDispose) disposable.dispose();
    }
}
