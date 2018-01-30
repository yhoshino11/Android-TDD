package yu.dev.architecture;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import yu.dev.architecture.Database.ApplicationDatabase;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by yuhoshino on 2018/01/30.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    private Context context = InstrumentationRegistry.getTargetContext();
    private ApplicationDatabase db;

    @Before
    public void initDB() throws Exception {
        db = Room.inMemoryDatabaseBuilder(context,ApplicationDatabase.class)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        main.getActivity().setDatabase(db);
    }

    @After
    public void closeDB() throws Exception {
        db.close();
    }

    @Test
    public void syncUser() throws Exception {
        main.getActivity().insertAndFetchUser();
//        onView(withText("foobar")).check(matches(isDisplayed()));
        onView(withText("Error")).check(matches(isDisplayed()));
    }
}