package yu.dev.architecture.API;

/**
 * Created by yuhoshino on 2018/02/01.
 */

public class Repository {

    private String name;

    private String description;

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Repository [name=" + name + ", description=" + description + "]";
    }
}