package yu.dev.architecture.API;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yuhoshino on 2018/02/01.
 */

public class Contributor {

    @SerializedName("login")
    private String name;

    private Integer contributions;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getContributions() {
        return contributions;
    }
    public void setContributions(Integer contributions) {
        this.contributions = contributions;
    }

    @Override
    public String toString() {
        return "Contributer [name=" + name + ", contributions=" + contributions + "]";
    }
}