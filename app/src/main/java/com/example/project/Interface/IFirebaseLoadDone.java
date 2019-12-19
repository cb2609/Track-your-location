package com.example.project.Interface;

import java.util.List;

public interface IFirebaseLoadDone {
    void onFirebaseLoadUserNameDone(List<String> lstemail);
    void onFirebaseLoadFailed(String message);

}
