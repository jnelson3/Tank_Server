package com.nelson;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.UUID;

/**
 * Created by nelso on 4/13/2017.
 */
public class Player {
    String id, name;
    float x, y, heading, driveSpeed, rotateSpeed;
    long lastUpdate, currentUpdate;

    public Player(float x, float y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
        heading = 0;
        driveSpeed = 0;
        rotateSpeed = 0;
        lastUpdate = System.nanoTime();
        id = UUID.randomUUID().toString();
    }

    public void setX(float x) {
        this.x = x;
    }
    public float getX() {
        return x;
    }
    public void setY(float y) {
        this.y = y;
    }
    public float getY() {
        return y;
    }
    public void setDriveSpeed(float speed) {
        driveSpeed = speed;
    }
    public float getDriveSpeed() {
        return driveSpeed;
    }
    public void setRotateSpeed(float speed) {
        rotateSpeed = speed;
    }
    public float getRotateSpeed() {
        return rotateSpeed;
    }

    public void update() {
        // update is independent of time
        currentUpdate = System.nanoTime();
        // 1 billion ns divided by the current difference in ns
        // equals the number of updates per second
        float timeDif = 1000000000 / (currentUpdate - lastUpdate);
        heading = heading + (rotateSpeed / timeDif);

        float radians = (float)(heading * 2 * Math.PI)/360;
        x = x + (float)(Math.sin(radians) * (driveSpeed / timeDif));
    }

    public String getID() {
        return id;
    }

    public JsonObject getJSON() {
        return Json.createObjectBuilder()
                .add("id", id)
                .add("x", x)
                .add("y", y)
                .add("heading", heading)
                .add("driveSpeed", driveSpeed)
                .add("rotateSpeed", rotateSpeed)
                .build();
    }
}
