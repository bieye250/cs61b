package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Stage implements Serializable {

    private Map<String, String> addBlobs;
    private Map<String, String> removeBlobs;

    public Map<String, String> getAddBlobs() {
        return addBlobs;
    }

    public Map<String, String> getRemoveBlobs() {
        return removeBlobs;
    }

    public Stage(){
        addBlobs = new HashMap<>();
        removeBlobs = new HashMap<>();
    }

    public boolean fileExists(String fileHash){
        return addBlobs.containsValue(fileHash) || removeBlobs.containsValue(fileHash);
    }
}
