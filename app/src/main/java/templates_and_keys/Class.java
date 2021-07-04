package templates_and_keys;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Class {
    public String className;
    public String classUid; // this stores the uuid value
    public String courseId; // this stores the canvas course id: managed by CANVAS Instructure

    @Exclude
    private List<Task> taskLists;

    private Class() {
    }

    public Class(String className) {
        this(
                className,
                UUID.randomUUID().toString().replace("-", ""),
                null
        );
    }

    public Class(String className, String classUid, String courseId) {
        this(className,
                classUid,
                courseId,
                null);
    }

    public Class(String className, String classUid, String courseId, ArrayList<Task> taskLists) {
        this.className = className;
        this.classUid = classUid;
        this.courseId = courseId;
        this.taskLists = taskLists;
    }

    @Exclude
    public List<Task> getTaskLists() {
        return this.taskLists;
    }

    @Exclude
    public void updateTaskLists(Task newTask) {
        if (this.taskLists.isEmpty())
            this.taskLists = new ArrayList<>();
        this.taskLists.add(newTask);
    }

    @Exclude
    @Override
    public String toString() {
        return this.className;
    }

    @Exclude
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Class) {
            Class c = (Class) obj;
            return c.className.equals(this.className) && c.classUid.equals(this.classUid);
        }
        return false;
    }
}
