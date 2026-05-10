package com.restoran.kasir.controller;

import com.restoran.kasir.model.Menu;
import com.restoran.kasir.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * KasirController - Controller utama untuk logika UI aplikasi kasir.
 *
 * IMPLEMENTASI INDIKATOR PENILAIAN:
 * 1. Dasar Pemrograman: Kelas, objek, method, tipe data, variabel
 * 2. Struktur Data: Array (Menu[]) dan String (manipulasi teks struk)
 * 3. Logika Keputusan: if-else, switch-case, nested if
 */
public class KasirController {

    // === KOMPONEN FXML ===
    @FXML private TableView<Menu> menuTable;
    @FXML private TableColumn<Menu, String> colMenuNama;
    @FXML private TableColumn<Menu, String> colMenuHarga;
    @FXML private TableColumn<Menu, String> colMenuKategori;
    @FXML private ToggleButton btnFilterSemua;
    @FXML private ToggleButton btnFilterMakanan;
    @FXML private ToggleButton btnFilterMinuman;
    @FXML private ComboBox<Menu> menuCombo;
    @FXML private Spinner<Integer> qtySpinner;
    @FXML private Button btnTambah;
    @FXML private Button btnHapus;
    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, String> colOrderNama;
    @FXML private TableColumn<Order, Integer> colOrderQty;
    @FXML private TableColumn<Order, String> colOrderHarga;
    @FXML private TableColumn<Order, String> colOrderSubtotal;
    @FXML private Label lblSubtotal;
    @FXML private Label lblTax;
    @FXML private Label lblService;
    @FXML private Label lblDiscount;
    @FXML private Label lblPromo;
    @FXML private Label lblTotal;
    @FXML private TextArea receiptArea;
    @FXML private Button btnHitung;
    @FXML private Button btnCetak;
    @FXML private Button btnBaru;

    // === KONSTANTA BISNIS (Indikator: Variabel & Tipe Data) ===
    private static final double PERSEN_PAJAK = 0.10;        // Pajak 10%
    private static final double BIAYA_LAYANAN = 20000;      // Biaya layanan Rp 20.000
    private static final double PERSEN_DISKON = 0.10;       // Diskon 10%
    private static final double BATAS_DISKON = 100000;      // Minimal subtotal untuk diskon
    private static final double BATAS_PROMO = 50000;        // Minimal subtotal untuk promo B1G1
    private static final int MAKS_ITEM_PESANAN = 4;         // Maksimal 4 menu per transaksi

    /**
     * IMPLEMENTASI ARRAY: Menyimpan seluruh data menu dalam Array of Objects.
     * Setiap elemen array adalah objek Menu dengan atribut: Nama, Harga, Kategori.
     */
    private final Menu[] daftarMenu = {
        // --- Kategori: Makanan ---
        new Menu("Nasi Goreng Spesial", 35000, "Makanan"),
        new Menu("Mie Goreng Jawa", 30000, "Makanan"),
        new Menu("Ayam Bakar Madu", 45000, "Makanan"),
        new Menu("Sate Ayam (10 tusuk)", 40000, "Makanan"),
        new Menu("Rendang Daging", 50000, "Makanan"),
        new Menu("Gado-Gado Jakarta", 25000, "Makanan"),
        new Menu("Soto Ayam Lamongan", 30000, "Makanan"),
        new Menu("Nasi Padang Komplit", 42000, "Makanan"),
        // --- Kategori: Minuman ---
        new Menu("Es Teh Manis", 8000, "Minuman"),
        new Menu("Es Jeruk Segar", 10000, "Minuman"),
        new Menu("Jus Alpukat", 15000, "Minuman"),
        new Menu("Kopi Hitam", 12000, "Minuman"),
        new Menu("Air Mineral", 5000, "Minuman"),
        new Menu("Es Campur", 18000, "Minuman"),
        new Menu("Teh Hangat", 7000, "Minuman"),
        new Menu("Jus Mangga", 15000, "Minuman")
    };

    // ObservableList untuk data pesanan (binding ke TableView)
    private final ObservableList<Order> daftarPesanan = FXCollections.observableArrayList();

    // ToggleGroup untuk filter kategori
    private final ToggleGroup filterGroup = new ToggleGroup();

    // Variabel untuk menyimpan hasil perhitungan
    private double hitungSubtotal = 0;
    private double hitungPajak = 0;
    private double hitungDiskon = 0;
    private double hitungPromo = 0;
    private double hitungTotal = 0;
    private boolean sudahDihitung = false;

    /**
     * Method initialize() - Dipanggil otomatis saat FXML dimuat.
     * Menginisialisasi semua komponen UI.
     */
    @FXML
    public void initialize() {
        setupMenuTable();
        setupOrderTable();
        setupFilterButtons();
        setupComboBox();
        setupSpinner();
    }

    // ================================================================
    //                     SETUP METHODS
    // ================================================================

    /** Mengatur kolom-kolom pada tabel menu */
    private void setupMenuTable() {
        colMenuNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colMenuKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));

        // IMPLEMENTASI STRING: Format harga ke Rupiah untuk kolom tabel
        colMenuHarga.setCellValueFactory(data -> {
            String hargaFormatted = Order.formatRupiah(data.getValue().getHarga());
            return new javafx.beans.property.SimpleStringProperty(hargaFormatted);
        });

        // Masukkan semua data dari ARRAY ke tabel
        menuTable.setItems(FXCollections.observableArrayList(daftarMenu));
        menuTable.setPlaceholder(new Label("Tidak ada menu"));

        // Klik pada tabel menu otomatis memilih di ComboBox
        menuTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    menuCombo.setValue(newVal);
                }
            }
        );
    }

    /** Mengatur kolom-kolom pada tabel pesanan */
    private void setupOrderTable() {
        colOrderNama.setCellValueFactory(new PropertyValueFactory<>("namaMenu"));
        colOrderQty.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        colOrderHarga.setCellValueFactory(new PropertyValueFactory<>("hargaFormatted"));

        // IMPLEMENTASI STRING: Format subtotal ke Rupiah
        colOrderSubtotal.setCellValueFactory(data -> {
            String subtotalFormatted = Order.formatRupiah(data.getValue().getSubtotal());
            return new javafx.beans.property.SimpleStringProperty(subtotalFormatted);
        });

        orderTable.setItems(daftarPesanan);
        orderTable.setPlaceholder(new Label("Belum ada pesanan"));
    }

    /** Mengatur filter toggle buttons dengan ToggleGroup */
    private void setupFilterButtons() {
        btnFilterSemua.setToggleGroup(filterGroup);
        btnFilterMakanan.setToggleGroup(filterGroup);
        btnFilterMinuman.setToggleGroup(filterGroup);
        btnFilterSemua.setSelected(true);

        filterGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            // Pastikan selalu ada satu toggle yang aktif
            if (newToggle == null) {
                oldToggle.setSelected(true);
                return;
            }
            filterMenu();
        });
    }

    /**
     * IMPLEMENTASI SWITCH-CASE: Filter menu berdasarkan kategori yang dipilih.
     * Menggunakan iterasi pada ARRAY daftarMenu.
     */
    private void filterMenu() {
        ObservableList<Menu> hasil = FXCollections.observableArrayList();
        String filter;

        // Tentukan filter berdasarkan toggle yang dipilih
        if (btnFilterMakanan.isSelected()) {
            filter = "Makanan";
        } else if (btnFilterMinuman.isSelected()) {
            filter = "Minuman";
        } else {
            filter = "Semua";
        }

        // IMPLEMENTASI ARRAY: Iterasi melalui array daftarMenu
        for (int i = 0; i < daftarMenu.length; i++) {
            Menu menu = daftarMenu[i];

            // IMPLEMENTASI SWITCH-CASE: Pencocokan kategori
            switch (filter) {
                case "Makanan":
                    if (menu.getKategori().equals("Makanan")) {
                        hasil.add(menu);
                    }
                    break;
                case "Minuman":
                    if (menu.getKategori().equals("Minuman")) {
                        hasil.add(menu);
                    }
                    break;
                case "Semua":
                default:
                    hasil.add(menu);
                    break;
            }
        }

        menuTable.setItems(hasil);
    }

    /** Mengatur ComboBox dengan data menu dari ARRAY */
    private void setupComboBox() {
        // IMPLEMENTASI ARRAY: Mengisi ComboBox dari array daftarMenu
        menuCombo.setItems(FXCollections.observableArrayList(daftarMenu));
    }

    /** Mengatur Spinner untuk input jumlah pesanan */
    private void setupSpinner() {
        SpinnerValueFactory<Integer> valueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        qtySpinner.setValueFactory(valueFactory);
        qtySpinner.setEditable(true);
    }

    // ================================================================
    //              EVENT HANDLERS (Aksi Tombol)
    // ================================================================

    /**
     * Handler tombol "Tambah Pesanan".
     * IMPLEMENTASI IF-ELSE: Validasi input sebelum menambahkan pesanan.
     */
    @FXML
    private void onTambahPesanan() {
        Menu menuDipilih = menuCombo.getValue();
        int jumlah = qtySpinner.getValue();

        // IMPLEMENTASI IF-ELSE: Validasi apakah menu sudah dipilih
        if (menuDipilih == null) {
            tampilkanPeringatan("Peringatan", "Silakan pilih menu terlebih dahulu!");
            return;
        }

        // IMPLEMENTASI IF: Cek apakah menu sudah ada di pesanan
        for (int i = 0; i < daftarPesanan.size(); i++) {
            if (daftarPesanan.get(i).getMenu() == menuDipilih) {
                // Update jumlah jika menu sudah ada
                Order existing = daftarPesanan.get(i);
                existing.setJumlah(existing.getJumlah() + jumlah);
                orderTable.refresh();
                resetInputPesanan();
                sudahDihitung = false;
                return;
            }
        }

        // IMPLEMENTASI IF-ELSE: Cek batas maksimal 4 menu berbeda
        if (daftarPesanan.size() >= MAKS_ITEM_PESANAN) {
            tampilkanPeringatan("Batas Tercapai",
                "Maksimal " + MAKS_ITEM_PESANAN + " menu berbeda per transaksi!\n" +
                "Silakan hapus pesanan yang ada jika ingin mengganti.");
            return;
        }

        // Tambahkan pesanan baru
        Order pesananBaru = new Order(menuDipilih, jumlah);
        daftarPesanan.add(pesananBaru);
        resetInputPesanan();
        sudahDihitung = false;
    }

    /**
     * Handler tombol "Hapus Pesanan Terpilih".
     */
    @FXML
    private void onHapusPesanan() {
        Order selected = orderTable.getSelectionModel().getSelectedItem();

        // IMPLEMENTASI IF-ELSE: Validasi apakah ada pesanan yang dipilih
        if (selected == null) {
            tampilkanPeringatan("Peringatan", "Silakan pilih pesanan yang ingin dihapus!");
        } else {
            daftarPesanan.remove(selected);
            sudahDihitung = false;
            resetHasilPerhitungan();
        }
    }

    /**
     * Handler tombol "Hitung Total".
     *
     * IMPLEMENTASI LOGIKA KEPUTUSAN:
     * - if-else untuk cek diskon (subtotal > Rp 100.000)
     * - if untuk cek promo B1G1 (subtotal > Rp 50.000)
     * - nested if untuk kombinasi diskon dan promo
     */
    @FXML
    private void onHitungTotal() {
        // Validasi: pesanan tidak boleh kosong
        if (daftarPesanan.isEmpty()) {
            tampilkanPeringatan("Peringatan", "Belum ada pesanan! Tambahkan menu terlebih dahulu.");
            return;
        }

        // --- LANGKAH 1: Hitung subtotal dari semua pesanan ---
        hitungSubtotal = 0;
        for (int i = 0; i < daftarPesanan.size(); i++) {
            hitungSubtotal += daftarPesanan.get(i).getSubtotal();
        }

        // --- LANGKAH 2: Hitung Promo Beli 1 Gratis 1 untuk Minuman ---
        hitungPromo = 0;

        // IMPLEMENTASI IF: Cek apakah subtotal memenuhi syarat promo
        if (hitungSubtotal > BATAS_PROMO) {
            // Iterasi pesanan untuk mencari item minuman
            for (int i = 0; i < daftarPesanan.size(); i++) {
                Order pesanan = daftarPesanan.get(i);
                String kategori = pesanan.getMenu().getKategori();

                // IMPLEMENTASI IF: Cek apakah item adalah minuman
                if (kategori.equals("Minuman")) {
                    /*
                     * PROMO BELI 1 GRATIS 1:
                     * Setiap 2 minuman, 1 gratis.
                     * Jumlah gratis = jumlah / 2 (pembulatan ke bawah)
                     * Potongan = jumlah_gratis x harga per item
                     */
                    int jumlahGratis = pesanan.getJumlah() / 2;
                    hitungPromo += jumlahGratis * pesanan.getMenu().getHarga();
                }
            }
        }

        // --- LANGKAH 3: Hitung Diskon ---
        hitungDiskon = 0;

        // IMPLEMENTASI NESTED IF: Cek diskon berdasarkan subtotal
        if (hitungSubtotal > BATAS_DISKON) {
            // Diskon 10% dari subtotal
            hitungDiskon = hitungSubtotal * PERSEN_DISKON;

            // NESTED IF: Jika ada promo juga, tampilkan info tambahan
            if (hitungPromo > 0) {
                // Pelanggan mendapat diskon DAN promo sekaligus
                System.out.println("INFO: Pelanggan mendapat DISKON 10% + PROMO B1G1!");
            }
        } else {
            // IMPLEMENTASI IF-ELSE: Subtotal tidak memenuhi syarat diskon
            if (hitungSubtotal > BATAS_PROMO) {
                System.out.println("INFO: Pelanggan mendapat PROMO B1G1 saja.");
            } else {
                System.out.println("INFO: Tidak ada diskon atau promo.");
            }
        }

        // --- LANGKAH 4: Hitung Pajak (10% dari subtotal setelah potongan) ---
        double totalSetelahPotongan = hitungSubtotal - hitungDiskon - hitungPromo;
        hitungPajak = totalSetelahPotongan * PERSEN_PAJAK;

        // --- LANGKAH 5: Hitung Total Akhir ---
        hitungTotal = totalSetelahPotongan + hitungPajak + BIAYA_LAYANAN;

        // --- Update tampilan label ---
        lblSubtotal.setText(Order.formatRupiah(hitungSubtotal));
        lblTax.setText(Order.formatRupiah(hitungPajak));
        lblService.setText(Order.formatRupiah(BIAYA_LAYANAN));

        // IMPLEMENTASI IF-ELSE: Tampilkan diskon jika ada
        if (hitungDiskon > 0) {
            lblDiscount.setText("- " + Order.formatRupiah(hitungDiskon));
        } else {
            lblDiscount.setText("-");
        }

        // IMPLEMENTASI IF-ELSE: Tampilkan promo jika ada
        if (hitungPromo > 0) {
            lblPromo.setText("- " + Order.formatRupiah(hitungPromo));
        } else {
            lblPromo.setText("-");
        }

        lblTotal.setText(Order.formatRupiah(hitungTotal));
        sudahDihitung = true;
    }

    /**
     * Handler tombol "Cetak Struk".
     * IMPLEMENTASI STRING: Membuat struk digital menggunakan manipulasi String.
     */
    @FXML
    private void onCetakStruk() {
        // Validasi: harus sudah dihitung dulu
        if (!sudahDihitung) {
            tampilkanPeringatan("Peringatan",
                "Silakan tekan 'Hitung Total' terlebih dahulu!");
            return;
        }

        // IMPLEMENTASI STRING: Menggunakan StringBuilder untuk membangun teks struk
        StringBuilder struk = new StringBuilder();
        String garis = "=".repeat(44);
        String garisStrip = "-".repeat(44);

        // Header struk
        struk.append(garis).append("\n");
        struk.append(tengahkanTeks("RESTORAN NUSANTARA", 44)).append("\n");
        struk.append(tengahkanTeks("Struk Pembayaran Digital", 44)).append("\n");
        struk.append(garis).append("\n");

        // IMPLEMENTASI STRING: Format tanggal dan waktu
        LocalDateTime sekarang = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss");
        String tanggalWaktu = sekarang.format(formatter);
        struk.append("Tanggal : ").append(tanggalWaktu).append("\n");
        struk.append(garisStrip).append("\n");

        // Detail pesanan
        struk.append(String.format("%-4s %-16s %3s %10s %9s\n",
            "No", "Menu", "Qty", "Harga", "Subtotal"));
        struk.append(garisStrip).append("\n");

        // IMPLEMENTASI ARRAY: Iterasi daftarPesanan untuk mencetak setiap item
        for (int i = 0; i < daftarPesanan.size(); i++) {
            Order p = daftarPesanan.get(i);

            // IMPLEMENTASI STRING: Potong nama menu jika terlalu panjang
            String namaMenu = p.getNamaMenu();
            if (namaMenu.length() > 16) {
                namaMenu = namaMenu.substring(0, 14) + "..";
            }

            struk.append(String.format("%-4d %-16s %3d %10s %9s\n",
                (i + 1),
                namaMenu,
                p.getJumlah(),
                formatAngka(p.getMenu().getHarga()),
                formatAngka(p.getSubtotal())
            ));
        }

        struk.append(garisStrip).append("\n");

        // Ringkasan biaya
        struk.append(String.format("%-28s %14s\n", "Subtotal:", formatAngka(hitungSubtotal)));

        // IMPLEMENTASI IF: Tampilkan info promo jika ada
        if (hitungPromo > 0) {
            struk.append("\n");
            struk.append("  * PROMO: Beli 1 Gratis 1\n");
            struk.append("    (Minuman)\n");
            struk.append(String.format("%-28s %14s\n",
                "  Potongan Promo:", "-" + formatAngka(hitungPromo)));
        }

        // IMPLEMENTASI IF: Tampilkan info diskon jika ada
        if (hitungDiskon > 0) {
            struk.append(String.format("%-28s %14s\n",
                "  Diskon 10%:", "-" + formatAngka(hitungDiskon)));
        }

        struk.append(String.format("%-28s %14s\n", "Pajak (10%):", formatAngka(hitungPajak)));
        struk.append(String.format("%-28s %14s\n", "Biaya Layanan:", formatAngka(BIAYA_LAYANAN)));
        struk.append(garis).append("\n");
        struk.append(String.format("%-28s %14s\n", "TOTAL BAYAR:", Order.formatRupiah(hitungTotal)));
        struk.append(garis).append("\n\n");
        struk.append(tengahkanTeks("Terima Kasih!", 44)).append("\n");
        struk.append(tengahkanTeks("Selamat Menikmati", 44)).append("\n");

        // Tampilkan struk di TextArea
        receiptArea.setText(struk.toString());
    }

    /**
     * Handler tombol "Transaksi Baru" - Reset seluruh data.
     */
    @FXML
    private void onTransaksiBaru() {
        // Konfirmasi jika ada pesanan aktif
        if (!daftarPesanan.isEmpty()) {
            Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION);
            konfirmasi.setTitle("Konfirmasi");
            konfirmasi.setHeaderText("Transaksi Baru");
            konfirmasi.setContentText("Apakah Anda yakin ingin memulai transaksi baru?\nSemua pesanan akan dihapus.");

            // IMPLEMENTASI IF-ELSE: Cek hasil konfirmasi
            if (konfirmasi.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                resetSemuaData();
            }
        } else {
            resetSemuaData();
        }
    }

    // ================================================================
    //                   HELPER METHODS
    // ================================================================

    /** Reset input pesanan ke default */
    private void resetInputPesanan() {
        menuCombo.setValue(null);
        qtySpinner.getValueFactory().setValue(1);
    }

    /** Reset label hasil perhitungan */
    private void resetHasilPerhitungan() {
        lblSubtotal.setText("Rp 0");
        lblTax.setText("Rp 0");
        lblService.setText("Rp 0");
        lblDiscount.setText("-");
        lblPromo.setText("-");
        lblTotal.setText("Rp 0");
        sudahDihitung = false;
    }

    /** Reset seluruh data untuk transaksi baru */
    private void resetSemuaData() {
        daftarPesanan.clear();
        receiptArea.clear();
        resetInputPesanan();
        resetHasilPerhitungan();
        hitungSubtotal = 0;
        hitungPajak = 0;
        hitungDiskon = 0;
        hitungPromo = 0;
        hitungTotal = 0;
    }

    /** Menampilkan dialog peringatan */
    private void tampilkanPeringatan(String judul, String pesan) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }

    /**
     * IMPLEMENTASI STRING: Format angka ke format Indonesia (dengan titik pemisah ribuan).
     */
    private String formatAngka(double angka) {
        String formatted = String.format("%,.0f", angka);
        return formatted.replace(",", ".");
    }

    /**
     * IMPLEMENTASI STRING: Menempatkan teks di tengah dengan lebar tertentu.
     * Menggunakan String.repeat() dan perhitungan panjang string.
     */
    private String tengahkanTeks(String teks, int lebar) {
        // IMPLEMENTASI IF-ELSE: Cek apakah teks lebih panjang dari lebar
        if (teks.length() >= lebar) {
            return teks;
        } else {
            int padding = (lebar - teks.length()) / 2;
            return " ".repeat(padding) + teks;
        }
    }
}
