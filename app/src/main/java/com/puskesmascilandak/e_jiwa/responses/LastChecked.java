
package com.puskesmascilandak.e_jiwa.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastChecked {

    @SerializedName("last_checked")
    @Expose
    private String lastChecked;

    public String getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(String lastChecked) {
        this.lastChecked = lastChecked;
    }

}
