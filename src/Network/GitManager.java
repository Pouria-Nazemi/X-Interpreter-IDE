package Network;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class GitManager {
    private UsernamePasswordCredentialsProvider credentialsProvider;
    private File gitDir;
    private String user;
    private String pass;

    public void setUser(String username,String password) {
        credentialsProvider =new UsernamePasswordCredentialsProvider(username,password);
        user=username;
        pass=password;
    }

    public String getUsername() {
        return user;
    }

    public String getPassword() {
        return pass;
    }

    public  boolean isRemote(){
        try(Git git = Git.open(gitDir)) {
            return credentialsProvider !=null &&
                    !git.remoteList().call().isEmpty();
        } catch (GitAPIException | IOException e) {
            return false;
        }
    }

    public void connect(String url, String address){
        if(!find(address)) {
            try {
                Git.init().setDirectory(new File(address)).call();
            } catch (GitAPIException e) {
                e.printStackTrace();
                return;
            }
            find(address);
        }

        try(Git git = Git.open(gitDir)) {

            if (git.remoteList().call().isEmpty()) {
                // add remote repo:
                RemoteAddCommand remoteAddCommand = git.remoteAdd();
                remoteAddCommand.setName("origin");
                remoteAddCommand.setUri(new URIish(url));
                remoteAddCommand.call();
            }else
                git.remoteSetUrl().setRemoteName("origin").setRemoteUri(new URIish(url)).call();
        } catch (GitAPIException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }

    public String getRemoteUrl(){
        String url="";
        try(Git git = Git.open(gitDir)) {
           url= git.getRepository().getConfig().getString("remote", "origin", "url");
        }catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    public  boolean find(String address){
        if (address==null)
            return false;
        RepositoryBuilder repository= new RepositoryBuilder().findGitDir(new File(address));

        if (repository.getGitDir() != null) {
            gitDir= repository.getGitDir();
            return true;
        }else
            return false;
    }

    public  void clone(String url, String path) throws GitAPIException {
                  Git.cloneRepository()
                     .setURI(url)
                     .setCredentialsProvider(credentialsProvider)
                     .setDirectory(new File(path))
                     .call();

             gitDir=new File(path);

        System.out.println();

    }

    public void commit(String commitMessage) {
        try(Git git = Git.open(gitDir)) {
            Status status = git.status().call();

            //add new files
            if (!status.getUntracked().isEmpty()) {
                AddCommand addCommand = git.add();
                for (String s : status.getUntracked()) {
                    addCommand.addFilepattern(s);
                }
                addCommand.call();
            }

            git.commit().setAll(true)
                    .setMessage(commitMessage)
                    .call();

        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    public void push() throws GitAPIException, IOException {
            try(Git git = Git.open(gitDir)) {

                // push to remote:
                PushCommand pushCommand = git.push().setRemote("origin").add("master");
                pushCommand.setCredentialsProvider(credentialsProvider);
                pushCommand.call();
            }
        }

    public boolean pull() {
            try (Git git = Git.open(gitDir)) {

                PullResult result = git.pull()
                        .setCredentialsProvider(credentialsProvider)
                        .setRemote("origin")
                        .setRemoteBranchName("master")
                        .call();

                return result.isSuccessful();
            } catch (IOException | GitAPIException e) {
                e.printStackTrace();
            }
            return false;
        }

}
