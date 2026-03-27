package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    private String timestamp;

    private Map<String, String> blobNameToHash;

    private String hash;

    private List<String> parents;

    private static final String INITMESSAGE = "initial commit";

    private static final File PATH = Repository.COMMITS_DIR;

    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    public Commit(){
        this.message = INITMESSAGE;
        this.timestamp = dateToTimeStamp(new Date(0));
        this.hash = sha1Hash();
        this.blobNameToHash = new HashMap<>();
    }

    public Commit(String message, List<String> parents, Map<String, String> blobNameToHash){
        this.message = message;
        this.timestamp = dateToTimeStamp(new Date());
        this.hash = sha1Hash();
        this.blobNameToHash = blobNameToHash;
        this.parents = parents;
    }

    private String sha1Hash(){
        return sha1(message, timestamp);
    }

    public void save(){
        File commit = join(PATH, hash);
        Utils.writeObject(commit, this);
    }

    public String getHash() {
        return hash;
    }

    public Map<String, String> getBlobNameToHash() {
        return blobNameToHash;
    }

    public List<String> getBlobHash(){
        return new ArrayList<>(blobNameToHash.values());
    }

    public List<String> getParents() {
        return parents;
    }

    public  String getMessage() {
        return message;
    }

    public  String getTimestamp() {
        return timestamp;
    }
}
