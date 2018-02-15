package yu.dev.architecture.UI;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import yu.dev.architecture.Database.LocalUserDataSource;
import yu.dev.architecture.Database.User;
import yu.dev.architecture.UI.UserViewModel;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by yuhoshino on 2018/02/01.
 */

@RunWith(AndroidJUnit4.class)
public class UserViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private LocalUserDataSource mDataSource;

    @Captor
    private ArgumentCaptor<User> mUserArgumentCaptor;

    private UserViewModel mViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mViewModel = new UserViewModel(mDataSource);
    }

    @Test
    public void completableErrorTest() throws InterruptedException {
        mViewModel.raiseError().test().assertErrorMessage("ERROR");
    }

    @Test
    public void latestInvalid() throws InterruptedException {
        when(mDataSource.latest()).thenReturn(Flowable.empty());
        mViewModel.latestName()
                .test()
                .assertNoValues();
    }

    @Test
    public void latest() throws InterruptedException {
        User user = new User("user name");
        when(mDataSource.latest()).thenReturn(Flowable.just(user));
        mViewModel.latestName()
                .test()
                .assertValue("user name");
    }

    @Test
    public void insertUserAndUpdateName() throws InterruptedException {
        mViewModel.insertUser("new user name")
                .test()
                .assertComplete();

        verify(mDataSource).insertUser(mUserArgumentCaptor.capture());
        assertThat(mUserArgumentCaptor.getValue().getName(), Matchers.is("new user name"));
    }

    @Test
    public void getCount() throws InterruptedException {
        List<User> userList = new ArrayList<>();
        userList.add(new User("a"));
        when(mDataSource.getUsers()).thenReturn(Flowable.just(userList));
        mViewModel.getCount()
                .test()
                .assertValue(integer -> integer == 1);
    }

    @Test
    public void getUser() throws InterruptedException {
        User user = new User("user name");
        when(mDataSource.getUser(user.getName())).thenReturn(Flowable.just(user));
        mViewModel.getUser(user.getName())
                .test()
                .assertValue(user1 -> user1.getName().equals(user.getName()));
    }
}
