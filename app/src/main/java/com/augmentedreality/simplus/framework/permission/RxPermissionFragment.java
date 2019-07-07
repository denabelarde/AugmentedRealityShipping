package com.augmentedreality.simplus.framework.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;


import static androidx.core.content.ContextCompat.checkSelfPermission;


public class RxPermissionFragment extends Fragment implements RxPermission {

    public static final String TAG = RxPermissionFragment.class.getSimpleName();

    private static final int PERMISSION_REQUEST_CODES = 100;

    private Map<String, PublishSubject<Permission>> subjectMap;

    private AppCompatActivity activity;


    public static RxPermissionFragment newInstance() {
        return new RxPermissionFragment();
    }

    public RxPermissionFragment() {
        this.subjectMap = new HashMap<>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != PERMISSION_REQUEST_CODES) return;

        for (int i = 0; i < permissions.length; i++) {
            PublishSubject<Permission> subject = subjectMap.get(permissions[i]);
            subjectMap.remove(permissions[i]);
            boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            subject.onNext(new Permission(permissions[i], granted));
            subject.onComplete();
        }
    }

    @Override
    public Observable<Boolean> request(String... permissions) {
        return createRequest(permissions);
    }

    @Override
    public Observable<Permission> requestEach(final String... permissions) {
        return createRequestEach(permissions);
    }

    private Observable<Boolean> createRequest(String[] permissions) {
        final List<Permission> permissionList = toPermissionList(permissions);
        return createPermissionRequest(permissionList)
                   .toList()
                   .toObservable()
                   .map(
                       permissionList1 -> {
                           for (Permission permission : permissionList1) {
                               if (!permission.isGranted())
                                   return false;
                           }
                           return true;
                       });
    }

    private Observable<Permission> createRequestEach(String[] permissions) {
        final List<Permission> permissionList = toPermissionList(permissions);
        return createPermissionRequest(permissionList);
    }

    private Observable<Permission> createPermissionRequest(final List<Permission> permissionList) {
        final List<Observable<Permission>> list = new ArrayList<>(permissionList.size());
        final List<String> unrequestedPermissions = new ArrayList<>();
        for (Permission permission : permissionList) {
            PublishSubject<Permission> subject = subjectMap.get(permission.getName());
            if (subject == null) {
                subject = PublishSubject.create();
                subjectMap.put(permission.getName(), subject);
                unrequestedPermissions.add(permission.getName());
            }
            list.add(subject);
        }

        if (unrequestedPermissions.size() > 0)
            requestPermissions(unrequestedPermissions.toArray(new String[unrequestedPermissions
                                                                             .size()]),
                               PERMISSION_REQUEST_CODES);

        return Observable.concat(Observable.fromIterable(list));
    }

    private List<Permission> toPermissionList(String[] permissions) {
        final List<Permission> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                permissionList.add(new Permission(permission, false));
            } else {
                permissionList.add(new Permission(permission, true));
            }
        }
        return permissionList;
    }
}
