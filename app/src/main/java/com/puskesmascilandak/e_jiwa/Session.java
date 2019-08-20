package com.puskesmascilandak.e_jiwa;

import android.content.Context;
import android.content.SharedPreferences;

import com.puskesmascilandak.e_jiwa.model.User;
import com.puskesmascilandak.e_jiwa.service.UserDbService;

public class Session extends EJiwaPreference {

    public Session(Context context) {
        super(context);
    }

    public void setUsername(String username) {
        putString("username", username);
    }

    private String getUsername() {
        return getString("username");
    }

    public User getUser() {
        UserDbService service = new UserDbService(getContext());
        return service.findBy(getUsername());
    }


}
