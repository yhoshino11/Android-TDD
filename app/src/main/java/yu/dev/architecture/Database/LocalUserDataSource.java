package yu.dev.architecture.Database;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by yuhoshino on 2018/01/31.
 */

public class LocalUserDataSource {

    private final UserDao mUserDao;

    public LocalUserDataSource(UserDao userDao) {
        mUserDao = userDao;
    }

    public Flowable<User> getUser(final String name) {
        return mUserDao.find(name);
    }

    public Flowable<User> latest() {
        return mUserDao.latest();
    }

    public Flowable<List<User>> getUsers() {
        return mUserDao.getAll();
    }

    public void insertUser(User user) {
        mUserDao.insertUser(user);
    }
}
