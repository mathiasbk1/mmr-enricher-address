package dk.convergens.mmr.message.database;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 *
 * @author Magnus
 */
@RegisterForReflection
public class AuthorizationCallback {

    private String callBackUrl, authorizationId;

    public AuthorizationCallback() {
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(String authorizationId) {
        this.authorizationId = authorizationId;
    }

    @Override
    public String toString() {
        return "AuthorizationCallback{" + "callBackUrl=" + callBackUrl + ", authorizationId=" + authorizationId + '}';
    }

}
