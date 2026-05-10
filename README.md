# 🍽️ Restoran Kasir — Sistem Pemesanan Digital

Aplikasi kasir restoran berbasis desktop yang dibangun dengan **JavaFX** dan **Gradle** sebagai tugas mata kuliah **Pemrograman Desktop**. Aplikasi ini menerapkan arsitektur MVC (Model-View-Controller) dan mengimplementasikan berbagai konsep dasar pemrograman Java.

---

## 📋 Daftar Isi

- [Fitur Aplikasi](#-fitur-aplikasi)
- [Teknologi yang Digunakan](#-teknologi-yang-digunakan)
- [Struktur Proyek](#-struktur-proyek)
- [Cara Menjalankan](#-cara-menjalankan)
- [Implementasi Konsep Pemrograman](#-implementasi-konsep-pemrograman)
- [Tampilan Aplikasi](#-tampilan-aplikasi)

---

## ✨ Fitur Aplikasi

- 📌 **Daftar Menu** — Menampilkan 16 item menu (Makanan & Minuman) dalam tabel interaktif
- 🔍 **Filter Kategori** — Filter menu berdasarkan kategori: Semua, Makanan, atau Minuman
- 🛒 **Manajemen Pesanan** — Tambah dan hapus item pesanan dengan validasi input
- 💰 **Perhitungan Otomatis** — Kalkulasi subtotal, pajak 10%, biaya layanan, diskon, dan promo
- 🎁 **Promo Beli 1 Gratis 1** — Promo otomatis untuk minuman jika subtotal > Rp 50.000
- 🏷️ **Diskon 10%** — Diskon otomatis jika subtotal > Rp 100.000
- 🧾 **Cetak Struk Digital** — Generate struk pembayaran berformat teks
- 🔄 **Reset Transaksi** — Mulai transaksi baru dengan konfirmasi dialog

---

## 🛠️ Teknologi yang Digunakan

| Teknologi | Versi | Keterangan |
|-----------|-------|------------|
| Java | 21 | Bahasa pemrograman utama |
| JavaFX | 24.0.1 | Framework UI desktop |
| Gradle | (Wrapper) | Build tool & dependency management |
| FXML | — | Deklarasi layout UI |
| CSS | — | Styling antarmuka |

---

## 📁 Struktur Proyek

```
restoran-kasir/
├── build.gradle                    # Konfigurasi build & dependensi
├── settings.gradle                 # Nama proyek Gradle
├── gradlew.bat                     # Gradle Wrapper (Windows)
└── src/
    └── main/
        ├── java/
        │   ├── module-info.java    # Deklarasi Java Module
        │   └── com/restoran/kasir/
        │       ├── Main.java                        # Entry point aplikasi
        │       ├── controller/
        │       │   └── KasirController.java         # Logika UI & bisnis
        │       └── model/
        │           ├── Menu.java                    # Model data menu
        │           └── Order.java                   # Model data pesanan
        └── resources/
            └── com/restoran/kasir/
                ├── kasir-view.fxml  # Layout UI (FXML)
                └── styles.css       # Styling antarmuka
```

---

## 🚀 Cara Menjalankan

### Prasyarat

- **Java 21** atau lebih tinggi sudah terinstal
- **JAVA_HOME** sudah dikonfigurasi di environment variable

### Langkah-langkah

1. **Clone repositori**
   ```bash
   git clone <url-repositori>
   cd restoran-kasir
   ```

2. **Jalankan aplikasi** menggunakan Gradle Wrapper:
   ```bash
   # Windows
   .\gradlew.bat run

   # Linux / macOS
   ./gradlew run
   ```

3. Aplikasi akan terbuka dengan ukuran jendela default **1280 × 820 px**.

> **Catatan:** Tidak perlu instalasi Gradle secara terpisah karena proyek sudah menyertakan Gradle Wrapper (`gradlew.bat`).

---

## 🧩 Implementasi Konsep Pemrograman

Proyek ini mengimplementasikan berbagai indikator penilaian mata kuliah:

### 1. Dasar Pemrograman

| Konsep | Implementasi |
|--------|-------------|
| **Kelas & Objek** | `Menu`, `Order`, `KasirController`, `Main` |
| **Inheritance** | `Main extends Application` (JavaFX) |
| **Tipe Data** | `String`, `double`, `int`, `boolean` |
| **Konstanta** | `PERSEN_PAJAK`, `BIAYA_LAYANAN`, `MAKS_ITEM_PESANAN`, dll. |
| **Method** | Setup, event handler, dan helper methods |
| **Override** | `toString()` di `Menu`, `start()` di `Main` |

### 2. Struktur Data

| Konsep | Implementasi |
|--------|-------------|
| **Array of Objects** | `Menu[] daftarMenu` — menyimpan 16 item menu |
| **Iterasi Array** | `for` loop untuk filter menu & kalkulasi pesanan |
| **Manipulasi String** | `StringBuilder` untuk struk, `String.format()`, `substring()`, `repeat()` |

### 3. Logika Keputusan

| Konsep | Implementasi |
|--------|-------------|
| **`if-else`** | Validasi input, cek diskon, cek promo |
| **`switch-case`** | Filter kategori menu (Makanan / Minuman / Semua) |
| **Nested `if`** | Kombinasi pengecekan diskon + promo secara bersamaan |

---

## 💡 Aturan Bisnis

| Aturan | Detail |
|--------|--------|
| Maksimal item berbeda | 4 menu per transaksi |
| Promo Beli 1 Gratis 1 | Berlaku untuk Minuman jika subtotal > **Rp 50.000** |
| Diskon | 10% dari subtotal jika subtotal > **Rp 100.000** |
| Pajak | 10% dihitung dari subtotal setelah potongan |
| Biaya Layanan | Rp 20.000 (tetap per transaksi) |

---

## 👤 Informasi Pengembang

> **Mata Kuliah:** Pemrograman Desktop  
> **Program Studi:** Teknik Informatika  

---

*Built with ❤️ using JavaFX & Gradle*
