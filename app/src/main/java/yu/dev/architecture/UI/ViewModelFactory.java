package yu.dev.architecture.UI;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import yu.dev.architecture.Database.LocalUserDataSource;

/**
 * Created by yuhoshino on 2018/01/31.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final LocalUserDataSource mUserDataSource;

    public ViewModelFactory(LocalUserDataSource source) {
        mUserDataSource = source;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(mUserDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
