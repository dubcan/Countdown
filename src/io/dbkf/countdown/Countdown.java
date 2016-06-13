package io.dbkf.countdown;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Countdown extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parameters parameters = getParameters();
		String initCountDownStr = parameters.getRaw().get(0);
		long initCountdownTime = Long.valueOf(initCountDownStr);
		stage.setTitle("Countdown");
		stage.setFullScreen(true);
		stage.centerOnScreen();
		
		StackPane root = new StackPane();
		root.setPadding(new Insets(3));
		root.setStyle("-fx-background: #000000;");

		Scene scene = new Scene(root, 300, 250);
		stage.setFullScreen(true);
		stage.setScene(scene);
		stage.show();

		long initTime = System.currentTimeMillis();

		Label label = new Label(formatSeconds(getCountdown(initTime, initCountdownTime)));
		Font font = new Font("Ubuntu Condensed", 350);
		label.setFont(font);
		root.getChildren().add(label);

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						long countdown = getCountdown(initTime, initCountdownTime);
						if (countdown > 0) {
							label.setText(formatSeconds(countdown));
						} else {
							timer.cancel();
							root.getChildren().remove(label);
							root.setStyle("-fx-background: #777777;");
						}
					}
				});
			}
		}, 0, 33);
	}

	private String formatSeconds(long countdown) {
		long ss = countdown / 1000 % 60;
		long mm = countdown / 1000 / 60 % 60;
		long hh = countdown / 1000 / 60 / 60 % 24;
		String result = String.format("%02d:%02d:%02d", hh, mm, ss);
		return result;
	}

	private long getCountdown(long initTime, long initCountdownTime) {
		return initTime - (System.currentTimeMillis() - initCountdownTime);
	}
}
