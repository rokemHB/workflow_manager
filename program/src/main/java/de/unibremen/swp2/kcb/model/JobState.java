package de.unibremen.swp2.kcb.model;

/**
 * This class contains every state that a job can have
 *
 * @author Arvid
 */
public enum JobState {
    PENDING("PENDING"), PROCESSING("PROCESSING"), FINISHED("FINISHED"), CANCELLED("CANCELLED");

    /**
     * Name attribute of the JobState enum
     */
    private final String name;

    /**
     * Constructor of the enum
     * @param name the name
     */
    JobState(String name) {
        this.name = name;
    }

    /**
     * toString method
     * @return
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * fromString method of JobState enum
     * @param name the name
     * @return the JobState
     */
    public static JobState fromString(final String name) {
        switch (name) {
            case "PENDING":
                return JobState.PENDING;
            case "PROCESSING":
                return JobState.PROCESSING;
            case "FINISHED":
                return JobState.FINISHED;
            case "CANCELLED":
                return JobState.CANCELLED;
            default:
                return null;
        }
    }
}
