package yu.dev.architecture.UI;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.VisibleForTesting;

import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.internal.operators.flowable.FlowableCombineLatest;
import io.reactivex.schedulers.Schedulers;
import yu.dev.architecture.Database.ApplicationDatabase;
import yu.dev.architecture.Injection;
import yu.dev.architecture.R;
import yu.dev.architecture.UI.Fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    private final CompositeDisposable disposable = new CompositeDisposable();
    ViewModelFactory mViewModelFactory;
    UserViewModel mViewModel;
    ItemViewModel mItemViewModel;

    String latestName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModelFactory = Injection.provideViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(UserViewModel.class);
        mItemViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ItemViewModel.class);

        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            disposable.add(FlowableCombineLatest
                    .combineLatest(
                            mViewModel.getCount(),
                            mViewModel.latestName(),
                            Pair::new)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pair -> actionBar.setTitle(String.valueOf(pair.first) + " : " + pair.second)));
//            actionBar.hide();
        }
        if (savedInstanceState == null) {
            // Add Fragment
            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mainFragment).commit();
        }
    }

    private void setLatestName(String name) {
        this.latestName = name;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mItemViewModel.getTyping().observe(this, isTyping -> {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                if (isTyping) {
                    actionBar.setTitle("Typing...");
                } else {
                    actionBar.setTitle(this.latestName);
                }
            }
        });
        disposable.add(mViewModel.latestName()
                .map(name -> {
                    setLatestName(name);
                    return name;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userName -> Toast.makeText(this, userName, Toast.LENGTH_LONG).show()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.clear();
    }
}
