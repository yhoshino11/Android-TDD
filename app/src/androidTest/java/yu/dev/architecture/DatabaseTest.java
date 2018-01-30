package yu.dev.architecture;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import java.io.IOException;

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
    public void findByName() throws Exception {
        User user = new User("a");
        db.getUserDao().insertUser(user);
        User result = db.getUserDao().findByName("a");
        assertEquals("a", result.getName());
    }

    @Test
    public void insertUsers() throws Exception {
        User[] users = new User[2];
        users[0] = new User("a");
        users[1] = new User("b");
        db.getUserDao().insertUsers(users);
        User[] userList = db.getUserDao().getAll();
        assertEquals(2, userList.length);
        assertEquals("a", userList[0].getName());
    }
}