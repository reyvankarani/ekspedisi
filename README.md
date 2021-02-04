aplikasi jasa kirim barang simple

Penggunaan
buat database dengan nama db_ekspedisi kemudian jalankan perintah SQL berikut

CREATE TABLE `tb_pengirimanbarang` (
 `noresi` varchar(10) NOT NULL,
 `tanggal` date NOT NULL,
 `petugas` varchar(15) NOT NULL,
 `kotatujuan` varchar(15) NOT NULL,
 `namapengirim` varchar(20) NOT NULL,
 `alamatpengirim` varchar(30) NOT NULL,
 `kodepospengirim` varchar(20) NOT NULL,
 `noteleponpengirim` varchar(20) NOT NULL,
 `namapenerima` varchar(20) NOT NULL,
 `alamatpenerima` varchar(30) NOT NULL,
 `kodepospenerima` varchar(20) NOT NULL,
 `noteleponpenerima` varchar(20) NOT NULL,
 `isibarang` varchar(20) NOT NULL,
 `instruksi` varchar(20) NOT NULL,
 `berat` int(15) NOT NULL,
 `jenispaket` varchar(20) NOT NULL,
 `total` int(30) NOT NULL,
 PRIMARY KEY (`noresi`)
) ENGINE=MyISAM

CREATE TABLE `tb_petugas` (
 `idpetugas` varchar(10) NOT NULL,
 `namadepan` varchar(30) NOT NULL,
 `namabelakang` varchar(30) NOT NULL,
 PRIMARY KEY (`idpetugas`)
) ENGINE=MyISAM

CREATE TABLE `users` (
 `id` int(15) NOT NULL AUTO_INCREMENT,
 `namad` varchar(20) NOT NULL,
 `namab` varchar(20) NOT NULL,
 `umur` int(15) NOT NULL,
 PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5
