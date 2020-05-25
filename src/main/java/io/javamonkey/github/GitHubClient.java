package io.javamonkey.github;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.graphql.client.BlameQuery;
import com.example.graphql.client.SourceQuery;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GitHubClient {

    private static final Logger LOG = LoggerFactory.getLogger(GitHubClient.class);

    private OkHttpClient okHttpClient;
    private ApolloClient client;

    public GitHubClient(String url, String token) {
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> chain.proceed(chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build()))
                .build();

        client = ApolloClient.builder()
                .serverUrl(url)
                .okHttpClient(okHttpClient)
                .build();
    }

    public GitHubClient(String url) {
        okHttpClient = new OkHttpClient.Builder()
                .build();

        client = ApolloClient.builder()
                .serverUrl(url)
                .okHttpClient(okHttpClient)
                .build();
    }

    public BlameQuery.Data blame(String repoOwner, String repoName, String ref, String path) {

        try {
            BlameQuery query = BlameQuery.builder()
                    .repoOwner(repoOwner)
                    .repoName(repoName)
                    .ref(ref)
                    .path(path)
                    .blob(ref + ":" + path)
                    .build();

            CompletableFuture<BlameQuery.Data> future = new CompletableFuture<>();
            ApolloQueryCall<Optional<BlameQuery.Data>> observable = client.query(query);
            observable.enqueue(new ApolloCall.Callback<Optional<BlameQuery.Data>>() {
                @Override
                public void onResponse(@NotNull Response<Optional<BlameQuery.Data>> response) {
                    if (response.getData().isPresent()) {
                        future.complete(response.getData().get());
                    } else {
                        future.complete(null);
                    }
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    future.completeExceptionally(e);
                }
            });

            BlameQuery.Data result = future.get();
            observable.cancel();
            return result;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Unexpected error trying to get Blame data.", e);
        }
    }

    public SourceQuery.Data source(String repoOwner, String repoName, String ref, String path) {
        try {
            SourceQuery query = SourceQuery.builder()
                    .repoOwner(repoOwner)
                    .repoName(repoName)
                    .blob(ref + ":" + path)
                    .build();

            CompletableFuture<SourceQuery.Data> future = new CompletableFuture<>();
            ApolloQueryCall<Optional<SourceQuery.Data>> observable = client.query(query);
            observable.enqueue(new ApolloCall.Callback<Optional<SourceQuery.Data>>() {
                @Override
                public void onResponse(@NotNull Response<Optional<SourceQuery.Data>> response) {
                    if (response.getData().isPresent()) {
                        future.complete(response.getData().get());
                    } else {
                        future.complete(null);
                    }
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    future.completeExceptionally(e);
                }
            });
            SourceQuery.Data result = future.get();
            observable.cancel();
            return result;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Unexpected error trying to get Blame data.", e);
        }
    }

}
