package com.ada.todoapp.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

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

    //TODO Enum?
    @Column(name = "status")
    String status;

    @Column(name = "name")
    String name;

    public Item(String status, String name) {
        this.status = status;
        this.name = name;
    }

    public Item() {
        super();
    }


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

    //DB operations

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

}
