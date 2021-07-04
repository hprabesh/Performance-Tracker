package templates_and_keys;

public abstract class NotificationObject {
    private String title; // this stores the title of notification

    private Object content; // content stores the friend request, streak points shared by friends ... and many more to come

    private String notificationUid; // this is the unique id for each notifications

    protected NotificationObject(String title, Object content, String notificationUid) {
        this.title = title;
        this.content = content;
        this.notificationUid = notificationUid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getNotificationUid() {
        return notificationUid;
    }

    public void setNotificationUid(String notificationUid) {
        this.notificationUid = notificationUid;
    }
}
