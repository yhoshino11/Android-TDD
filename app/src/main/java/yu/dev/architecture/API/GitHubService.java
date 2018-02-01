package yu.dev.architecture.API;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yuhoshino on 2018/02/01.
 */

public class GitHubService {

    private GitHubServiceAPI gitHubServiceAPI;

    GitHubService() {
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
