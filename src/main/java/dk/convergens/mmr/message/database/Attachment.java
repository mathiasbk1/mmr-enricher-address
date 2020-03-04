package dk.convergens.mmr.message.database;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 *
 * @author Magnus
 */
@RegisterForReflection
public class Attachment {

    private String contentType, fileName;
    private int size;

    public Attachment() {
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Attachment{" + "contentType=" + contentType + ", fileName=" + fileName + ", size=" + size + '}';
    }

}
