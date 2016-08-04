package io.dbkf.countdown;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Countdown extends Application {
	
	private static Theme DEFAULT_THEME = Theme.DARK;
	private static Long DEFAULT_SECONDS = 600L;
	
	private static Theme THEME;
	private static Long SECONDS;
	
	enum Theme {
		DARK("dark", "-fx-background: #000000;", "#FFFFFF"), 
		LIGHT("light", "-fx-background: #FFFFFF;", "#000000");
		private final String descr;
		private final String fontColor;
		private final String backgroundTheme;

		Theme(String descr, String backgroundTheme, String fontColor) {
			this.descr = descr;
			this.backgroundTheme = backgroundTheme;
			this.fontColor = fontColor;
		}
		public String getDescr() {
			return descr;
		}
		
		public String getFontColor() {
			return fontColor;
		}
		
		public String getBackgroundTheme() {
			return backgroundTheme;
		}
	}
	
	/**
	 * @param args
	 * [0] - num of seconds
	 * [1] - Theme (dark or light)
	 */
	public static void main(String[] args) {
		SECONDS = args.length > 0 ? Long.valueOf(args[0]) : DEFAULT_SECONDS;
		THEME = args.length > 1 ? toTheme(args[1]) : DEFAULT_THEME;
		launch(args);
	}

	private static Theme toTheme(String themeNameFromArgs) {
		for (Theme theme : Theme.values()) {
			if (themeNameFromArgs.equals(theme.descr)) {
				return theme;
			}
		}
		throw new RuntimeException("Can't find theme with name " + themeNameFromArgs);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Countdown");
		stage.setFullScreen(true);
		stage.centerOnScreen();
		
		StackPane root = new StackPane();
		root.setPadding(new Insets(3));
		root.setStyle(THEME.getBackgroundTheme());

		Scene scene = new Scene(root, 300, 250);
		stage.setScene(scene);
		stage.show();

		long initTime = System.currentTimeMillis();

		Label label = new Label(formatSeconds(getCountdown(initTime, SECONDS)));
		Font font = new Font("Ubuntu Condensed", 350);
		label.setTextFill(Color.web(THEME.getFontColor()));
		label.setFont(font);
		root.getChildren().add(label);

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						long countdown = getCountdown(initTime, SECONDS);
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
