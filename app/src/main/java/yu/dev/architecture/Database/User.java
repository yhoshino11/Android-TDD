package yu.dev.architecture.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by yuhoshino on 2018/01/30.
 */
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
