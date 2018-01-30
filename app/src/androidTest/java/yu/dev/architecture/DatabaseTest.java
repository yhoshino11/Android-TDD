package yu.dev.architecture;

import android.arch.persistence.room.EmptyResultSetException;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;

import io.reactivex.Completable;

import yu.dev.architecture.Database.ApplicationDatabase;
import yu.dev.architecture.Database.User;

/**
 * Created by yuhoshino on 2018/01/30.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private Context context = InstrumentationRegistry.getTargetContext();
    private ApplicationDatabase db;

    @Before
    public void initDb() throws Exception {
        db = Room.inMemoryDatabaseBuilder(context,ApplicationDatabase.class)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertUsers() throws Exception {
        User[] users = new User[2];
        users[0] = new User("a");
        users[1] = new User("b");
        Completable
                .fromAction(() -> db.getUserDao().insertUsers(users))
                .test()
                .assertComplete();
    }

    @Test
    public void getAll() throws Exception {
        User[] users = new User[2];
        users[0] = new User("a");
        users[1] = new User("b");
        Completable
                .fromAction(() -> db.getUserDao().insertUsers(users))
                .andThen(db.getUserDao().getAll())
                .test()
                .assertValue(users1 -> users1.size() == 2);
    }

    @Test
    public void findWithInvalid() throws Exception {
        db.getUserDao().find("c").test().assertError(EmptyResultSetException.class);
    }

    @Test
    public void insertUser() throws Exception {
        User user = new User("a");
        Completable
                .fromAction(() -> db.getUserDao().insertUser(user))
                .test()
                .assertComplete();
    }

    @Test
    public void find() throws Exception {
        User[] users = new User[2];
        users[0] = new User("a");
        users[1] = new User("b");
        Completable
                .fromAction(() -> db.getUserDao().insertUsers(users))
                .andThen(db.getUserDao().find("a"))
                .test()
                .assertValue(user1 -> user1.getName().equals("a"));
    }
}