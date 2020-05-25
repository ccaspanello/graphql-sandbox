package io.javamonkey.github;

import com.example.graphql.client.SourceQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static final String GITHUB_URL = "https://api.github.com/graphql";

    public static void main(String[] args) throws Exception {
        testGitHubClient();


    }

    private static void testGitHubClient() {
        String token = TokenUtil.findToken();
        GitHubClient service = new GitHubClient(GITHUB_URL, token);
        SourceQuery.Data data = service.source("ccaspanello", "overops-event-generator", "master", "README.md");

        LOG.info("{}", data.getRepository().get().getContent().get());
//        System.exit(0);
    }

}
