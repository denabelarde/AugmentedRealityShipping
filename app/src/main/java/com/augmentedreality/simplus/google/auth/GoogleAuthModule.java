package com.augmentedreality.simplus.google.auth;

import android.content.Context;

import com.augmentedreality.simplus.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GoogleAuthModule {

    @Provides
    @Singleton
    static GoogleSignInOptions provideGoogleSignInOptions(Context context) {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions
                                                   .DEFAULT_SIGN_IN)
                   .requestIdToken(context.getString(R.string.default_web_client_id))
                   .requestEmail()
                   .build();
    }

    @Provides
    @Singleton
    static GoogleSignInClient provideGoogleSignInClient(Context context,
                                                        GoogleSignInOptions googleSignInOptions) {
        return GoogleSignIn.getClient(context, googleSignInOptions);
    }
}
