package dk.convergens.mmr.message.log;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 *
 * @author Magnus
 */
@RegisterForReflection
public class ErrorLog {

    private String stackTrace;

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(Exception e) {
        this.stackTrace = e.getStackTrace().toString();
    }

}
