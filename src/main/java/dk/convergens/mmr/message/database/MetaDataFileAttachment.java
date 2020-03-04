package dk.convergens.mmr.message.database;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Magnus
 */
@RegisterForReflection
public class MetaDataFileAttachment {

    private List<Attachment> attachments = new ArrayList<>();
    private String authorizationId, producerRefrence, messageUuid;
    private MetaDataAttachmentDataBaseInformation metaDataAttachmentDataBaseInformation;
    private int bundleSize;

    public MetaDataFileAttachment() {
    }

    public MetaDataAttachmentDataBaseInformation getMetaDataAttachmentDataBaseInformation() {
        return metaDataAttachmentDataBaseInformation;
    }

    public void setMetaDataAttachmentDataBaseInformation(MetaDataAttachmentDataBaseInformation metaDataAttachmentDataBaseInformation) {
        this.metaDataAttachmentDataBaseInformation = metaDataAttachmentDataBaseInformation;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(String authorizationId) {
        this.authorizationId = authorizationId;
    }

    public String getProducerRefrence() {
        return producerRefrence;
    }

    public void setProducerRefrence(String producerRefrence) {
        this.producerRefrence = producerRefrence;
    }

    public String getMessageUuid() {
        return messageUuid;
    }

    public void setMessageUuid(String messageUuid) {
        this.messageUuid = messageUuid;
    }

    public int getBundleSize() {
        return bundleSize;
    }

    public void setBundleSize(int bundleSize) {
        this.bundleSize = bundleSize;
    }

    @Override
    public String toString() {
        return "MetaDataAttachment{" + "attachments=" + attachments + ", authorizationId=" + authorizationId + ", producerRefrence=" + producerRefrence + ", messageUuid=" + messageUuid + ", metaDataAttachmentDataBaseInformation=" + metaDataAttachmentDataBaseInformation + ", bundleSize=" + bundleSize + '}';
    }


}
