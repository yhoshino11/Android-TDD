package yu.dev.architecture.UI;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import yu.dev.architecture.Database.ApplicationDatabase;
import yu.dev.architecture.R;
import yu.dev.architecture.UI.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by yuhoshino on 2018/01/30.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    private Context context = InstrumentationRegistry.getTargetContext();
    ApplicationDatabase db;

    @Before
    public void initDB() throws Exception {
        main.getActivity().getSupportFragmentManager().beginTransaction();
        db = Room.inMemoryDatabaseBuilder(context,ApplicationDatabase.class)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDB() throws Exception {
        db.close();
    }

    @Test
    public void showMainFragment() throws Exception {
        onView(ViewMatchers.withText(R.string.main_fragment)).check(matches(isDisplayed()));
    }
}