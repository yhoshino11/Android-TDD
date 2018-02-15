package yu.dev.architecture.API;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yuhoshino on 2018/02/01.
 */

public class GitHubService {

    private GitHubServiceAPI gitHubServiceAPI;

    GitHubService() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(30, TimeUnit.SECONDS);
        client.readTimeout(30, TimeUnit.SECONDS);
        client.writeTimeout(30, TimeUnit.SECONDS);
        client.addInterceptor((chain) -> {
            Request request = chain.request().newBuilder().addHeader("", "").build();
            return chain.proceed(request);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        gitHubServiceAPI = retrofit.create(GitHubServiceAPI.class);
    }

    Observable<List<Repository>> listRepos(String userName) {
        return gitHubServiceAPI.listRepos(userName);
    }

    Observable<List<Contributor>> listContributors(String userName, String repoName) {
        return gitHubServiceAPI.listRepoContributors(userName, repoName);
    }
}
