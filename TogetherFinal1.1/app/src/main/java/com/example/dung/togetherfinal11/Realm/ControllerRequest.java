package com.example.dung.togetherfinal11.Realm;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ControllerRequest extends Application {
    private RequestQueue requestQueue;
    public static final String TAG = ControllerRequest.class.getSimpleName();
    private static ControllerRequest controller;
    private ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        controller = this;
        Realm.init(controller);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(42)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    /**
     *
     * @return
     */

    public static ControllerRequest getInstance() {
        return controller;
    }

    /**
     *
     * @return trả về một đối tượng của RequestQueue sử dụng để gửi request
     */
   public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    /**
     *
     * @param request một request bất kì
     * @param tag được sử dụng setTag cho request
     * @param <T> tham số extends từ Object
     */
   public  <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    /**
     *
     * @param request
     * @param <T> tham số extends từ Object
     */
    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);

    }

    /**
     *
     * @param tag
     */
    public void cancelRequest(Objects tag){
        if (requestQueue!=null){
            requestQueue.cancelAll(tag);
        }
    }
//    public ImageLoader getImageLoader() {
//        getRequestQueue();
//        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(this.requestQueue,
//                    (ImageLoader.ImageCache) new LruBitmapCache());
//        }
//        return this.mImageLoader;
//    }
}
