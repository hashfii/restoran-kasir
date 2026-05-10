package com.restoran.kasir;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main.java - Kelas utama untuk meluncurkan aplikasi Kasir Restoran.
 *
 * IMPLEMENTASI DASAR PEMROGRAMAN:
 * - Kelas Main sebagai entry point aplikasi JavaFX
 * - Menggunakan inheritance (extends Application)
 * - Override method start() dari kelas induk
 */
public class Main extends Application {

    // KONSTANTA: Ukuran default jendela aplikasi
    private static final double WINDOW_WIDTH = 1280;
    private static final double WINDOW_HEIGHT = 820;

    /**
     * Method start() - Dipanggil saat aplikasi JavaFX diluncurkan.
     * Memuat file FXML dan menampilkan jendela utama.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Memuat layout dari file FXML
        FXMLLoader fxmlLoader = new FXMLLoader(
                Main.class.getResource("kasir-view.fxml")
        );

        // Membuat Scene dengan ukuran yang ditentukan
        Scene scene = new Scene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);

        // Memuat stylesheet CSS untuk tampilan yang menarik
        String cssPath = Main.class.getResource("styles.css").toExternalForm();
        scene.getStylesheets().add(cssPath);

        // Konfigurasi jendela utama (Stage)
        stage.setTitle("Kasir Restoran Nusantara \u2014 Sistem Pemesanan Digital");
        stage.setScene(scene);
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.show();
    }

    /**
     * Method main() - Entry point utama aplikasi Java.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
