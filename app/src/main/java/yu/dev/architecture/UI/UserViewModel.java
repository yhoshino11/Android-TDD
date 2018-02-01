package yu.dev.architecture.UI;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import yu.dev.architecture.Database.LocalUserDataSource;
import yu.dev.architecture.Database.User;

/**
 * Created by yuhoshino on 2018/01/31.
 */

public class UserViewModel extends ViewModel {

    private final LocalUserDataSource mUserDataSource;

    private User mUser;
    private int count;
    private MutableLiveData<Boolean> typing = new MutableLiveData<>();

    public UserViewModel(LocalUserDataSource source) {
        mUserDataSource = source;
        typing.setValue(false);
    }

    public void setTyping(boolean isTyping) {
        typing.setValue(isTyping);
    }

    public LiveData<Boolean> getTyping() {
        return typing;
    }

    public Flowable<User> getUser(String name) {
        return mUserDataSource.getUser(name);
    }

    public Flowable<String> latestName() {
        return mUserDataSource.latest().map(user -> {
            mUser = user;
            return user.getName();
        });
    }

    public Completable insertUser(String name) {
        return Completable.fromAction(() -> {
            mUser = new User(name);
            mUserDataSource.insertUser(mUser);
        });
    }

    public Flowable<Integer> getCount() {
        return mUserDataSource.getUsers().map(users -> {
            count = users.size();
            return users.size();
        });
    }
}
