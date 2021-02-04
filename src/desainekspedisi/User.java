/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desainekspedisi;

/**
 *
 * @author rxxxx
 */
public class User {
    private String noResi;
    private String tanggal;
    private String petugas;
    private String kotaTujuan;
    private String namaPengirim;
    private String alamatPengirim;
    private String kodePosPengirim;
    private String noTeleponPengirim;
    private String namaPenerima;
    private String alamatPenerima;
    private String kodePosPenerima;
    private String noTeleponPenerima;
    private String isiBarang;
    private String instruksi;
    private int berat;
    private String jenisPaket;
    private int total;
    
    public User(String NoResi, String Tanggal, String Petugas, String KotaTujuan, String NamaPengirim, String AlamatPengirim,
    String KodePosPengirim, String NoTeleponPengirim, String NamaPenerima, String AlamatPenerima, String KodePosPenerima, String NoTeleponPenerima, String IsiBarang,
    String Instruksi, int Berat, String JenisPaket, int Total){
        this.noResi = NoResi;
        this.tanggal = Tanggal;
        this.petugas = Petugas;
        this.kotaTujuan = KotaTujuan;
        this.namaPengirim = NamaPengirim;
        this.alamatPengirim = AlamatPengirim;
        this.kodePosPengirim = KodePosPengirim;
        this.noTeleponPengirim = NoTeleponPengirim;
        this.namaPenerima = NamaPenerima;
        this.alamatPenerima = AlamatPenerima;
        this.noTeleponPenerima = NoTeleponPenerima;
        this.kodePosPenerima = KodePosPenerima;
        this.isiBarang = IsiBarang;
        this.instruksi = Instruksi;
        this.berat = Berat;
        this.jenisPaket = JenisPaket;
        this.total = Total;
    } 
    
    public String getNoResi()
    {
     return noResi;   
    }
    
    public String getTanggal()
    {
     return tanggal;
    }
    
    public String getPetugas()
    {
     return petugas;
    }
    
    public String getKotaTujuan()
    {
     return kotaTujuan;   
    }
    
     public String getNamaPengirim()
    {
     return namaPengirim;   
    }
     
    public String getAlamatPengirim()
    {
     return alamatPengirim;   
    }
    
    public String getKodePosPengirim()
    {
     return kodePosPengirim;   
    }        
    
     public String getNoTeleponPengirim()
    {
     return noTeleponPengirim;   
    }
     
    public String getNamaPenerima()
    {
     return namaPenerima;
    }
    
    public String getAlamatPenerima()
    {
     return alamatPenerima;   
    }
    
    public String getKodePosPenerima()
    {
     return kodePosPenerima;   
    }        
    
    public String getNoTeleponPenerima()
    {
     return noTeleponPengirim;
    }
    
    public String getIsiBarang()
    {
     return isiBarang;
    }
    
    public String getInstruksi()
    {
        return instruksi;
    }
    
    public int getBerat()
    {
        return berat;
    }
    
    public String getJenisPaket()
    {
        return jenisPaket;
    }
    
    public int getTotal()
    {
        return total;
    }        
}
