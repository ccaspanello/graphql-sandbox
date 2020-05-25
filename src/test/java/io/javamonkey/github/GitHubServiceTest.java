package io.javamonkey.github;

import com.example.graphql.client.BlameQuery;
import com.example.graphql.client.SourceQuery;
import org.junit.jupiter.api.Test;

import static io.javamonkey.github.Main.GITHUB_URL;
import static org.assertj.core.api.Assertions.assertThat;

public class GitHubServiceTest {

    String repoOwner = "ccaspanello";
    String repoName = "overops-event-generator";
    String ref = "master";
    String path = "README.md";

    @Test
    public void testBlame() {
        GitHubClient service = new GitHubClient(GITHUB_URL, TokenUtil.findToken());
        BlameQuery.Data data = service.blame(repoOwner, repoName, ref, path);
        BlameQuery.Commit commit = data.getRepository().get().getCommit().get();
        BlameQuery.Range range = ((BlameQuery.AsCommit) commit).getBlame().getRanges().get(0);
        assertThat(range.getStartingLine()).isEqualTo(1);
        assertThat(range.getEndingLine()).isEqualTo(1);
        assertThat(range.getCommit().getAuthor().get().getName().get()).isEqualTo("Tim Veil");

        BlameQuery.Content content = data.getRepository().get().getContent().get();
        assertThat(content.toString()).contains("This project is a simple Spring Boot application");
    }

    @Test
    public void testContent() {
        GitHubClient service = new GitHubClient(GITHUB_URL, TokenUtil.findToken());
        SourceQuery.Data data = service.source(repoOwner, repoName, ref, path);
        SourceQuery.Content content = data.getRepository().get().getContent().get();
        assertThat(content.toString()).contains("This project is a simple Spring Boot application");
    }

}
