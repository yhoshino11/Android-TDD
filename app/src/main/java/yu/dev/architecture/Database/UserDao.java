package yu.dev.architecture.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by yuhoshino on 2018/01/30.
 */

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Insert
    void insertUsers(User...users);

    @Query("SELECT * FROM users")
    Single<List<User>> getAll();

    @Query("SELECT * FROM users WHERE name = :name LIMIT 1")
    Single<User> find(String name);
}
