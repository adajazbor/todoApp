package com.ada.todoapp.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by ada on 9/13/16.
 */
@Table(name = "Item", id = "_id")
public class Item extends Model implements Parcelable {

    //TODO Enum?
    @Column(name = "status")
    String status;

    @Column(name = "name", unique = true)
    String name;

    public Item(String status, String name) {
        this.status = status;
        this.name = name;
    }

    public Item() {
        super();
    }

    private Item(Parcel in) {
        this.name = in.readString();
        this.status = in.readString();
        //TODO how to set id in activeandroid??? there is no this.setId();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.status);
    }

    //DB operations

    public static Item get(int id) {
        return new Select().from(Item.class).where("remoteId = ? ", id).executeSingle();
    }

    public static List<Item> getAll() {
        return new Select().from(Item.class).orderBy("name").execute();
    }


    public static void save(Item item) {
        item.save();
    }

    public static void delete(Item item) {
        item.delete();
    }

}
