package templates_and_keys;


import java.io.Serializable;
import java.util.UUID;

public class Task implements Serializable {
    public String uuid; // a unique hashed value
    private String taskName; // name of the task
    private String taskDueDate; // set the deadling of the task
    private Priority taskPriority; // a new features: to set the priority level of the task
    private String classRelated; // if the task is part of any current classes
    private Boolean taskStatus; // task status is a boolean value: True = task Completed, False = incomplete task
    private String taskCompletedDate;

    private Task() {
//        throw new AssertionError();
    }

    public Task(String taskName, String taskDueDate, Priority taskPriority) {
        this(
                UUID.randomUUID().toString().replace("-", ""),
                taskName,
                taskDueDate,
                taskPriority
        );
    }

    public Task(String taskName, String taskDueDate, Priority taskPriority, String classRelated) {
        this(
                UUID.randomUUID().toString().replace("-", ""),
                taskName,
                taskDueDate,
                taskPriority,
                classRelated
        );
    }

    public Task(String uuid, String taskName, String taskDueDate, Priority taskPriority) {
        this(uuid, taskName, taskDueDate, taskPriority, null);
    }

    public Task(String uuid, String taskName, String taskDueDate, Priority taskPriority, String classRelated) {
        this.uuid = uuid;
        this.setTaskName(taskName);
        this.setTaskDueDate(taskDueDate);
        this.setTaskPriority(taskPriority);
        this.setClassRelated(classRelated);
        this.setTaskStatus(false);
        this.setTaskCompletedDate(null);
    }


    public void setTaskStatus(Boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Boolean getTaskStatus() {
        return taskStatus;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDueDate() {
        return taskDueDate;
    }

    public void setTaskDueDate(String taskDueDate) {
        this.taskDueDate = taskDueDate;
    }

    public Priority getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(Priority taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getClassRelated() {
        return classRelated;
    }

    public void setClassRelated(String classRelated) {
        this.classRelated = classRelated;
    }

    public String getTaskCompletedDate() {
        return this.taskCompletedDate;
    }

    public void setTaskCompletedDate(String completionDate) {
        this.taskCompletedDate = completionDate;
    }
}
