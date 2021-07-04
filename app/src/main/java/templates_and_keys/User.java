package templates_and_keys;


import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class User implements Parcelable {
    public String profilePic;
    public String firstName, lastName, emailAddress, phoneNumber, studentIdNumber, accountOpenedDate; // user basic information
    public Streak userStreaks; // stores the streak points
    public HashMap<String, Streak> streakHistory; // stores the past streak points: Key (Date) -> Streak Value
    public HashMap<String, HashMap<String, Task>> taskLists; //stores the task: Date -> UID -> Task (String)
    public Integer numberOfTasks; //stores the number of tasks the user has created
    public String lastLoggedInDate; //stores the last time the user logged in
    public HashMap<String, Class> classes; // stores the classes list: key -> classUid : value = Class Object
    public String canvasAPIKey; // stores the canvas api key : this value is periodically refreshed-> accessed via OAUTH 2 Canvas LMS Rest API

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String firstName, String lastName, String emailAddress, String phoneNumber, String studentIdNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.studentIdNumber = studentIdNumber;
        this.numberOfTasks = 0;

        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        this.accountOpenedDate = currentDate.format(new Date());
        this.userStreaks = new Streak(this.accountOpenedDate);
        this.streakHistory = new HashMap<>();
        this.streakHistory.put(accountOpenedDate, userStreaks);
        this.taskLists = new HashMap<>();
        this.classes = new HashMap<>();
    }

    protected User(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        emailAddress = in.readString();
        phoneNumber = in.readString();
        studentIdNumber = in.readString();
        accountOpenedDate = in.readString();
        if (in.readByte() == 0) {
            numberOfTasks = null;
        } else {
            numberOfTasks = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(emailAddress);
        dest.writeString(phoneNumber);
        dest.writeString(studentIdNumber);
        dest.writeString(accountOpenedDate);
        if (numberOfTasks == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(numberOfTasks);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
