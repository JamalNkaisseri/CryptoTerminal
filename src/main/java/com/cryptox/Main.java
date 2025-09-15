package com.cryptox;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        new MainWindow(stage).show(); // delegate everything
    }

    public static void main(String[] args) {
        launch();
    }
}
