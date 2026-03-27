package gitlet;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  does at a high level.
 *
 */
public class Repository {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");

    public static final File COMMITS_DIR = join(OBJECTS_DIR, "commits");

    public static final File BLOBS_DIR = join(OBJECTS_DIR, "blobs");

    public static final File REFS_DIR = join(GITLET_DIR, "refs");

    public static final File HEADS_DIR = join(REFS_DIR, "head");

    public static final File HEAD = join(GITLET_DIR, "HEAD");

    public static final File STAGE = join(GITLET_DIR, "stage");

    private static Commit currentCommit;

    private static final String MASTER = "master";

    private static void initCheck(){
        if(!GITLET_DIR.exists()){
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    public static void init(){
        if(GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        COMMITS_DIR.mkdirs();
        BLOBS_DIR.mkdirs();
        HEADS_DIR.mkdirs();
        Stage stage = new Stage();
        writeStage(stage);

        Commit initCommit = new Commit();
        //写commit
        initCommit.save();

        //写当前分支名
        writeContents(HEAD, MASTER);

        //创建新分支
        updateRefHead(MASTER, initCommit.getHash());
    }

    private static void updateRefHead(String branchName, String commitHash){
        File branchFile = join(HEADS_DIR, branchName);
        writeContents(branchFile, commitHash);
    }

    public static void add(String fileName){
        initCheck();

        if(!userFileExists(fileName)){
            System.out.println("File does not exist.");
            System.exit(0);
        }

        Blob blob = new Blob(fileName);

        currentCommit = readCurCommit();
        Stage stage = readStage();
        Map<String, String> removeBlobs = stage.getRemoveBlobs();

        //文件不在当前commit里, 或不在stage里
        if(!currentCommit.getBlobHash().contains(blob.getFileHash())){
            Map<String, String> addBlobs = stage.getAddBlobs();
            if(!stage.fileExists(blob.getFileHash())){
                //保存新的文件
                blob.save();
            }

            //删除旧的文件映射
            addBlobs.remove(fileName);
            addBlobs.put(fileName,  blob.getFileHash());

            removeBlobs.remove(fileName);
            writeStage(stage);
        }
    }

    private static boolean userFileExists(String fileName){
        return join(CWD, fileName).exists();
    }

    private static Stage readStage(){
        return readObject(STAGE, Stage.class);
    }

    private static void writeStage(Stage stage){
        writeObject(STAGE, stage);
    }

    private static Commit readCurCommit(){
        String branch = readBranch();
        String commitHash = readContentsAsString(join(HEADS_DIR, branch));
        return readObject(join(COMMITS_DIR, commitHash), Commit.class);
    }

    private static Commit readCommit(String commitHash){
        if(commitHash.length() == 40) {
            return readObject(join(COMMITS_DIR, commitHash), Commit.class);
        }
        else {
            List<String> commitsHashList = plainFilenamesIn(COMMITS_DIR);
            String fullHash = commitsHashList.stream().filter(hash -> hash.startsWith(commitHash))
                    .findFirst().get();
            return readObject(join(COMMITS_DIR, fullHash), Commit.class);
        }
    }

    public static void commit(String message){
        initCheck();

        if(message.isEmpty()){
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }

        Stage stage = readStage();
        if(stage.getAddBlobs().isEmpty() && stage.getRemoveBlobs().isEmpty()){
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        currentCommit = readCurCommit();
        //继承当前commit的blob, 合并stage
        Map<String, String> commitBlobsMap = currentCommit.getBlobNameToHash();
        commitBlobsMap.putAll(stage.getAddBlobs());
        stage.getRemoveBlobs().values().forEach(commitBlobsMap::remove);

        Commit commit = new Commit(message, List.of(currentCommit.getHash()), commitBlobsMap);
        commit.save();

        flushStage(stage);

        updateRefHead(readBranch(), commit.getHash());
    }

    private static void flushStage(Stage stage){
        stage.getRemoveBlobs().clear();
        stage.getAddBlobs().clear();
        writeStage(stage);
    }

    private static String readBranch(){
        return readContentsAsString(HEAD);
    }

    public static void rm(String fileName){
        initCheck();

        //从stage删除
        Stage stage = readStage();

        boolean f = stage.getAddBlobs().remove(fileName) == null;

        currentCommit = readCurCommit();
        Map<String, String> blobNameToHash = currentCommit.getBlobNameToHash();
        if(blobNameToHash.containsKey(fileName)){
            join(CWD, fileName).delete();
            stage.getRemoveBlobs().put(fileName, blobNameToHash.get(fileName));
            blobNameToHash.remove(fileName);
        }
        //不在stage和commit里
        else if(f){
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        writeStage(stage);
    }

    public static void log(){
        initCheck();
        currentCommit = readCurCommit();
        while(currentCommit != null){
            printCommit(currentCommit);
            if(currentCommit.getParents() != null) {
                currentCommit = readCommit(currentCommit.getParents().get(0));
            }
            else break;
        }
    }

    private static void printCommit(Commit commit){
        System.out.println("===");
        System.out.println("commit " + commit.getHash());
        List<String> parents = commit.getParents();
        if(parents != null && parents.size() > 1){
            System.out.println("Merge: " + parents.get(0).substring(0, 7) + " " + parents.get(1).substring(0, 7));
        }
        System.out.println("Date: " + commit.getTimestamp());
        System.out.println(commit.getMessage());
        System.out.println();
    }

    public static void global_log(){
        initCheck();
        List<String> allCommits = plainFilenamesIn(COMMITS_DIR);

        allCommits.stream().map(Repository::readCommit)
                .forEach(Repository::printCommit);
    }

    public static void find(String findMessage){
        initCheck();
        List<String> allCommits = plainFilenamesIn(COMMITS_DIR);

        List<String> hashList = allCommits.stream().map(Repository::readCommit)
                .filter(commit -> commit.getMessage().equals(findMessage))
                .map(Commit::getHash)
                .collect(Collectors.toList());

        if(!hashList.isEmpty()){
            for(String hash : hashList){
                System.out.println(hash);
            }
        }
        else {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void status(){
        initCheck();
        List<String> branchList = plainFilenamesIn(HEADS_DIR);
        String curBranch = readBranch();

        System.out.println("=== Branches ===");
        System.out.println("*" + curBranch);

        branchList.stream().filter(branch -> !branch.equals(curBranch))
                .forEach(System.out::println);
        System.out.println();

        System.out.println("=== Staged Files ===");
        Stage stage = readStage();
        stage.getAddBlobs().keySet().forEach(System.out::println);
        System.out.println();

        System.out.println("=== Removed Files ===");
        stage.getRemoveBlobs().keySet().forEach(System.out::println);
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public static void checkout(String fileName){
        initCheck();
        currentCommit = readCurCommit();
        String fileHash = currentCommit.getBlobNameToHash().get(fileName);
        if(fileHash != null){
            writeFile(fileHash);
        }
        else{
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
    }

    private static void writeFile(String fileHash){
        Blob blob = readObject(join(BLOBS_DIR, fileHash),  Blob.class);
        File file = join(CWD, blob.getFileName());
        writeContents(file, blob.getFileContent());
    }

    public static void checkout(String commitHash, String fileName){
        initCheck();
        Commit commit = readCommit(commitHash);
        if(commit == null){
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        String fileHash = commit.getBlobNameToHash().get(fileName);
        if(fileHash == null){
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        writeFile(fileHash);
    }

    public static void checkoutBranch(String branch){
        initCheck();
        //分支不存在
        List<String> branchList = plainFilenamesIn(HEADS_DIR);
        if(!branchList.contains(branch)){
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        //分支是当前分支
        String curBranch = readBranch();
        if(branch.equals(curBranch)){
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        String commitHash = readContentsAsString(join(HEADS_DIR, branch));
        updateFileToCommit(commitHash);
        //更新HEAD
        writeContents(HEAD, branch);
        //清空stage
        clearStage();
    }

    private static void updateFileToCommit(String commitHash){
        //存在未跟踪的文件
        currentCommit = readCurCommit();
        Map<String, String> allTrackedFile = currentCommit.getBlobNameToHash();
        List<String> allCurFiles = plainFilenamesIn(CWD);
        if(allCurFiles.stream().anyMatch(i -> !allTrackedFile.containsKey(i))){
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }
        //删除多余文件
        Commit branchCommit = readCommit(commitHash);
        Map<String, String> allBranchFile = branchCommit.getBlobNameToHash();
        allTrackedFile.keySet().stream()
                .filter(i -> !allBranchFile.containsKey(i))
                .forEach(i -> restrictedDelete(join(CWD, i)));
        //替换文件
        allBranchFile.values().stream()
                .map(i -> readObject(join(BLOBS_DIR, i), Blob.class))
                .forEach(i -> writeContents(join(CWD, i.getFileName()), i.getFileContent()));

    }

    private static void clearStage(){
        Stage stage = readStage();
        stage.getAddBlobs().clear();
        stage.getRemoveBlobs().clear();
        writeStage(stage);
    }

    public static void branch(String branch){
        initCheck();
        validateBranch(branch);

        currentCommit = readCurCommit();
        writeContents(join(HEADS_DIR, branch), currentCommit.getHash());
    }

    private static void validateBranch(String branch){
        List<String> allBranch = plainFilenamesIn(HEADS_DIR);
        if(allBranch.contains(branch)){
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
    }

    public static void rm_branch(String branch){
        initCheck();
        List<String> allBranch = plainFilenamesIn(HEADS_DIR);
        if(!allBranch.contains(branch)){
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        String curBranch = readBranch();
        if(curBranch.equals(branch)){
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }

        File branchFile = (join(HEADS_DIR, branch));
        if(!branchFile.isDirectory()) branchFile.delete();
    }

    public static void reset(String commitHash){
        initCheck();
        List<String> allCommit = plainFilenamesIn(COMMITS_DIR);
        if(!allCommit.contains(commitHash)){
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        updateFileToCommit(commitHash);
        String curBranch = readBranch();
        writeContents(join(HEADS_DIR, curBranch), commitHash);
        clearStage();
    }

    /**
     * @apiNote
     * branch: 文件已修改    HEAD: 未修改 --> branch, stage
     * branch: 未修改       HEAD: 已修改 --> HEAD
     * branch: 已修改但内容一致或都已删除       HEAD:  --> HEAD
     * split point: 没有文件 branch: 无      HEAD: 有文件 --> HEAD
     * split point: 没有文件 branch: 有文件   HEAD: 无 --> branch, stage
     * split point: 存在文件 branch: 删除 HEAD: 未修改 --> 删除, stage
     * split point: 存在文件 branch: 未修改 HEAD: 删除 --> 删除
     * branch: 修改不一致 HEAD: 修改 --> conflict
     * branch: 修改或删除 HEAD: 删除或修改 --> conflict
     * @param branch
     */
    public static void merge(String branch){
        initCheck();
        validateBranch(branch);

        Stage stage = readStage();
        if(stage.getAddBlobs().isEmpty() || stage.getRemoveBlobs().isEmpty()) ;
        else {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }

        String curBranch = readBranch();
        if(curBranch.equals(branch)){
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }

        currentCommit = readCurCommit();
        String branchCommitHash = readContentsAsString(join(HEADS_DIR, branch));
        Commit branchCommit = readCommit(branchCommitHash);
        String splitPointHash = findSplitPoint(currentCommit, branchCommit);
        if(splitPointHash.equals(branchCommitHash)){
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }

        String curCommitHash = currentCommit.getHash();
        if(splitPointHash.equals(curCommitHash)){
            checkoutBranch(branch);
            System.out.println("Current branch fast-forwarded.");
        }

        List<String> curAdd = new ArrayList<>();
        List<String> curDelete = new ArrayList<>();
        List<String> curModify = new ArrayList<>();
        List<String> curUnModify = new ArrayList<>();

        List<String> branchAdd = new ArrayList<>();
        List<String> branchDelete = new ArrayList<>();
        List<String> branchModify = new ArrayList<>();
        List<String> branchUnModify = new ArrayList<>();

        Commit splitCommit = readCommit(splitPointHash);
        findDiffFromCommits(splitCommit, currentCommit, curModify, curUnModify, curAdd, curDelete);
        findDiffFromCommits(splitCommit, branchCommit, branchModify, branchUnModify, branchAdd, branchDelete);

        Map<String, String> curBlobMap = currentCommit.getBlobNameToHash();
        Map<String, String> branchBlobMap = branchCommit.getBlobNameToHash();
        //branch: 文件已修改    HEAD: 未修改 --> branch, stage
        branchModify.stream().filter(curUnModify::contains)
                .forEach(i -> {
                    String hash = branchBlobMap.get(i);
                    writeFileFromBlob(i, hash);
                    stage.getAddBlobs().put(i, hash);
                });
        //split point: 没有文件 branch: 有文件   HEAD: 无 --> branch, stage
        branchAdd.stream().filter(i -> !curAdd.contains(i))
                .forEach(i -> {
                    String hash = branchBlobMap.get(i);
                    writeFileFromBlob(i, hash);
                    stage.getAddBlobs().put(i, hash);
                });
        //split point: 存在文件 branch: 删除 HEAD: 未修改 --> 删除, stage
        branchDelete.stream().filter(i -> !curUnModify.contains(i))
                .forEach(i -> {
                    restrictedDelete(join(CWD, i));
                    stage.getRemoveBlobs().put(i, curBlobMap.get(i));
                });
        //branch: 修改不一致 HEAD: 修改 --> conflict
        //branch: 修改或删除 HEAD: 删除或修改 --> conflict
        List<String> conflictFiles = new ArrayList<>();
        for(var fileName : branchModify){
            if(curModify.contains(fileName) &&
                    !curBlobMap.get(fileName).equals(branchBlobMap.get(fileName))){
                conflictFiles.add(fileName);
            }

            if(curDelete.contains(fileName)) conflictFiles.add(fileName);
        }

        for(var fileName : curModify){
            if(branchDelete.contains(fileName)) conflictFiles.add(fileName);
        }
        if(!conflictFiles.isEmpty()) {
            System.out.println("Encountered a merge conflict.");
            for (var fileName : conflictFiles) {
                StringBuilder content = new StringBuilder("<<<<<<< HEAD\n");
                content.append(curBlobMap.getOrDefault(fileName, ""));
                content.append("=======\n");
                content.append(branchBlobMap.getOrDefault(fileName, ""));
                content.append(">>>>>>>\n");
                writeContents(join(CWD, fileName), content.toString());
            }
            String mergeMessage = String.format("Merged %s into %s.", branch, curBranch);
            curBlobMap.putAll(stage.getAddBlobs());
            stage.getRemoveBlobs().keySet().forEach(curBlobMap::remove);
            clearStage();
            Commit mergeCommit = new Commit(mergeMessage, List.of(curCommitHash, branchCommitHash), curBlobMap);
            mergeCommit.save();
        }
    }

    private static String findSplitPoint(Commit curCommit, Commit givenCommit){
        Commit commit1 = curCommit, commit2 = givenCommit;
        String commitHash1 = commit1.getHash(), commitHash2 = commit2.getHash();
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        set1.add(commitHash1);
        set2.add(commitHash2);

        Deque<String> commitDeque1 = new ArrayDeque<>();
        Deque<String> commitDeque2 = new ArrayDeque<>();
        if(commit1.getParents() != null){
            commitDeque1.addAll(commit1.getParents());
        }
        if(commit2.getParents() != null){
            commitDeque2.addAll(commit2.getParents());
        }

        while(!commitDeque1.isEmpty() || !commitDeque2.isEmpty()){
            if(!commitDeque1.isEmpty()){
                commitHash1 = commitDeque1.pop();
                if(set2.contains(commitHash1)){
                    return commitHash1;
                }

                set1.add(commitHash1);
                commit1 = readCommit(commitHash1);
                if(commit1.getParents() != null){
                    commitDeque1.addAll(commit1.getParents());
                }
            }

            if(!commitDeque2.isEmpty()){
                commitHash2 = commitDeque2.pop();
                if(set1.contains(commitHash2)){
                    return commitHash2;
                }

                set2.add(commitHash2);
                commit2 = readCommit(commitHash2);
                if(commit2.getParents() != null){
                    commitDeque2.addAll(commit2.getParents());
                }
            }
        }
        return null;
    }

    private static void findDiffFromCommits(Commit commit1, Commit commit2,
                                            List<String> modifyFile, List<String> unModifyFile, List<String> addFile, List<String> deleteFile){

        Map<String, String> commit1Files = commit1.getBlobNameToHash();
        Map<String, String> commit2Files = commit2.getBlobNameToHash();

        for(var s : commit1Files.keySet()){
            //同时存在, 比较内容
            if(commit2Files.containsKey(s)){
                if(commit1Files.get(s).equals(commit2Files.get(s))){
                    unModifyFile.add(s);
                }
                else modifyFile.add(s);
            }
            else deleteFile.add(s);
        }
        //增加的文件
        commit2Files.keySet().stream().filter(i -> !commit1Files.containsKey(i))
                .forEach(addFile::add);

    }

    private static void writeFileFromBlob(String fileName, String blobHash){
        String newContent = readContentsAsString(join(BLOBS_DIR, blobHash));
        writeContents(join(CWD, fileName), newContent);
    }
}
