package yu.dev.architecture.UI;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

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

    public UserViewModel(LocalUserDataSource source) {
        mUserDataSource = source;
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

    public Completable raiseError() {
        return Completable.fromAction(() -> {
            throw new Error("ERROR");
        });
    }

    public Flowable<Integer> getCount() {
        return mUserDataSource.getUsers().map(users -> users.size());
    }
}
