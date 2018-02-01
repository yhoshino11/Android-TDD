package yu.dev.architecture.Database;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;

import io.reactivex.Completable;

import yu.dev.architecture.Database.ApplicationDatabase;
import yu.dev.architecture.Database.User;
import yu.dev.architecture.Database.UserDao;

/**
 * Created by yuhoshino on 2018/01/30.
 */

@RunWith(AndroidJUnit4.class)
public class UserDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Context context = InstrumentationRegistry.getTargetContext();
    private ApplicationDatabase db;
    private UserDao userDao;

    @Before
    public void initDb() throws Exception {
        db = Room.inMemoryDatabaseBuilder(context,ApplicationDatabase.class)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        userDao = db.getUserDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void getAll() throws Exception {
        User[] users = new User[2];
        users[0] = new User("a.txt");
        users[1] = new User("b");
        userDao.insertUser(users[0]);
        userDao.insertUser(users[1]);
        userDao.getAll()
                .test()
                .assertNoErrors()
                .assertValue(users1 -> users1.size() == 2);
    }

    @Test
    public void findWithInvalid() throws Exception {
        userDao.find("a.txt").test().assertEmpty();
    }

    @Test
    public void insertUser() throws Exception {
        User user = new User("a.txt");
        Completable
                .fromAction(() -> userDao.insertUser(user))
                .test()
                .assertNoErrors()
                .assertComplete();
    }

    @Test
    public void find() throws Exception {
        User[] users = new User[2];
        users[0] = new User("a.txt");
        users[1] = new User("b");
        userDao.insertUser(users[0]);
        userDao.insertUser(users[1]);

        userDao.find("a.txt")
                .test()
                .assertNoErrors()
                .assertValue(user1 -> user1.getName().equals("a.txt"));
    }
}