package yu.dev.architecture.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

/**
 * Created by yuhoshino on 2018/01/30.
 */

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Insert
    void insertUsers(User...users);

    @Query("SELECT * FROM users WHERE name = :name")
    User findByName(String name);

    @Query("SELECT * FROM users")
    User[] getAll();
}
