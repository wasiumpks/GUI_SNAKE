package pl.maciejwasiak.snake;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private static double speed = 0.3;
    private Scene game;
    private Scene menu;
    private Scene level;
    private Scene board;
    private Scene window;

    private Stage theStage;
    private Stage stage;

    private String typedText;

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;

    private Button buttonOk;
    private TextField field;
    private Button backButton;

    private int score = 0;

    private Button easy;
    private Button medium;
    private Button hard;

    private Button male;
    private Button srednie;
    private Button duze;

    private Label scoreLab;
    private Label levelLab;
    private Label time;

    private String css;


    private Kierunki kierunki = Kierunki.RIGHT;
    private boolean moved = false;
    private boolean running = false;

    private Timeline timeline = new Timeline();

    private ObservableList<Node> snake;
    private ObservableList<Gracz> gracz = FXCollections.observableArrayList();

    static int min;
    static int sec;
    Runnable rn = () -> {
        System.exit(0);
    };
    KeyCombination keyCombination = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);

    private Parent createMenu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));

        Pane root1 = (Pane) root.lookup("#menu");
        root1.getStyleClass().add("back");

        btn1 = (Button) root.lookup("#btn1");
        btn1.getStyleClass().add("button-menu");

        btn2 = (Button) root.lookup("#btn2");
        btn2.getStyleClass().add("button-menu");

        btn3 = (Button) root.lookup("#btn3");
        btn3.getStyleClass().add("button-menu");

        btn4 = (Button) root.lookup("#btn4");
        btn4.getStyleClass().add("button-menu");

        btn5 = (Button) root.lookup("#btn5");
        btn5.getStyleClass().add("button-menu");

        btn1.setOnAction(e -> ButtonClicked(e));
        btn2.setOnAction(e -> ButtonClicked(e));
        btn3.setOnAction(e -> ButtonClicked(e));
        btn4.setOnAction(e -> ButtonClicked(e));
        btn5.setOnAction(e -> ButtonClicked(e));
        load();

        return root1;
    }

    private Parent createLevel() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Level.fxml"));

        Pane root1 = (Pane) root.lookup("#menu");
        root1.getStyleClass().add("back");

        easy = (Button) root.lookup("#easy");
        easy.getStyleClass().add("button-menu");

        medium = (Button) root.lookup("#medium");
        medium.getStyleClass().add("button-menu");

        hard = (Button) root.lookup("#hard");
        hard.getStyleClass().add("button-menu");

        easy.setOnAction(e -> ButtonClicked(e));
        medium.setOnAction(e -> ButtonClicked(e));
        hard.setOnAction(e -> ButtonClicked(e));

        return root1;
    }

    private Parent createScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ScreenSize.fxml"));

        Pane root1 = (Pane) root.lookup("#menu");
        root1.getStyleClass().add("back");

        male = (Button) root.lookup("#male");
        male.getStyleClass().add("button-menu");

        srednie = (Button) root.lookup("#srednie");
        srednie.getStyleClass().add("button-menu");

        duze = (Button) root.lookup("#duze");
        duze.getStyleClass().add("button-menu");

        male.setOnAction(e -> ButtonClicked(e));
        srednie.setOnAction(e -> ButtonClicked(e));
        duze.setOnAction(e -> ButtonClicked(e));

        return root1;
    }

    public void gameStarting() throws IOException {
        game = new Scene(create());

        game.getStylesheets().add(css);
        recursKey(game);
        theStage.setScene(game);
        theStage.show();
        startGame();
    }

    public void levelStarting() throws IOException {
        level = new Scene(createLevel());

        level.getStylesheets().add(css);
        theStage.setScene(level);
    }

    public void screenStarting() throws IOException {
        window = new Scene(createScreen());

        window.getStylesheets().add(css);
        theStage.setScene(window);
    }

    public Parent boardStarting() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderBoard.fxml"));

        Pane root1 = (Pane) root.lookup("#pane");
        root1.getStyleClass().add("back");

        backButton = (Button) root.lookup("#backButton");
        backButton.getStyleClass().add("button-menu");
        backButton.setOnAction(e -> ButtonClicked(e));

        TableView<Gracz> table = new TableView();
        table.getStyleClass().add("table-view");

        table.setMaxHeight(300);
        table.setMaxWidth(599);

        TableColumn<Gracz, String> name = new TableColumn<>("Name");
        name.setMinWidth(300);

        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Gracz, Integer> score = new TableColumn<>("Score");

        score.setMinWidth(300);
        score.setCellValueFactory(new PropertyValueFactory<>("score"));

        Collections.sort((List) gracz, new Comparator<Gracz>() {
            public int compare(Gracz c1, Gracz c2) {
                if (c1.getScore() > c2.getScore())
                    return -1;
                if (c1.getScore() < c2.getScore())
                    return 1;
                return 0;
            }
        });

        table.setItems(gracz);
        table.getColumns().addAll(name, score);

        root1.getChildren().add(table);

        return root1;

    }

    public void ButtonClicked(ActionEvent e) {

        if (e.getSource() == male) {
            theStage.setHeight(480);
            theStage.setWidth(640);
        }

        if (e.getSource() == srednie) {
            theStage.setHeight(600);
            theStage.setWidth(800);
        }

        if (e.getSource() == duze) {
            theStage.setHeight(720);
            theStage.setWidth(1280);
        }

        if (e.getSource() == btn1) {

            try {
                gameStarting();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

        if (e.getSource() == btn2) {
            try {
                board = new Scene(boardStarting());
                board.getStylesheets().add(css);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            theStage.setScene(board);
        }

        if (e.getSource() == btn4) {
            try {
                levelStarting();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (e.getSource() == btn5) {
            try {
                screenStarting();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (e.getSource() == btn3) {
            write(gracz);
            System.exit(0);
        }

        if (e.getSource() == easy) {
            speed = 0.3;

            theStage.setScene(menu);
        }

        if (e.getSource() == medium) {
            speed = 0.1;

            theStage.setScene(menu);
        }

        if (e.getSource() == hard) {
            speed = 0.07;

            theStage.setScene(menu);
        }

        if (e.getSource() == buttonOk) {
            typedText = field.getText();
            gracz.add(new Gracz(typedText, score));
            stage.close();
            stopGame();

        }

        if (e.getSource() == backButton) {
            theStage.setScene(menu);

        }

    }

    private Parent create() throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/fxml/SnakeScreen.fxml"));

        Pane root2 = (Pane) root1.lookup("#paneOut");
        root2.getStyleClass().add("back");

        Pane root = (Pane) root2.lookup("#panePlay");

        root.getStyleClass().add("playBack");

        timeline = new Timeline();
        root.setPrefSize(UstawieniaSnake.getWidth(), UstawieniaSnake.getHeight());
        score = 0;

        Group snakeBody = new Group();
        snake = snakeBody.getChildren();

        Rectangle food = Gra.newFood();

        scoreLab = (Label) root1.lookup("#score");
        scoreLab.setText("" + score);
        time = (Label) root1.lookup("#time");

        levelLab = (Label) root1.lookup("#level");

        if (speed == 0.3) {
            levelLab.setText("Easy");

        } else if (speed == 0.1) {
            levelLab.setText("Medium");
        } else if (speed == 0.07) {
            levelLab.setText("Hard");
        }

        KeyFrame frame = new KeyFrame(Duration.seconds(speed), event -> {
            time.setText(sec + " sec");
            if (!running)
                return;

            boolean toRemove = snake.size() > 1;

            Node tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0); // if
            double tailX = tail.getTranslateX();
            double tailY = tail.getTranslateY();

            switch (kierunki) {
                case UP:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() - UstawieniaSnake.getBlockSize());
                    break;
                case DOWN:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() + UstawieniaSnake.getBlockSize());
                    break;
                case RIGHT:
                    tail.setTranslateX(snake.get(0).getTranslateX() + UstawieniaSnake.getBlockSize());
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
                case LEFT:
                    tail.setTranslateX(snake.get(0).getTranslateX() - UstawieniaSnake.getBlockSize());
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
            }
            moved = true;
            if (toRemove)
                snake.add(0, tail);

            for (Node rect : snake) {
                if (rect != tail && tail.getTranslateX() == rect.getTranslateX()
                        && tail.getTranslateY() == rect.getTranslateY()) {

                    try {

                        stoppingGame();
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            Gra.tailConditions(tail);

            if (Gra.checkConditions(tail, food)) {
                ListIterator<Node> it = snake.listIterator();

                while (it.hasNext()) {

                    Node x = it.next();
                    boolean match = x.getTranslateX() == food.getTranslateX()
                            && x.getTranslateY() == food.getTranslateY();
                    if (match) {
                        food.setTranslateX(Gra.randXY(UstawieniaSnake.getWidth()));
                        food.setTranslateY(Gra.randXY(UstawieniaSnake.getHeight()));
                        while (it.hasPrevious()) {
                            it.previous();
                        }

                    }
                }

                Rectangle rect = Gra.grow(tailX, tailY);
                rect.getStyleClass().add("snake");
                score += 10;
                scoreLab.setText("" + score);

                snake.add(rect);
            }

        });

        timeline.getKeyFrames().addAll(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        root.getChildren().addAll(food, snakeBody);
        return root2;

    }

    private void stoppingGame() throws IOException {
        min = 0;
        sec = 0;
        stopGame();
        theStage.setScene(menu);
        stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Koniec gry");

        Parent root = (Parent) FXMLLoader.load(getClass().getResource("/fxml/Scores.fxml"));

        Pane root1 = (Pane) root.lookup("#scores");
        buttonOk = (Button) root.lookup("#enterButton");
        field = (TextField) root.lookup("#inputField");

        buttonOk.setOnAction(e -> ButtonClicked(e));

        Scene scene = new Scene(root1);
        scene.getStylesheets().add(css);
        stage.setScene(scene);

        stage.show();

    }

    private void stopGame() {
        running = false;
        timeline.stop();
        snake.clear();

    }

    private void startGame() {

        kierunki = Kierunki.RIGHT;

        Rectangle head = new Rectangle(UstawieniaSnake.getBlockSize(), UstawieniaSnake.getBlockSize());
        head.setTranslateY(Gra.randXY(UstawieniaSnake.getHeight()));
        head.getStyleClass().add("snake");

        snake.add(head);

        Czas czas = new Czas();
        czas.start();
        timeline.play();
        running = true;
    }

    public void recursKey(Scene scene) {


        scene.getAccelerators().put(keyCombination, rn);
        scene.setOnKeyPressed(event -> {
            if (!moved)
                return;

            switch (event.getCode()) {
                case W:
                    if (kierunki != Kierunki.DOWN)
                        kierunki = Kierunki.UP;
                    break;
                case S:
                    if (kierunki != Kierunki.UP)
                        kierunki = Kierunki.DOWN;
                    break;
                case D:
                    if (kierunki != Kierunki.LEFT)
                        kierunki = Kierunki.RIGHT;
                    break;
                case A:
                    if (kierunki != Kierunki.RIGHT)
                        kierunki = Kierunki.LEFT;
                    break;
                case ESCAPE:
                    stopGame();
                    theStage.setScene(menu);
                    break;
            }

            moved = false;
        });

    }

    private void load() {

        try {
            FileInputStream fis = new FileInputStream("PlayerSaveFile.ser"); // using
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<Gracz> list = (List<Gracz>) ois.readObject();

            gracz = FXCollections.observableList(list);
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }

    }

    public void write(ObservableList<Gracz> list) {

        try {

            FileOutputStream fos = new FileOutputStream("PlayerSaveFile.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<Gracz>(list));
            oos.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        theStage = primaryStage;
        css = this.getClass().getResource("/css/style.css").toExternalForm();

        menu = new Scene(createMenu());
        menu.getStylesheets().add(css);

        primaryStage.setTitle("Snake");
        primaryStage.setScene(menu);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);

    }

}
