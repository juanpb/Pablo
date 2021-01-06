package p.pruebas;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;

/**
 * Created by Juan on 14/1/2020.
 */
public class GitHub {
    public static void main(String[] args) throws IOException {

        GitHubClient client = new GitHubClient();
//        client.setCredentials("juanpb", args[0]);
        RepositoryService service = new RepositoryService();
        Repository rep = service.getRepository("juanpb", "jpb");
        System.out.println("rep.generateId() = " + rep.generateId());
        System.out.println("rep.getDescription() = " + rep.getDescription());

//        service..

//        for (Repository repo : service.getRepositories("juanpb"))
//            System.out.println(repo.getName() + " Watchers: " + repo.getWatchers());
    }
}
