package com.ada.todoapp.domain.model;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by ada on 9/26/16.
 */
@Database(name = ToDoDatabase.NAME, version = ToDoDatabase.VERSION)
public class ToDoDatabase {
    public static final String NAME = "todoDB";
    public static final int VERSION = 1;
}
