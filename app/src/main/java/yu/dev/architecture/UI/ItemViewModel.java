package yu.dev.architecture.UI;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by yuhoshino on 2018/02/08.
 */

public class ItemViewModel extends ViewModel {
    private MutableLiveData<Boolean> typing = new MutableLiveData<>();
    public void setTyping(boolean isTyping) {
        typing.setValue(isTyping);
    }

    public LiveData<Boolean> getTyping() {
        return typing;
    }

}
