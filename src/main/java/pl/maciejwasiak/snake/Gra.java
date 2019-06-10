package pl.maciejwasiak.snake;

import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

public class Gra {

    public static int randXY(int value) {

        return (int) (Math.random() * (value - UstawieniaSnake.getBlockSize())) / UstawieniaSnake.getBlockSize()
                * UstawieniaSnake.getBlockSize();

    }

    public static Rectangle newFood() {
        Rectangle food = new Rectangle(UstawieniaSnake.getBlockSize(), UstawieniaSnake.getBlockSize());
        food.getStyleClass().add("food");

        food = setFood(food);

        return food;
    }

    public static Rectangle grow(double tailX, double tailY) {

        Rectangle rect = new Rectangle(UstawieniaSnake.getBlockSize(), UstawieniaSnake.getBlockSize());
        rect.setTranslateX(tailX);
        rect.setTranslateY(tailY);

        return rect;
    }

    public static boolean checkConditions(Node tail, Rectangle food) {
        boolean flag = false;

        if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY())
            flag = true;

        return flag;
    }

    public static Rectangle setFood(Rectangle food) {

        food.setTranslateX((int) (Math.random() * (UstawieniaSnake.getWidth() - UstawieniaSnake.getBlockSize()))
                / UstawieniaSnake.getBlockSize() * UstawieniaSnake.getBlockSize());
        food.setTranslateY((int) (Math.random() * (UstawieniaSnake.getHeight() - UstawieniaSnake.getBlockSize()))
                / UstawieniaSnake.getBlockSize() * UstawieniaSnake.getBlockSize());

        return food;
    }

    public static void tailConditions(Node tail) {

        if (tail.getTranslateX() < 0) {
            tail.setTranslateX(UstawieniaSnake.getWidth() - UstawieniaSnake.getBlockSize());
        }

        if (tail.getTranslateX() >= UstawieniaSnake.getWidth()) {
            tail.setTranslateX(0.0);
        }

        if (tail.getTranslateY() < 0) {
            tail.setTranslateY(UstawieniaSnake.getHeight() - UstawieniaSnake.getBlockSize());

        }
        if (tail.getTranslateY() >= UstawieniaSnake.getHeight()) {
            tail.setTranslateY(0.0);
        }

    }

}
