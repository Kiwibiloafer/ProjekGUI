# README - Bali Rent Car GUI Project
Project Started at 20 Feb 2025

## Deskripsi Proyek
Bali Rent Car adalah sebuah aplikasi berbasis GUI yang dikembangkan menggunakan Java AWT dan terhubung ke database MySQL melalui XAMPP. Aplikasi ini dirancang untuk membantu manajemen penyewaan mobil dengan fitur yang dapat diakses oleh staff dan manager sesuai dengan hak akses masing-masing.

## Fitur yang Tersedia
1. **Login Form**
   - Pengguna dapat login menggunakan kredensial yang tersimpan dalam database `projekgui` pada tabel `employees`.
   - Sistem memvalidasi username dan password sebelum mengizinkan akses.
   
2. **Dashboard Lengkap**
   - Menampilkan fitur yang berbeda berdasarkan peran pengguna (staff atau manager).
   - Hanya fitur tertentu yang dapat diakses oleh manager.
   - Desain UI telah disempurnakan agar lebih user-friendly.

3. **Subdashboard dengan CRUD (Create, Read, Update, Delete)**
   - Fitur CRUD telah dikembangkan untuk berbagai entitas dalam sistem, termasuk penyewaan mobil, data pelanggan, dan daftar kendaraan.
   
4. **Manajemen Database yang Lengkap**
   - Semua tabel yang dibutuhkan untuk operasional sistem telah dibuat, termasuk `employees`, `cars`, `rentals`, dan `customers`.
   - Implementasi relasi antar tabel untuk memastikan data tetap konsisten.
   
5. **Koneksi Database yang Stabil**
   - Menggunakan JDBC untuk koneksi yang stabil ke MySQL melalui XAMPP.
   - Optimasi query untuk meningkatkan performa saat memproses data dalam jumlah besar.
   
6. **Keamanan Data**
   - Penerapan hashing password untuk meningkatkan keamanan login.
   - Validasi input pengguna untuk mencegah SQL Injection.

## Teknologi yang Digunakan
- **Java AWT** untuk GUI.
- **MySQL (XAMPP)** untuk manajemen database.
- **JDBC** untuk koneksi database.
- **Swing dan Layout Manager** untuk tampilan yang lebih dinamis.

## Cara Menjalankan Aplikasi
1. **Persiapan Lingkungan**
   - Pastikan Java Development Kit (JDK) sudah terinstal di komputer.
   - Unduh dan install XAMPP untuk menjalankan server database.
   
2. **Konfigurasi Database**
   - Jalankan XAMPP dan pastikan Apache serta MySQL dalam keadaan aktif.
   - Buka phpMyAdmin dan buat database baru dengan nama `projekgui`.
   - Import file `projekgui.sql` yang berisi struktur dan data awal untuk tabel `employees`, `cars`, `rentals`, dan `customers`.
   
3. **Menjalankan Aplikasi**
   - Buka proyek di IDE seperti NetBeans atau IntelliJ IDEA.
   - Pastikan dependensi JDBC telah ditambahkan ke classpath proyek.
   - Jalankan file utama `Main.java` untuk membuka aplikasi.
   
4. **Login ke Aplikasi**
   - Gunakan akun yang telah terdaftar dalam database `employees`.
   - Jika login berhasil, pengguna akan diarahkan ke dashboard sesuai dengan peran mereka.

5. **Menggunakan Fitur-Fitur Aplikasi**
   - Tambahkan, ubah, atau hapus data mobil, pelanggan, dan penyewaan melalui antarmuka yang tersedia.
   - Lakukan pencarian data dengan fitur filter yang telah disediakan.
   - Logout untuk keluar dari sesi pengguna saat selesai menggunakan aplikasi.

## Catatan Tambahan
- Aplikasi ini telah selesai dikembangkan dan siap untuk digunakan dalam operasional manajemen penyewaan mobil.
- Dokumentasi API dan panduan pengguna tersedia dalam folder `docs/` untuk referensi lebih lanjut.
- Kontribusi dalam bentuk peningkatan fitur atau perbaikan bug tetap terbuka bagi siapa saja yang tertarik untuk mengembangkan proyek ini lebih lanjut.

---

**Author:** Alif Jamaludin & Nindita Ramya

**Status Proyek:** Selesai ✅

