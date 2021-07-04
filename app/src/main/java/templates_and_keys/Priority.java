package templates_and_keys;

public enum Priority {
    HIGH(3),
    MEDIUM(2),
    LOW(1),
    NONE(0);

    private final int priorityLevelValue;

    Priority(int i) {
        this.priorityLevelValue = i;
    }

    public int getPriorityLevelCode() {
        return this.priorityLevelValue;
    }
}
