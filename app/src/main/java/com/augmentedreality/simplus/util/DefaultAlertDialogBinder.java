package com.augmentedreality.simplus.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by denabelarde on 03/05/2018.
 */

public class DefaultAlertDialogBinder implements AlertDialogBinder {

    @Inject
    public DefaultAlertDialogBinder() {
    }

    @Override
    public void showAlertDialog(Context context,
                                String message,
                                String positiveButton,
                                String negativeButton,
                                DialogInterface.OnClickListener positiveClickListener,
                                DialogInterface.OnClickListener negativeClickListener) {
        if (!TextUtils.toNonNull(message)
                      .isEmpty()) {
            try {
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    if (!activity.isFinishing()) {
                        new AlertDialog.Builder(context)
                            .setMessage(message)
                            .setPositiveButton(positiveButton, positiveClickListener)
                            .setNegativeButton(negativeButton, negativeClickListener)
                            .create()
                            .show();
                    }
                }
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }

    @Override
    public void showAlertDialog(Context context,
                                String message,
                                String positiveButton,
                                boolean cancellable,
                                DialogInterface.OnClickListener positiveClickListener) {
        if (!TextUtils.toNonNull(message)
                      .isEmpty()) {
            try {
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    if (!activity.isFinishing()) {
                        new AlertDialog.Builder(context)
                            .setMessage(message)
                            .setCancelable(cancellable)
                            .setPositiveButton(positiveButton, positiveClickListener)
                            .create()
                            .show();
                    }
                }
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }
}
