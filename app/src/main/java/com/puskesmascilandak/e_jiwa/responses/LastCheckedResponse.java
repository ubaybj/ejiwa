
package com.puskesmascilandak.e_jiwa.responses;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastCheckedResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("last_checked")
    @Expose
    private List<LastChecked> lastChecked = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<LastChecked> getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(List<LastChecked> lastChecked) {
        this.lastChecked = lastChecked;
    }

}
