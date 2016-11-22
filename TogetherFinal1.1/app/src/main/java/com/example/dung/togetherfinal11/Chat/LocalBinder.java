package com.example.dung.togetherfinal11.Chat;

import android.os.Binder;

import java.lang.ref.WeakReference;

/*
 * Created by dung on 9/5/2016.
 */
public class LocalBinder<S> extends Binder {
    private final WeakReference<S> mService;

    public LocalBinder(final S service) {
        mService = new WeakReference<S>(service);
    }

    public S getService() {
        return mService.get();
    }
}
