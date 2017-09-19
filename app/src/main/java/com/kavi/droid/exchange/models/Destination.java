package com.kavi.droid.exchange.models;

/**
 * Created by kwijewardana on 9/19/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class Destination {

    private int id;
    private String name;

    public Destination(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
