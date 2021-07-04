package templates_and_keys;

public class Keys {
    // key of the fire-store collection
    public static final String USERS = "Users";
    // key of the bundle
    public static final String BUNDLE_USER = "user";
    // key to add the task
    public static final String TASK_LISTS = "taskLists";
    // key to add the class
    public static final String CLASS_LISTS_KEY = "classes";
    // key to add the canvas api key
    public static final String CLASS_CANVAS_API_KEY = "canvasAPIKey";
    // for accessing classes
    public static final String CANVAS_CLASS_JSON_URL = "https://uta.instructure.com/api/v1/courses.json";

    // for accessing all the homework for the given class
    public static String GET_CLASS_ASSIGNMENTS_URL(String classId) {
        if (classId != null)
            return "https://uta.instructure.com/api/v1/courses/".concat(classId).concat("/assignments");
        else
            return null;
    }

    // key for the email address on Database
    public static final String EMAIL_KEY = "emailAddress";
    // key for the notifications
    public static final String NOTIFICATIONS = "notifications";
    // key for the friend Requests
    public static final String FRIEND_REQUESTS = "friendRequests";
    // key for the Task
    public static final String TASK_OBJECT = "taskObject";
    // key for the task Uid
    public static final String TASK_OBJECT_ID = "uuid";
    // key for the task DueDate
    public static final String TASK_OBJECT_DATE = "taskDate";
    // key for getting class
    public static final String CLASS_NAME_OBJECT="class";
}
