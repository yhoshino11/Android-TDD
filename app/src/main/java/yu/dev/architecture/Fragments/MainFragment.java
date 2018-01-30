package yu.dev.architecture.Fragments;


import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import yu.dev.architecture.Database.ApplicationDatabase;
import yu.dev.architecture.Database.User;
import yu.dev.architecture.MainActivity;
import yu.dev.architecture.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    TextView hello;
    Button successBtn;
    Button errorBtn;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public MainFragment() {}

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hello = view.findViewById(R.id.hello);
        hello.setText(getString(R.string.main_fragment));

        successBtn = view.findViewById(R.id.success);
        successBtn.setOnClickListener(v -> fetchUser("foobar", "foobar"));

        errorBtn = view.findViewById(R.id.error);
        errorBtn.setOnClickListener(v -> fetchUser("a", "b"));
    }

    private void fetchUser(final String insertName, final String findName) {
        Single<User> userSingle = ((MainActivity)getActivity()).insertAndFetchUser(insertName, findName);
        disposable.add(userSingle
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                user -> showUserName(user.getName()),
                                throwable -> showErrorMessage())
        );
    }

    public void showErrorMessage() {
        Toast.makeText(getActivity(),getString(R.string.error), Toast.LENGTH_LONG).show();
        hello.setText(getString(R.string.error));
    }

    public void showUserName(String name) {
        Toast.makeText(getActivity(),name, Toast.LENGTH_LONG).show();
        hello.setText(name);
    }
}