package com.augmentedreality.simplus.framework.permission;

import io.reactivex.Observable;

public interface RxPermission {

    Observable<Boolean> request(String... permissions);

    Observable<Permission> requestEach(String... permissions);
}
