package com.nelson;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.util.ArrayList;

/**
 * Created by nelso on 4/13/2017.
 */
public class World {
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Obstacle> obstacles = new ArrayList<>();
    float xMax, yMax, bulletDTL;

    public World(float x, float y, float bulletDTL) {
        xMax = x;
        yMax = y;
        this.bulletDTL = bulletDTL;
    }

    public String registerPlayer(String name) {
        Player player = new Player(0, 0, name);
        players.add(player);
        return player.getID();
    }

    public void fireBullet(float x, float y, float heading, float speed) {
        Bullet bullet = new Bullet(x, y, heading, speed);
        bullets.add(bullet);
    }

    public void addObstacle(float x, float y, float radius) {
        Obstacle obstacle = new Obstacle(x, y, radius);
        obstacles.add(obstacle);
    }

    public float getWidth() {
        return xMax;
    }
    public float getHeight() {
        return yMax;
    }

    public JsonObject getJSON() {

        JsonArrayBuilder playerBuilder = Json.createArrayBuilder();
        for (Player player : players) {
            playerBuilder.add(player.getJSON());
        }

        JsonArrayBuilder bulletBuilder = Json.createArrayBuilder();
        for (Bullet bullet : bullets) {
            bulletBuilder.add(bullet.getJSON());
        }

        JsonArrayBuilder obstacleBuilder = Json.createArrayBuilder();
        for (Obstacle obstacle : obstacles) {
            obstacleBuilder.add(obstacle.getJSON());
        }

        return Json.createObjectBuilder()
                .add("messageType", "update")
                .add("map", Json.createObjectBuilder()
                    .add("obstacles", obstacleBuilder.build())
                    .add("dimensions", Json.createObjectBuilder()
                        .add("height", yMax)
                        .add("width", xMax))
                    .add("players", playerBuilder.build())
                    .add("bullets", bulletBuilder.build())
                )
                .build();

    }

    public void update() {
        for (Player player : players) {
            player.update();
            if (player.getX() > xMax) player.setX(xMax);
            if (player.getY() > yMax) player.setY(yMax);
        }
        // didn't use for:each loop because bullets.size() may change
        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).update() > bulletDTL) {
                bullets.remove(i);
            }
            for (Player player : players) {
                // TODO take into account size of tank
                if (bullets.get(i).getX() == player.getX() &&
                        bullets.get(i).getY() == player.getY()){
                    //TODO kill tank
                }
            }
        }
    }

    public static void main(String[] args) {
        World world = new World(20, 20, 10);
        world.registerPlayer("Jonathan");
        world.registerPlayer("Taylor");
        world.registerPlayer("Hannah");

        world.addObstacle(2,3,1);
        world.addObstacle(3,4,1);

        world.fireBullet(5,6,34, 1);

        System.out.println(world.getJSON().toString());
    }
}
