package yu.dev.architecture.UI.Fragments;

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
import yu.dev.architecture.UI.MainActivity;
import yu.dev.architecture.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by yuhoshino on 2018/01/31.
 */
@RunWith(AndroidJUnit4.class)
public class MainFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);
    private Context context = InstrumentationRegistry.getTargetContext();
    private ApplicationDatabase db;

    @Before
    public void initDB() throws Exception {
        main.getActivity().getSupportFragmentManager().beginTransaction();
        db = Room.inMemoryDatabaseBuilder(context,ApplicationDatabase.class)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
//        main.getActivity().setDatabase(db);
    }

    @After
    public void closeDB() throws Exception {
        db.close();
    }

    @Test
    public void showMainFragment() throws Exception {
        onView(withText(R.string.main_fragment)).check(matches(isDisplayed()));
    }
    @Test
    public void showSuccess() throws Exception {
        onView(withId(R.id.success)).perform(click());
        onView(withText(R.string.dummyName)).check(matches(isDisplayed()));
    }

    @Test
    public void showError() throws Exception {
//        onView(withId(R.id.error)).perform(click());
        onView(withText(R.string.error)).check(matches(isDisplayed()));
    }
}