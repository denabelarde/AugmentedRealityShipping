package com.augmentedreality.simplus.user.login.view;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.augmentedreality.simplus.Constants;
import com.augmentedreality.simplus.R;
import com.augmentedreality.simplus.dashboard.view.DashboardActivity;
import com.augmentedreality.simplus.user.login.presenter.LoginPresenter;
import com.augmentedreality.simplus.user.model.User;
import com.augmentedreality.simplus.user.model.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import timber.log.Timber;

public class LoginActivity extends MvpActivity<LoginView, LoginPresenter>
    implements LoginView {

    private static final int RC_SIGN_IN = 9001;

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    LoginPresenter presenter;

    @Inject
    GoogleSignInClient mGoogleSignInClient;


    @BindView(R.id.google_signin_button)
    ImageButton googleSigninButton;

    private String idToken, name, email, photo;

    private Uri photoUri;

    private DatabaseReference databaseUsers;

    private Unbinder unbinder;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return presenter;
    }


    @OnClick(R.id.google_signin_button)
    public void onClickSignIn() {
        signIn();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                idToken = account.getIdToken();
                name = account.getDisplayName();
                email = account.getEmail();
                photoUri = account.getPhotoUrl();
                photo = "";
                // Save Data to DiskCache
                presenter.saveUserDataToDiskCache(true, email, name, photo, idToken);

                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Timber.e(e, "Google sign in failed");
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    @Override
    public void redirectToDashboard() {
        startActivity(new Intent(this, DashboardActivity.class)
                          .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    @Override
    public void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            findViewById(R.id.google_signin_button).setVisibility(View.GONE);
        } else {
            findViewById(R.id.google_signin_button).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void createUserInFirebaseHelper() {
        final String encodedEmail = Utils.encodeEmail(email.toLowerCase());
        databaseUsers = FirebaseDatabase.getInstance()
                                        .getReference("users");
        HashMap<String, Object> timestampJoined = new HashMap<>();
        timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

        String id = firebaseAuth.getCurrentUser()
                                .getUid();
        User newUser = new User(id, name, photo, encodedEmail);
        databaseUsers.child(id)
                     .setValue(newUser);
    }

    @Override
    public void updateUserInFirebase(User user) {
        HashMap<String, Object> timestampJoined = new HashMap<>();
        timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

        user.setFullName(name);
        user.setPhoto(photo);
        databaseUsers = FirebaseDatabase.getInstance()
                                        .getReference("users");
        databaseUsers.child(user.getFbUserId())
                     .setValue(user);
    }


    @Override
    public void showErrorAuthenticating() {
        Toast.makeText(LoginActivity.this, getString(R.string.authentication_failed),
                       Toast.LENGTH_SHORT)
             .show();
        signOut();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Timber.d("firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(),
                                                                         null);
        presenter.firebaseAuthWithGoogle(authCredential,
                                         firebaseAuth,
                                         this);
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        firebaseAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut()
                           .addOnCompleteListener(this,
                                                  task -> updateUI(null));
    }

    @Override
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
