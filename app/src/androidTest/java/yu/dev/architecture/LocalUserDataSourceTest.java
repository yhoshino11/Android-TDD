package yu.dev.architecture;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Flowable;
import yu.dev.architecture.Database.ApplicationDatabase;
import yu.dev.architecture.Database.LocalUserDataSource;
import yu.dev.architecture.Database.User;

import static org.mockito.Mockito.when;

/**
 * Created by yuhoshino on 2018/02/01.
 */

@RunWith(AndroidJUnit4.class)
public class LocalUserDataSourceTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private static final User USER = new User("username");

    private ApplicationDatabase mDatabase;
    private LocalUserDataSource mDataSource;

    @Before
    public void initDb() throws Exception {
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                ApplicationDatabase.class)
                .allowMainThreadQueries()
                .build();
        mDataSource = new LocalUserDataSource(mDatabase.getUserDao());
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

    @Test
    public void insertAndGetUser() {
        mDataSource.insertUser(USER);

        mDataSource.latest()
                .test()
                .assertValue(user ->  user != null && user.getName().equals(USER.getName() ));
    }

    @Test
    public void latest() {
        mDataSource.insertUser(USER);
        mDataSource.latest()
                .test()
                .assertValue(user -> user.getName().equals(USER.getName()));
    }

    @Test
    public void getUsers() {
        mDataSource.insertUser(USER);
        mDataSource.getUsers()
                .test()
                .assertValue(users -> users.size() == 1);
    }
}
