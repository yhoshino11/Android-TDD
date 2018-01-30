package yu.dev.architecture;

import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yu.dev.architecture.Database.ApplicationDatabase;
import yu.dev.architecture.Database.User;

public class MainActivity extends AppCompatActivity {

    private ApplicationDatabase appDB;
    private TextView hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appDB = ApplicationDatabase.getInstance(getApplicationContext());
        hello = findViewById(R.id.hello);
    }

    @VisibleForTesting
    public void setDatabase(ApplicationDatabase db) {
        appDB = db;
    }

    public void insertAndFetchUser() {
        Completable
                .fromAction(() -> appDB.getUserDao().insertUser(new User("a")))
                .andThen(appDB.getUserDao().find("foobar"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> showUserName(user.getName()),
                        throwable -> showErrorMessage());
    }

    public void showErrorMessage() {
        hello.setText(getString(R.string.error));
    }

    public void showUserName(String name) {
        hello.setText(name);
    }
}
