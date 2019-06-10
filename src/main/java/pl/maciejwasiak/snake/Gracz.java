package pl.maciejwasiak.snake;

import java.io.Serializable;

public class Gracz implements Serializable {
    private String name;
    private int score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public int getScore() {

        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Gracz(String name, int score) {

        this.name = name;
        this.score = score;
    }
}
