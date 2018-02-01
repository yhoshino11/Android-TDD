package yu.dev.architecture;

import android.content.Context;

import yu.dev.architecture.Database.ApplicationDatabase;
import yu.dev.architecture.Database.LocalUserDataSource;
import yu.dev.architecture.UI.ViewModelFactory;

/**
 * Created by yuhoshino on 2018/01/31.
 */

public class Injection {

    private static LocalUserDataSource provideUserDataSource(Context context) {
        ApplicationDatabase db = ApplicationDatabase.getInstance(context);
        return new LocalUserDataSource(db.getUserDao());
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        LocalUserDataSource dataSource = provideUserDataSource(context);
        return new ViewModelFactory(dataSource);
    }
}