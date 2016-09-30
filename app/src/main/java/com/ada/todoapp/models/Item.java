package com.ada.todoapp.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

/**
 * Created by ada on 9/13/16.
 */
@Table(database = ToDoDatabase.class)
@Parcel(analyze={Item.class})
public class Item extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "due_date")
    Date dueDate;

    //TODO Enum?
    @Column(name = "status")
    String status;

    //TODO Enum
    @Column(name = "priority")
    String priority;

    public Item(String status, String name) {
        this.status = status;
        this.name = name;
    }

    public static final String[] statuses = {"TODO", "DONE"};
    public static final String[] priorities = {"HIGH", "MEDIUM", "LOW"};

    public Item() {
        super();
    }

    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getStatus() {
        return this.status;
    }
    public Date getDueDate() {
        return this.dueDate;
    }
    public String getPriority() {
        return this.priority;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }

    //====== DB operations

    public static Item get(int id) {
        return SQLite.select().from(Item.class).where(Item_Table.id.eq(id)).querySingle();
    }

    public static List<Item> getAll() {
        return SQLite.select().from(Item.class).queryList();
    }

    public static void save(Item item) {
        item.save();
    }

    public static void delete(Item item) {
        item.delete();
    }

    //======= helpers


}
