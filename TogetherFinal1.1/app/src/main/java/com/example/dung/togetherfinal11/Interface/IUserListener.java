package com.example.dung.togetherfinal11.Interface;

import com.example.dung.togetherfinal11.Model.UserEntity;

import java.util.ArrayList;

/**
 * Created by dung on 17/11/2016.
 */

public interface IUserListener {
    void onSuccess(ArrayList<UserEntity> arrUser);
    void onError();
}
