package yu.dev.architecture;

import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.reactivex.Completable;
import io.reactivex.Single;

import io.reactivex.disposables.CompositeDisposable;

import yu.dev.architecture.Database.ApplicationDatabase;
import yu.dev.architecture.Database.User;
import yu.dev.architecture.Fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    private final CompositeDisposable disposable = new CompositeDisposable();
    private ApplicationDatabase appDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // Add Fragment
            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mainFragment).commit();
        }

        appDB = ApplicationDatabase.getInstance(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.clear();
    }

    @VisibleForTesting
    public void setDatabase(ApplicationDatabase db) {
        appDB = db;
    }

    public Single<User> insertAndFetchUser(final String insertName, final String findName) {
        return Completable
                .fromAction(() -> appDB.getUserDao().insertUser(new User(insertName)))
                .andThen(appDB.getUserDao().find(findName));
    }
}
