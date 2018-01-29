package io.github.iamrajendra.chatexample;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rajendra on 28/1/18.
 */

public class UserModel implements Parcelable {

    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
    private int userId;
    private boolean status;
    private String id;
    private String name;

    public UserModel() {
    }

    protected UserModel(Parcel in) {
        this.userId = in.readInt();
        this.status = in.readByte() != 0;
        this.id = in.readString();
        this.name = in.readString();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getUserId() == ((UserModel) obj).getUserId();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeString(this.name);
    }
}
