package com.qiuxs.codegenerate.context;

import java.io.IOException;

import com.qiuxs.codegenerate.TableBuilderService;
import com.qiuxs.codegenerate.utils.ComnUtils;

import javafx.concurrent.Service;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ContextManager {

	private static Service<Boolean> builderService = new TableBuilderService();
	static {
		EventHandler<WorkerStateEvent> onBuilderFinishHandler = new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				ContextManager.hideLoading();
			}
		};
		builderService.setOnCancelled(onBuilderFinishHandler);
		builderService.setOnFailed(onBuilderFinishHandler);
		builderService.setOnReady(onBuilderFinishHandler);
		builderService.setOnSucceeded(onBuilderFinishHandler);
	}

	private static Stage primaryStage;
	private static Stage loadingStage;
	private static Stage alertStage;

	private static String userName;
	private static String password;
	private static String host;
	private static String port;
	private static String database;

	private static String outPutPath;

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void setPrimaryStage(Stage primaryStage) {
		ContextManager.primaryStage = primaryStage;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		ContextManager.userName = userName;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		ContextManager.password = password;
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		ContextManager.host = host;
	}

	public static String getPort() {
		return port;
	}

	public static void setPort(String port) {
		ContextManager.port = port;
	}

	public static String getDatabase() {
		return database;
	}

	public static void setDatabase(String database) {
		ContextManager.database = database;
	}

	public static void showLoading() {
		getLoadingSatge().show();
	}

	public static void hideLoading() {
		getLoadingSatge().hide();
	}

	public static void destory() {
		if (loadingStage != null) {
			loadingStage.close();
		}
	}

	private static Stage getLoadingSatge() {
		try {
			if (loadingStage == null) {
				loadingStage = new Stage();
				loadingStage.initOwner(getPrimaryStage());
				loadingStage.initModality(Modality.APPLICATION_MODAL);
				loadingStage.initStyle(StageStyle.TRANSPARENT);
				loadingStage.setResizable(false);
				Parent loading_main = FXMLLoader.load(ContextManager.class.getResource("/loading.fxml"));
				loadingStage.setScene(new Scene(loading_main));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loadingStage;
	}

	public static void showAlert(String text) {
		Stage stage = getAlertStage();
		Scene scene = stage.getScene();
		Label label = (Label) scene.getRoot().getChildrenUnmodifiable().get(0);
		label.setText(text);
		stage.show();
	}

	private static Stage getAlertStage() {
		try {
			if (alertStage == null) {
				alertStage = new Stage();
				alertStage.initOwner(getPrimaryStage());
				alertStage.initModality(Modality.APPLICATION_MODAL);
				alertStage.initStyle(StageStyle.UTILITY);
				alertStage.setWidth(300);
				alertStage.setHeight(200);
				Parent alert_main = FXMLLoader.load(ContextManager.class.getResource("/alert.fxml"));
				alertStage.setScene(new Scene(alert_main));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return alertStage;
	}

	/**
	 * 信息是否完整
	 *
	 * @return
	 */
	public static boolean isComplete() {
		return ComnUtils.isNotBlank(userName) && ComnUtils.isNotBlank(password) && ComnUtils.isNotBlank(host)
				&& ComnUtils.isNotBlank(port);
	}

	public static void setOutPutPath(String outPutPath) {
		ContextManager.outPutPath = outPutPath;
	}

	public static String getOutPutPath() {
		return outPutPath;
	}

	public static void startBuilder() {
		showLoading();
		State state = builderService.getState();
		if (state != State.READY) {
			builderService.reset();
		}
		builderService.start();
	}

	public static void cancelBuild() {
		builderService.cancel();
	}
}
