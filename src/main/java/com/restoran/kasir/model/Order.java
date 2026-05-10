package com.restoran.kasir.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Kelas Model Order - Merepresentasikan pesanan pelanggan.
 *
 * IMPLEMENTASI TIPE DATA & OBJEK:
 * - Menggunakan objek Menu sebagai referensi
 * - int untuk jumlah pesanan
 * - double untuk subtotal (hasil perhitungan)
 */
public class Order {

    // === VARIABEL INSTANCE ===
    private final Menu menu;                           // Referensi ke objek Menu
    private final SimpleIntegerProperty jumlah;        // Tipe data int untuk jumlah pesanan
    private final SimpleDoubleProperty subtotal;       // Tipe data double untuk subtotal
    private final SimpleStringProperty namaMenu;       // Untuk binding ke TableView
    private final SimpleStringProperty hargaFormatted; // Harga yang sudah diformat

    /**
     * Konstruktor untuk membuat pesanan baru.
     *
     * @param menu   Objek Menu yang dipesan
     * @param jumlah Jumlah item yang dipesan (int)
     */
    public Order(Menu menu, int jumlah) {
        this.menu = menu;
        this.jumlah = new SimpleIntegerProperty(jumlah);

        // Menghitung subtotal: harga x jumlah
        double subtotalValue = menu.getHarga() * jumlah;
        this.subtotal = new SimpleDoubleProperty(subtotalValue);

        // IMPLEMENTASI STRING: Format teks untuk tampilan tabel
        this.namaMenu = new SimpleStringProperty(menu.getNama());
        this.hargaFormatted = new SimpleStringProperty(formatRupiah(menu.getHarga()));
    }

    // === GETTER & PROPERTY METHODS ===

    public Menu getMenu() {
        return menu;
    }

    public int getJumlah() {
        return jumlah.get();
    }

    public SimpleIntegerProperty jumlahProperty() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah.set(jumlah);
        // Update subtotal saat jumlah berubah
        this.subtotal.set(menu.getHarga() * jumlah);
    }

    public double getSubtotal() {
        return subtotal.get();
    }

    public SimpleDoubleProperty subtotalProperty() {
        return subtotal;
    }

    public String getNamaMenu() {
        return namaMenu.get();
    }

    public SimpleStringProperty namaMenuProperty() {
        return namaMenu;
    }

    public String getHargaFormatted() {
        return hargaFormatted.get();
    }

    public SimpleStringProperty hargaFormattedProperty() {
        return hargaFormatted;
    }

    /**
     * IMPLEMENTASI STRING: Method untuk memformat angka menjadi format Rupiah.
     * Menggunakan String.format() untuk manipulasi teks.
     *
     * @param angka Angka yang akan diformat
     * @return String dalam format "Rp XX.XXX"
     */
    public static String formatRupiah(double angka) {
        // Menggunakan manipulasi String untuk format mata uang Indonesia
        String formatted = String.format("%,.0f", angka);
        // Mengganti koma (format internasional) dengan titik (format Indonesia)
        formatted = formatted.replace(",", ".");
        return "Rp " + formatted;
    }
}
