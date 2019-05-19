package com.augmentedreality.simplus.util;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by denabelarde on 03/05/2018.
 */

public interface AlertDialogBinder {

    void showAlertDialog(Context context,
                         String message,
                         String positiveButton,
                         String negativeButton,
                         DialogInterface.OnClickListener positiveClickListener,
                         DialogInterface.OnClickListener negativeClickListener);

    void showAlertDialog(Context context,
                         String message,
                         String positiveButton,
                         boolean cancellable,
                         DialogInterface.OnClickListener positiveClickListener);

}
