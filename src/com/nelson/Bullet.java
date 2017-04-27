package com.nelson;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.UUID;

/**
 * Created by nelso on 4/13/2017.
 */
public class Bullet {
    String id;
    float x, y, xOrigin, yOrigin, heading, speed;
    long lastUpdate, currentUpdate;

    public Bullet (float x, float y, float heading, float speed) {
        this.x = x;
        this.y = y;
        xOrigin = x;
        yOrigin = y;
        this.heading = heading;
        this.speed = speed;
        lastUpdate = System.nanoTime();
        id = UUID.randomUUID().toString();
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public float update() {
        // update is independent of time
        currentUpdate = System.nanoTime();
        // 1 billion ns divided by the current difference in ns
        // equals the number of updates per second
        float timeDif = 1000000000 / (currentUpdate - lastUpdate);

        float radians = (float)(heading * 2 * Math.PI)/360;
        x = x + (float)(Math.sin(radians) * (speed / timeDif));

        return (float) Math.sqrt(Math.pow((x-xOrigin),2) + Math.pow((y-yOrigin),2));
    }

    public JsonObject getJSON() {
        return Json.createObjectBuilder()
                .add("id", id)
                .add("x", x)
                .add("y", y)
                .add("heading", heading)
                .add("speed", speed)
                .build();
    }
}
