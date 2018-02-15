package yu.dev.architecture.UI.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import yu.dev.architecture.Injection;
import yu.dev.architecture.R;
import yu.dev.architecture.UI.ItemViewModel;
import yu.dev.architecture.UI.UserViewModel;
import yu.dev.architecture.UI.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    TextView hello;

    Button successBtn;
    EditText userNameInput;

    private final CompositeDisposable disposable = new CompositeDisposable();
    ViewModelFactory mViewModelFactory;
    UserViewModel mViewModel;
    ItemViewModel mItemViewModel;

    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModelFactory = Injection.provideViewModelFactory(getActivity());
        mViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(UserViewModel.class);
        mItemViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(ItemViewModel.class);
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
        disposable.add(mViewModel.getCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> showMessage(String.valueOf(count), false)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hello = view.findViewById(R.id.hello);
        hello.setText(getString(R.string.main_fragment));

        successBtn = view.findViewById(R.id.success);
        successBtn.setOnClickListener(v -> insertUser("first"));
        userNameInput = view.findViewById(R.id.userNameInput);

        Observable<CharSequence> textState = RxTextView.textChanges(userNameInput);
        textState
                .map(inputText -> inputText.length() > 0)
                .subscribe(
                        hasText -> mItemViewModel.setTyping(hasText),
                        throwable -> {},
                        () -> {});

        userNameInput.setCursorVisible(false);

        userNameInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String newValue = String.valueOf(v.getText());
                if (newValue.length() == 0) return true;
                disposable.add(mViewModel
                        .insertUser(newValue)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> v.setText("")));
                return true;
            }
            return false;
        });
    }

    private void insertUser(final String name) {
        successBtn.setEnabled(false);

        disposable.add(mViewModel.insertUser(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> successBtn.setEnabled(true),
                        throwable -> showMessage(throwable.getMessage(), false)));
    }

    public void showMessage(String msg, boolean toast) {
        if (toast) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        } else {
            hello.setText(msg);
        }
    }
}