package com.ada.todoapp.domain.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ada on 9/13/16.
 */
@Table(name = "Item", id="_id")
public class Item extends Model {

    @Column(name = "name", unique = true)
    String name;


}
