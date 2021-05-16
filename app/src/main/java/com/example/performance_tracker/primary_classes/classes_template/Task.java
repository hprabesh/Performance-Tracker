package classes_template;


import java.io.Serializable;

public class Task implements Serializable {
    public  String uuid;
    private  String taskName;
    private  String taskDueDate;
    private Priority taskPriority;
    private  String classRelated;
    private  Boolean taskStatus;
    private Task(){
//        throw new AssertionError();
    }

    public Task( String uuid, String taskName, String taskDueDate, Priority taskPriority){
        this( uuid, taskName,taskDueDate, taskPriority, null);
    }

    public Task( String uuid, String taskName, String taskDueDate, Priority taskPriority, String classRelated){
        this.uuid = uuid;
        this.setTaskName(taskName);
        this.setTaskDueDate(taskDueDate);
        this.setTaskPriority(taskPriority);
        this.setClassRelated(classRelated);
        this.setTaskStatus(false);
    }

    public void setTaskStatus(Boolean taskStatus){
        this.taskStatus =taskStatus;
    }

    public Boolean getTaskStatus(){
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

}