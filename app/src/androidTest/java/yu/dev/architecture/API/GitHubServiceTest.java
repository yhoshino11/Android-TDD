package yu.dev.architecture.API;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.HttpException;

/**
 * Created by yuhoshino on 2018/02/01.
 */
@RunWith(AndroidJUnit4.class)
public class GitHubServiceTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private static final GitHubService gitHubService = new GitHubService();
    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(final RecordedRequest request) throws InterruptedException {
                if (request == null || request.getPath() == null) {
                    return new MockResponse().setResponseCode(400);
                }
                switch (request.getPath()) {
                    case "users/yhoshino11/repos":
                        return new MockResponse()
                                .addHeader("Content-Type", "application/json; charset=utf-8")
                                .addHeader("Cache-Control", "no-cache")
                                .setResponseCode(200)
                                .setBody(responseBodyFromFile("repo.json"));
                    case "repos/yhoshino11/ArchLinux/contributors":
                        return new MockResponse()
                                .addHeader("Content-Type", "application/json; charset=utf-8")
                                .addHeader("Cache-Control", "no-cache")
                                .setResponseCode(200)
                                .setBody(responseBodyFromFile("contributor.json"));
                    default:
                        return new MockResponse()
                                .addHeader("Content-Type", "application/json; charset=utf-8")
                                .addHeader("Cache-Control", "no-cache")
                                .setResponseCode(404);
                }
            }
        };
        server = new MockWebServer();
        server.setDispatcher(dispatcher);
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void repos() throws Exception {
        gitHubService.listRepos("yhoshino11")
                .map(repositories -> repositories.get(6))
                .map(Repository::getName)
                .test()
                .assertValue(s -> s.equals("ArchLinux"));
    }

    @Test
    public void getContributors() throws Exception {
        gitHubService.listRepos("yhoshino11")
                .map(repositories -> repositories.get(6))
                .map(Repository::getName)
                .flatMap(name -> gitHubService.listContributors("yhoshino11", name))
                .test()
                .assertValue(contributors -> contributors.size() == 2);
    }

    @Test
    public void getRepoDescription() throws Exception {
        gitHubService.listRepos("yhoshino11")
                .map(repositories -> repositories.get(6))
                .map(Repository::getDescription)
                .test()
                .assertValue(description -> description.equals("my minimum Arch Linux Installution"));
    }

    @Test
    public void forbidden() throws Exception {
        gitHubService.listRepos("")
                .test()
                .assertError(HttpException.class);
        gitHubService.listRepos("")
                .test()
                .assertError(throwable -> ((HttpException)throwable).code() == 404);
    }

    @Test
    public void contributors() throws Exception {
        gitHubService.listContributors("yhoshino11", "ArchLinux")
                .map(contributors -> contributors.get(0))
                .test()
                .assertValue(contributor -> contributor.getName().equals("yhoshino11"));
    }

    private String responseBodyFromFile(String fileName) {
        try {
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream("api-response/" + fileName);
            BufferedSource source = Okio.buffer(Okio.source(inputStream));
            return source.readString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }
}