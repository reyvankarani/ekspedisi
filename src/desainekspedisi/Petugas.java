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
public class Petugas {
        private String idPetugas;
        private String namaDepan;
        private String namaBelakang;
        
        public Petugas(String IdPetugas, String NamaDepan, String NamaBelakang){
            this.idPetugas = IdPetugas;
            this.namaDepan = NamaDepan;
            this.namaBelakang = NamaBelakang;
        }
        
        public String getIdPetugas()
        {
            return idPetugas;
        }
        
        public String getNamaDepan()
        {
            return namaDepan;
        }
        
        public String getNamaBelakang()
        {
            return namaBelakang;
        }        
        
        
        }

