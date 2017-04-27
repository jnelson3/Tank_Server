package com.nelson;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Created by nelso on 4/13/2017.
 */
public class Obstacle {
    float x, y, radius;

    public Obstacle (float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public JsonObject getJSON() {
        return Json.createObjectBuilder()
                .add("x", x)
                .add("y", y)
                .add("radius", radius)
                .build();
    }
}
