package gitlet;

import java.io.File;
import java.io.Serializable;
import static gitlet.Utils.*;

public class Blob implements Serializable {

    private String fileHash;

    private String fileContent;

    private String fileName;

    private File file;

    public Blob(String fileName) {
        this.fileName = fileName;
        this.fileContent = readContentsAsString(new File(fileName));

        this.fileHash = sha1(fileName, fileContent);
        this.file = join(Repository.BLOBS_DIR, fileHash);
    }

    public String getFileContent() {
        return fileContent;
    }

    public void save() {
        writeObject(file, this);
    }

    public String getFileHash() {
        return fileHash;
    }

    public String getFileName() {
        return fileName;
    }
}
