package com.example.dung.togetherfinal11.Interface;

import com.example.dung.togetherfinal11.Model.TeamEntity;
import com.example.dung.togetherfinal11.Model.UserEntity;

import java.util.ArrayList;

/**
 * Created by dung on 21/11/2016.
 */

public interface ITeamListener {
    void onSuccess(ArrayList<TeamEntity> arrTeam);
    void onError();
}
