package com.restoran.kasir.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Kelas Model Menu - Merepresentasikan item menu restoran.
 *
 * IMPLEMENTASI TIPE DATA & VARIABEL:
 * - String untuk nama dan kategori menu
 * - double untuk harga menu
 * - JavaFX Property untuk binding ke TableView
 */
public class Menu {

    // === VARIABEL INSTANCE (Indikator: Tipe Data & Variabel) ===
    private final SimpleStringProperty nama;       // Tipe data String untuk nama menu
    private final SimpleDoubleProperty harga;      // Tipe data double untuk harga menu
    private final SimpleStringProperty kategori;   // Tipe data String untuk kategori (Makanan/Minuman)

    /**
     * Konstruktor untuk membuat objek Menu baru.
     *
     * @param nama     Nama menu (String)
     * @param harga    Harga menu dalam Rupiah (double)
     * @param kategori Kategori menu: "Makanan" atau "Minuman" (String)
     */
    public Menu(String nama, double harga, String kategori) {
        this.nama = new SimpleStringProperty(nama);
        this.harga = new SimpleDoubleProperty(harga);
        this.kategori = new SimpleStringProperty(kategori);
    }

    // === GETTER & SETTER (Indikator: Method) ===

    public String getNama() {
        return nama.get();
    }

    public SimpleStringProperty namaProperty() {
        return nama;
    }

    public double getHarga() {
        return harga.get();
    }

    public SimpleDoubleProperty hargaProperty() {
        return harga;
    }

    public String getKategori() {
        return kategori.get();
    }

    public SimpleStringProperty kategoriProperty() {
        return kategori;
    }

    // IMPLEMENTASI STRING: Override toString() untuk menampilkan nama menu di ComboBox
    @Override
    public String toString() {
        return nama.get();
    }
}
