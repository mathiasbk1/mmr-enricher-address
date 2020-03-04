/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.convergens.mmr.message.database;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 *
 * @author Magnus
 */
@RegisterForReflection
public class MetaDataAttachmentDataBaseInformation {

    private String id, rev;

    public MetaDataAttachmentDataBaseInformation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    @Override
    public String toString() {
        return "MetaDataAttachmentDataBaseInformation{" + "id=" + id + ", rev=" + rev + '}';
    }
    
}
