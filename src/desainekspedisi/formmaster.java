/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desainekspedisi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author rxxxx
 */
public class formmaster extends javax.swing.JFrame {

    /**
     * Creates new form formmaster
     */
    public Connection conn;
    public Statement state;
    PreparedStatement pst=null;
    ResultSet rs=null;
    
    

    public formmaster() {
        initComponents();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ekspedisi?zeroDateTimeBehavior=convertToNull", "root", "");
            state = conn.createStatement();
            JOptionPane.showMessageDialog(null, "Login Sucessfull");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + " Error connecting to Database!");
        }
        
        AutoCompleteDecorator.decorate(jComboBox_KotaTujuan);
        getContentPane().setBackground(new java.awt.Color(255,204,51));
        jButton_LaporkanBug.setOpaque(false);
        jButton_LaporkanBug.setContentAreaFilled(false); //to make the content area transparent
        jButton_LaporkanBug.setBorderPainted(false);
        Show_Petugas_In_JTable();
        Show_Users_In_JTable();
        ShowDate();
        ShowTime();
        FillCombo();
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we) {
                Exit(); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        //setWeidthHeightTable();
    }
    
    void OpenReport(){
        ReportMail r = new ReportMail();
        r.setVisible(true);
    }

    void Exit(){
        int x = JOptionPane.showConfirmDialog(null, "Apakah Anda Yakin Ingin Keluar?", "Keluar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(x==JOptionPane.YES_OPTION){
                    this.dispose();
                }
    }
    
    void ShowDate() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
        jLabel_Date.setText(s.format(d));

    }

    void ShowTime() {
        new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
        jLabel_Time.setText(s.format(d));
            }
        }
        ).start();
    }
    
    private void FillCombo(){
        try{
            String sql="select * from `tb_petugas`";
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            
            while(rs.next()){
                String name = rs.getString("namadepan");
                jComboBox_Petugas.addItem(name);
            }
        }catch(Exception e){
            
        }
    }
    
    void setWeidthHeightTable(){
        TableColumnModel columnmodel=jTable_Tampil.getColumnModel();
        columnmodel.getColumn(0).setPreferredWidth(200);
        columnmodel.getColumn(1).setPreferredWidth(200);
        columnmodel.getColumn(2).setPreferredWidth(200);
        columnmodel.getColumn(3).setPreferredWidth(200);
        columnmodel.getColumn(4).setPreferredWidth(200);
        columnmodel.getColumn(5).setPreferredWidth(200);
        columnmodel.getColumn(6).setPreferredWidth(200);
        columnmodel.getColumn(7).setPreferredWidth(200);
        columnmodel.getColumn(8).setPreferredWidth(200);
        columnmodel.getColumn(9).setPreferredWidth(200);
        
    }
    
    private void filter(String sql){
        DefaultTableModel model;
        model = (DefaultTableModel)jTable_Tampil.getModel();
        TableRowSorter<DefaultTableModel>tr = new TableRowSorter
                <DefaultTableModel>(model);
        jTable_Tampil.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(sql));
    }
    
    private void filter2(String sql){
        DefaultTableModel model;
        model = (DefaultTableModel)jTable_TampilPetugas.getModel();
        TableRowSorter<DefaultTableModel>tr = new TableRowSorter
                <DefaultTableModel>(model);
        jTable_TampilPetugas.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(sql));
    }

    public Connection getConnection() {
        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ekspedisi?zeroDateTimeBehavior=convertToNull", "root", "");
            return con;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<User> getUsersList() {
        ArrayList<User> userList = new ArrayList<User>();
        Connection connection = getConnection();
        String query = "SELECT * FROM `tb_pengirimanbarang` ";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            User user;
            while (rs.next()) {
                user = new User(rs.getString("noresi"), rs.getString("tanggal"), rs.getString("petugas"), rs.getString("kotatujuan"),
                        rs.getString("namapengirim"), rs.getString("alamatpengirim"), rs.getString("kodepospengirim"), rs.getString("noteleponpengirim"),
                        rs.getString("namapenerima"), rs.getString("alamatpenerima"), rs.getString("kodepospenerima"), rs.getString("noteleponpenerima"), rs.getString("IsiBarang"),
                        rs.getString("instruksi"), rs.getInt("berat"), rs.getString("jenispaket"), rs.getInt("total"));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }
    
    
        public ArrayList<Petugas> getPetugasList() {
        ArrayList<Petugas> petugasList = new ArrayList<Petugas>();
        Connection connection = getConnection();
        String query = "SELECT * FROM `tb_petugas` ";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            Petugas petugas;
            while (rs.next()) {
                petugas = new Petugas(rs.getString("idpetugas"), rs.getString("namadepan"), rs.getString("namabelakang"));      
                petugasList.add(petugas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return petugasList;
    }


    //Eksekusi Query SQL untuk table1
    public void ExecuteSQLQuery(String query, String message) {
        Connection con = getConnection();
        Statement st;
        try {
            st = con.createStatement();
            if ((st.executeUpdate(query)) == 1) {
                //refresh tabel
                DefaultTableModel model = (DefaultTableModel) jTable_Tampil.getModel();
                model.setRowCount(0);
                Show_Users_In_JTable();

                JOptionPane.showMessageDialog(null, "Data telah di" + message);
            } else {
                JOptionPane.showMessageDialog(null, "Data tidak" + message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    //Eksekusi Query SQL untuk table2
        public void ExecuteSQLQuery2(String query, String message) {
        Connection con = getConnection();
        Statement st;
        try {
            st = con.createStatement();
            if ((st.executeUpdate(query)) == 1) {
                //refresh tabel
                DefaultTableModel model = (DefaultTableModel) jTable_TampilPetugas.getModel();
                model.setRowCount(0);
                Show_Petugas_In_JTable();

                JOptionPane.showMessageDialog(null, "Data telah di" + message);
            } else {
                JOptionPane.showMessageDialog(null, "Data tidak" + message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Menampilkan data di tabel
    public void Show_Users_In_JTable() {
        ArrayList<User> list = getUsersList();
        DefaultTableModel model = (DefaultTableModel) jTable_Tampil.getModel();
        Object[] row = new Object[17];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getNoResi();
            row[1] = list.get(i).getTanggal();
            row[2] = list.get(i).getPetugas();
            row[3] = list.get(i).getKotaTujuan();
            row[4] = list.get(i).getNamaPengirim();
            row[5] = list.get(i).getAlamatPengirim();
            row[6] = list.get(i).getKodePosPengirim();
            row[7] = list.get(i).getNoTeleponPengirim();
            row[8] = list.get(i).getNamaPenerima();
            row[9] = list.get(i).getAlamatPenerima();
            row[10] = list.get(i).getKodePosPenerima();
            row[11] = list.get(i).getNoTeleponPenerima();
            row[12] = list.get(i).getIsiBarang();
            row[13] = list.get(i).getInstruksi();
            row[14] = list.get(i).getBerat();
            row[15] = list.get(i).getJenisPaket();
            row[16] = list.get(i).getTotal();

            model.addRow(row);
        }
    }
    
        public void Show_Petugas_In_JTable() {
        ArrayList<Petugas> plist = getPetugasList();
        DefaultTableModel model = (DefaultTableModel) jTable_TampilPetugas.getModel();
        Object[] row = new Object[3];
        for (int i = 0; i < plist.size(); i++) {
            row[0] = plist.get(i).getIdPetugas();
            row[1] = plist.get(i).getNamaDepan();
            row[2] = plist.get(i).getNamaBelakang();
            

            model.addRow(row);
        }
        
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jFrame1 = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jTextField_NoResi = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextField_NamaPengirim = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_AlamatPengirim = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField_NoTelpPengirim = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField_Deskripsi = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField_Instruksi = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextField_Berat = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jComboBox_JenisPaket = new javax.swing.JComboBox<>();
        jButton_Hitung = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jTextField_Total = new javax.swing.JTextField();
        jButton_Simpan = new javax.swing.JButton();
        jButton_Baru = new javax.swing.JButton();
        jLabel_Register = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jTextField_NamaPenerima = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_AlamatPenerima = new javax.swing.JTextArea();
        jLabel15 = new javax.swing.JLabel();
        jTextField_NoTelpPenerima = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton_Ubah = new javax.swing.JButton();
        jComboBox_KotaTujuan = new javax.swing.JComboBox<>();
        jDateChooser_Tanggal = new com.toedter.calendar.JDateChooser();
        jButton_TentangBerat = new javax.swing.JButton();
        jComboBox_Petugas = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        jButton_TentangJenis = new javax.swing.JButton();
        jButton_LaporkanBug = new javax.swing.JButton();
        jTextField_KodePosPenerima = new javax.swing.JTextField();
        jTextField_KodePosPengirim = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jButton_Hapus = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable_Tampil = new javax.swing.JTable();
        jButton_Ubah2 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jMonthChooser1 = new com.toedter.calendar.JMonthChooser();
        jButton_BuatLaporan = new javax.swing.JButton();
        jYearChooser1 = new com.toedter.calendar.JYearChooser();
        jPanel7 = new javax.swing.JPanel();
        jTextField_FilterNamaPengirim = new javax.swing.JTextField();
        jTextField_Filter = new javax.swing.JTextField();
        jTextField_FilterNoResi = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jTextField_NamaDepan = new javax.swing.JTextField();
        jTextField_IDPetugas = new javax.swing.JTextField();
        jTextField_NamaBelakang = new javax.swing.JTextField();
        jButton_SimpanPetugas = new javax.swing.JButton();
        jButton_BaruPetugas = new javax.swing.JButton();
        jButton_UbahPetugas = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable_TampilPetugas = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField_FilterPetugas = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel_welcome = new javax.swing.JLabel();
        jLabel_Date = new javax.swing.JLabel();
        jLabel_Time = new javax.swing.JLabel();
        jLabel_LogOut = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        jMenu3.setText("jMenu3");

        jMenu4.setText("jMenu4");

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rey Express Form");
        setBackground(new java.awt.Color(255, 204, 0));

        jTabbedPane1.setBackground(new java.awt.Color(255, 204, 0));

        jPanel4.setBackground(new java.awt.Color(255, 204, 0));
        jPanel4.setLayout(null);

        jTextField_NoResi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_NoResiActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField_NoResi);
        jTextField_NoResi.setBounds(120, 10, 111, 20);

        jLabel4.setText("No Resi                    ");
        jPanel4.add(jLabel4);
        jLabel4.setBounds(10, 10, 96, 14);

        jLabel5.setText("Tanggal                   ");
        jPanel4.add(jLabel5);
        jLabel5.setBounds(10, 50, 95, 14);

        jLabel6.setText("Nama Petugas         ");
        jPanel4.add(jLabel6);
        jLabel6.setBounds(10, 90, 96, 14);

        jLabel19.setText("Kota Tujuan            ");
        jPanel4.add(jLabel19);
        jLabel19.setBounds(10, 150, 100, 14);
        jPanel4.add(jTextField_NamaPengirim);
        jTextField_NamaPengirim.setBounds(120, 210, 111, 20);

        jLabel14.setText("Nama                       ");
        jPanel4.add(jLabel14);
        jLabel14.setBounds(10, 210, 96, 14);

        jLabel7.setForeground(new java.awt.Color(255, 0, 51));
        jLabel7.setText("Pengirim-Bekasi-");
        jPanel4.add(jLabel7);
        jLabel7.setBounds(10, 190, 110, 14);

        jTextArea_AlamatPengirim.setColumns(20);
        jTextArea_AlamatPengirim.setRows(5);
        jScrollPane1.setViewportView(jTextArea_AlamatPengirim);

        jPanel4.add(jScrollPane1);
        jScrollPane1.setBounds(120, 240, 250, 50);

        jLabel8.setText("Alamat                     ");
        jPanel4.add(jLabel8);
        jLabel8.setBounds(10, 240, 96, 14);

        jLabel10.setText("No Telepon              ");
        jPanel4.add(jLabel10);
        jLabel10.setBounds(10, 300, 96, 14);
        jPanel4.add(jTextField_NoTelpPengirim);
        jTextField_NoTelpPengirim.setBounds(120, 300, 111, 20);

        jLabel11.setText("Deskripsi Isi Barang ");
        jPanel4.add(jLabel11);
        jLabel11.setBounds(10, 340, 130, 14);

        jTextField_Deskripsi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_DeskripsiActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField_Deskripsi);
        jTextField_Deskripsi.setBounds(120, 340, 111, 20);

        jLabel12.setText("Instruksi Pengiriman");
        jPanel4.add(jLabel12);
        jLabel12.setBounds(10, 380, 140, 14);

        jTextField_Instruksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_InstruksiActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField_Instruksi);
        jTextField_Instruksi.setBounds(120, 380, 111, 20);

        jLabel18.setText("Berat                        ");
        jPanel4.add(jLabel18);
        jLabel18.setBounds(10, 420, 103, 14);

        jTextField_Berat.setText("0");
        jTextField_Berat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_BeratActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField_Berat);
        jTextField_Berat.setBounds(120, 420, 111, 20);

        jLabel21.setText("Jenis Paket               ");
        jPanel4.add(jLabel21);
        jLabel21.setBounds(10, 460, 99, 14);

        jComboBox_JenisPaket.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Normal", "Ekspres", "Ekonomis" }));
        jComboBox_JenisPaket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_JenisPaketActionPerformed(evt);
            }
        });
        jPanel4.add(jComboBox_JenisPaket);
        jComboBox_JenisPaket.setBounds(120, 460, 100, 20);

        jButton_Hitung.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jButton_Hitung.setText("Hitung");
        jButton_Hitung.setToolTipText("Hitung Total Yang Harus Dibayarkan");
        jButton_Hitung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_HitungActionPerformed(evt);
            }
        });
        jPanel4.add(jButton_Hitung);
        jButton_Hitung.setBounds(260, 500, 90, 30);

        jLabel20.setText("Total                         ");
        jPanel4.add(jLabel20);
        jLabel20.setBounds(10, 500, 99, 14);

        jTextField_Total.setEditable(false);
        jTextField_Total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_TotalActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField_Total);
        jTextField_Total.setBounds(120, 500, 111, 20);

        jButton_Simpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/save.png"))); // NOI18N
        jButton_Simpan.setText("Simpan");
        jButton_Simpan.setToolTipText("Simpan ke Data Pengiriman Ctrl + S");
        jButton_Simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SimpanActionPerformed(evt);
            }
        });
        jPanel4.add(jButton_Simpan);
        jButton_Simpan.setBounds(470, 490, 90, 40);

        jButton_Baru.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/new.png"))); // NOI18N
        jButton_Baru.setText("Baru");
        jButton_Baru.setToolTipText("Input Data Baru Ctrl + N");
        jButton_Baru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_BaruActionPerformed(evt);
            }
        });
        jPanel4.add(jButton_Baru);
        jButton_Baru.setBounds(570, 490, 90, 40);

        jLabel_Register.setForeground(new java.awt.Color(51, 153, 255));
        jLabel_Register.setText("register petugas baru");
        jLabel_Register.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_RegisterMouseClicked(evt);
            }
        });
        jPanel4.add(jLabel_Register);
        jLabel_Register.setBounds(120, 120, 180, 14);

        jLabel22.setText("Nama                       ");
        jPanel4.add(jLabel22);
        jLabel22.setBounds(400, 210, 96, 14);

        jTextField_NamaPenerima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_NamaPenerimaActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField_NamaPenerima);
        jTextField_NamaPenerima.setBounds(500, 210, 111, 20);

        jTextArea_AlamatPenerima.setColumns(20);
        jTextArea_AlamatPenerima.setRows(5);
        jScrollPane2.setViewportView(jTextArea_AlamatPenerima);

        jPanel4.add(jScrollPane2);
        jScrollPane2.setBounds(500, 240, 250, 50);

        jLabel15.setText("Alamat                     ");
        jPanel4.add(jLabel15);
        jLabel15.setBounds(400, 240, 100, 20);
        jPanel4.add(jTextField_NoTelpPenerima);
        jTextField_NoTelpPenerima.setBounds(500, 300, 111, 20);

        jLabel16.setText("No Telepon              ");
        jPanel4.add(jLabel16);
        jLabel16.setBounds(400, 300, 100, 14);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("INPUT PENGIRIMAN BARANG ");
        jPanel4.add(jLabel2);
        jLabel2.setBounds(520, 10, 244, 22);

        jButton_Ubah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/edit.png"))); // NOI18N
        jButton_Ubah.setText("Ubah");
        jButton_Ubah.setToolTipText("Ubah Data Ctrl  + E");
        jButton_Ubah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_UbahActionPerformed(evt);
            }
        });
        jPanel4.add(jButton_Ubah);
        jButton_Ubah.setBounds(670, 490, 90, 40);

        jComboBox_KotaTujuan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jakarta", "Bandung", "Karawang", "Purwakarta", "Sukabumi", "Cianjur", "Depok", "Tangerang", "Cilegon", "Bekasi", "Bogor", "Cimahi", "Serang", "Banjar", "Cirebon", "Semarang", "Magelang", "Kebumen", "Pekalongan", "Purworejo", "Cilacap", "Brebes", "Wonogiri", "Tegal", "Banyumas", "Pemalang", "Kudus", "Salatiga", "Surakarta", "Yogyakarta", "Bantul", "Malang", "Surabaya", "Gresik", "Sidoarjo", "Jombang", "Lamongan", "Ngawi", "Pacitan", "Madiun", "Probolinggo", "Kediri", "Mojokerto", "Banyuwangi", "Bondowoso", "Nganjuk" }));
        jPanel4.add(jComboBox_KotaTujuan);
        jComboBox_KotaTujuan.setBounds(120, 150, 100, 20);
        jPanel4.add(jDateChooser_Tanggal);
        jDateChooser_Tanggal.setBounds(120, 50, 130, 20);

        jButton_TentangBerat.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton_TentangBerat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/info.png"))); // NOI18N
        jButton_TentangBerat.setText("Tentang Berat");
        jButton_TentangBerat.setToolTipText("Ketahui Ketentuan Berat");
        jButton_TentangBerat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_TentangBeratActionPerformed(evt);
            }
        });
        jPanel4.add(jButton_TentangBerat);
        jButton_TentangBerat.setBounds(250, 420, 120, 20);

        jPanel4.add(jComboBox_Petugas);
        jComboBox_Petugas.setBounds(120, 90, 100, 20);

        jLabel29.setForeground(new java.awt.Color(255, 0, 51));
        jLabel29.setText("Penerima");
        jPanel4.add(jLabel29);
        jLabel29.setBounds(400, 190, 180, 14);

        jButton_TentangJenis.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton_TentangJenis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/info.png"))); // NOI18N
        jButton_TentangJenis.setText("Tentang Jenis");
        jButton_TentangJenis.setToolTipText("Ketahui Ketentuan Jenis Paket");
        jButton_TentangJenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_TentangJenisActionPerformed(evt);
            }
        });
        jPanel4.add(jButton_TentangJenis);
        jButton_TentangJenis.setBounds(250, 460, 120, 20);

        jButton_LaporkanBug.setText("Laporkan Bug");
        jButton_LaporkanBug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_LaporkanBugActionPerformed(evt);
            }
        });
        jPanel4.add(jButton_LaporkanBug);
        jButton_LaporkanBug.setBounds(660, 540, 100, 23);
        jPanel4.add(jTextField_KodePosPenerima);
        jTextField_KodePosPenerima.setBounds(670, 300, 80, 20);
        jPanel4.add(jTextField_KodePosPengirim);
        jTextField_KodePosPengirim.setBounds(290, 300, 80, 20);

        jLabel32.setText("Kode Pos");
        jPanel4.add(jLabel32);
        jLabel32.setBounds(620, 300, 50, 14);

        jLabel33.setText("Kode Pos");
        jPanel4.add(jLabel33);
        jLabel33.setBounds(240, 300, 50, 14);

        jTabbedPane1.addTab("Input Pengiriman", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 204, 0));
        jPanel5.setLayout(null);

        jButton_Hapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/delete.png"))); // NOI18N
        jButton_Hapus.setText("Hapus");
        jButton_Hapus.setToolTipText("Hapus Data Ctrl + D");
        jButton_Hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_HapusActionPerformed(evt);
            }
        });
        jPanel5.add(jButton_Hapus);
        jButton_Hapus.setBounds(340, 490, 100, 40);

        jTable_Tampil.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No Resi", "Tanggal", "Nama Petugas", "Kota Tujuan", "Nama Pengirim", "Alamat Pengirim", "Kode Pos Pengirim", "No Telepon Pengirim", "Nama Penerima", "Alamat Penerima", "Kode Pos Penerima", "No Teleopon Penerima", "Deskripsi", "Instruksi", "Berat", "Jenis Paket", "Total"
            }
        ));
        jTable_Tampil.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable_Tampil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_TampilMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable_Tampil);

        jPanel5.add(jScrollPane4);
        jScrollPane4.setBounds(21, 20, 740, 402);

        jButton_Ubah2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/edit.png"))); // NOI18N
        jButton_Ubah2.setText("Ubah");
        jButton_Ubah2.setToolTipText("Ubah Data Ctrl + E");
        jButton_Ubah2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Ubah2ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton_Ubah2);
        jButton_Ubah2.setBounds(340, 430, 100, 40);

        jPanel6.setBackground(new java.awt.Color(-13312,true));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Laporan"));
        jPanel6.setLayout(null);
        jPanel6.add(jMonthChooser1);
        jMonthChooser1.setBounds(16, 27, 98, 20);

        jButton_BuatLaporan.setText("Buat Laporan");
        jButton_BuatLaporan.setToolTipText("Buat Laporan Bulanan");
        jButton_BuatLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_BuatLaporanActionPerformed(evt);
            }
        });
        jPanel6.add(jButton_BuatLaporan);
        jButton_BuatLaporan.setBounds(150, 30, 97, 40);
        jPanel6.add(jYearChooser1);
        jYearChooser1.setBounds(40, 60, 47, 20);

        jPanel5.add(jPanel6);
        jPanel6.setBounds(500, 430, 260, 110);

        jPanel7.setBackground(new java.awt.Color(-13312,true));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Pencarian"));

        jTextField_FilterNamaPengirim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_FilterNamaPengirimKeyReleased(evt);
            }
        });

        jTextField_Filter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_FilterKeyReleased(evt);
            }
        });

        jTextField_FilterNoResi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_FilterNoResiKeyReleased(evt);
            }
        });

        jLabel23.setText("Nama Pengirim");

        jLabel28.setText("No Resi");

        jLabel31.setText("text");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addComponent(jTextField_FilterNoResi, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(jTextField_FilterNamaPengirim, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                        .addComponent(jTextField_Filter, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_FilterNoResi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_FilterNamaPengirim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_Filter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel7);
        jPanel7.setBounds(20, 430, 260, 116);

        jLabel30.setText("No Resi");
        jPanel5.add(jLabel30);
        jLabel30.setBounds(30, 450, 50, 14);

        jTabbedPane1.addTab("Data Pengiriman", jPanel5);

        jPanel3.setBackground(new java.awt.Color(255, 204, 0));
        jPanel3.setLayout(null);

        jLabel25.setText("Nama Depan");
        jPanel3.add(jLabel25);
        jLabel25.setBounds(30, 60, 90, 14);

        jLabel26.setText("Nama Belakang");
        jPanel3.add(jLabel26);
        jLabel26.setBounds(30, 100, 90, 14);

        jLabel27.setText("ID Petugas");
        jPanel3.add(jLabel27);
        jLabel27.setBounds(30, 20, 90, 14);

        jTextField_NamaDepan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_NamaDepanActionPerformed(evt);
            }
        });
        jPanel3.add(jTextField_NamaDepan);
        jTextField_NamaDepan.setBounds(130, 60, 111, 20);

        jTextField_IDPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_IDPetugasActionPerformed(evt);
            }
        });
        jPanel3.add(jTextField_IDPetugas);
        jTextField_IDPetugas.setBounds(130, 20, 111, 20);

        jTextField_NamaBelakang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_NamaBelakangActionPerformed(evt);
            }
        });
        jPanel3.add(jTextField_NamaBelakang);
        jTextField_NamaBelakang.setBounds(130, 100, 111, 20);

        jButton_SimpanPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/save.png"))); // NOI18N
        jButton_SimpanPetugas.setText("Simpan");
        jButton_SimpanPetugas.setToolTipText("Simpan Data Petugas Baru");
        jButton_SimpanPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SimpanPetugasActionPerformed(evt);
            }
        });
        jPanel3.add(jButton_SimpanPetugas);
        jButton_SimpanPetugas.setBounds(260, 90, 90, 40);

        jButton_BaruPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/new.png"))); // NOI18N
        jButton_BaruPetugas.setText("Baru");
        jButton_BaruPetugas.setToolTipText("Input Data Baru");
        jButton_BaruPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_BaruPetugasActionPerformed(evt);
            }
        });
        jPanel3.add(jButton_BaruPetugas);
        jButton_BaruPetugas.setBounds(380, 90, 90, 40);

        jButton_UbahPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/edit.png"))); // NOI18N
        jButton_UbahPetugas.setText("Ubah");
        jButton_UbahPetugas.setToolTipText("Ubah Data Petugas");
        jButton_UbahPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_UbahPetugasActionPerformed(evt);
            }
        });
        jPanel3.add(jButton_UbahPetugas);
        jButton_UbahPetugas.setBounds(510, 90, 90, 40);

        jTable_TampilPetugas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nama Depan", "Nama Belakang"
            }
        ));
        jTable_TampilPetugas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_TampilPetugasMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTable_TampilPetugas);

        jPanel3.add(jScrollPane6);
        jScrollPane6.setBounds(30, 170, 700, 310);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/back.png"))); // NOI18N
        jButton1.setText("Kembal ke Input Pengiriman");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);
        jButton1.setBounds(260, 530, 220, 30);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/delete.png"))); // NOI18N
        jButton2.setText("Hapus");
        jButton2.setToolTipText("Hapus Data Petugas");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2);
        jButton2.setBounds(630, 90, 90, 40);

        jTextField_FilterPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_FilterPetugasKeyReleased(evt);
            }
        });
        jPanel3.add(jTextField_FilterPetugas);
        jTextField_FilterPetugas.setBounds(290, 490, 150, 20);

        jLabel13.setText("Search");
        jPanel3.add(jLabel13);
        jLabel13.setBounds(240, 490, 50, 14);

        jTabbedPane1.addTab("Data Petugas", jPanel3);

        jPanel1.setBackground(new java.awt.Color(255, 204, 51));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(204, 0, 0));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setLayout(null);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/img1.png"))); // NOI18N
        jPanel2.add(jLabel9);
        jLabel9.setBounds(50, 10, 100, 50);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/img2.jpg"))); // NOI18N
        jPanel2.add(jLabel3);
        jLabel3.setBounds(170, 10, 100, 50);

        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/img3.jpg"))); // NOI18N
        jLabel34.setText("jLabel34");
        jPanel2.add(jLabel34);
        jLabel34.setBounds(290, 10, 90, 50);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(370, 20, 410, 70);

        jLabel17.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 0, 51));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/logoform.png"))); // NOI18N
        jPanel1.add(jLabel17);
        jLabel17.setBounds(10, 0, 210, 70);

        jLabel24.setText("Selamat Datang ");
        jPanel1.add(jLabel24);
        jLabel24.setBounds(380, 0, 100, 20);
        jPanel1.add(jLabel_welcome);
        jLabel_welcome.setBounds(480, 0, 60, 20);

        jLabel_Date.setText("Date");
        jPanel1.add(jLabel_Date);
        jLabel_Date.setBounds(10, 70, 100, 14);

        jLabel_Time.setText("Time");
        jPanel1.add(jLabel_Time);
        jLabel_Time.setBounds(140, 70, 100, 14);

        jLabel_LogOut.setForeground(new java.awt.Color(0, 153, 204));
        jLabel_LogOut.setText("Log Out");
        jLabel_LogOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_LogOutMouseClicked(evt);
            }
        });
        jPanel1.add(jLabel_LogOut);
        jLabel_LogOut.setBounds(730, 0, 50, 20);

        jMenu1.setText("File");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/new.png"))); // NOI18N
        jMenuItem1.setText("Baru");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/save.png"))); // NOI18N
        jMenuItem5.setText("Simpan");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);
        jMenu1.add(jSeparator1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Close");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/edit.png"))); // NOI18N
        jMenuItem3.setText("Ubah");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desainekspedisi/delete.png"))); // NOI18N
        jMenuItem4.setText("Hapus");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        jMenu5.setText("Help");
        jMenu5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu5ActionPerformed(evt);
            }
        });

        jMenuItem6.setText("Laporkan Bug");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem6);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 605, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_SimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SimpanActionPerformed
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
     String d = sdf.format(jDateChooser_Tanggal.getDate());
        // memasukan data baru
        String query = "INSERT INTO `tb_pengirimanbarang`(`noresi`, `tanggal`, `petugas`, `kotatujuan`, `namapengirim`, `alamatpengirim`, `kodepospengirim`, `noteleponpengirim`, `namapenerima`, `alamatpenerima`, `kodepospenerima`, `noteleponpenerima`, `isibarang`, `instruksi`, `berat`, `jenispaket`, `total`) "
                + "VALUES ('" + jTextField_NoResi.getText() + "','" + d + "','" + jComboBox_Petugas.getSelectedItem() + "','" + jComboBox_KotaTujuan.getSelectedItem().toString() + "','" + jTextField_NamaPengirim.getText() + "','" + jTextArea_AlamatPengirim.getText() + "','" + jTextField_KodePosPengirim.getText() + "','" + jTextField_NoTelpPengirim.getText() + "',"
                + "'" + jTextField_NamaPenerima.getText() + "','" + jTextArea_AlamatPenerima.getText() + "','" + jTextField_KodePosPenerima.getText() + "','" + jTextField_NoTelpPenerima.getText() + "','" + jTextField_Deskripsi.getText() + "','" + jTextField_Instruksi.getText() + "','" + jTextField_Berat.getText() + "','" + jComboBox_JenisPaket.getSelectedItem().toString() + "','" + jTextField_Total.getText() + "')";
        ExecuteSQLQuery(query, "masukan");
        jTabbedPane1.setSelectedIndex(1);
        
    }//GEN-LAST:event_jButton_SimpanActionPerformed

    private void jButton_BaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BaruActionPerformed
        jButton_Simpan.setEnabled(true);
        jTextField_NoResi.setEditable(true);
// membuat clear form/ membuat baru
        jTextField_NoResi.setText("");
        jDateChooser_Tanggal.setDate(null);
        //jTextField_NamaPetugas.setText("");
        jComboBox_KotaTujuan.setSelectedIndex(0);
        jTextField_NamaPengirim.setText("");
        jTextArea_AlamatPengirim.setText("");
        jTextField_KodePosPengirim.setText("");
        jTextField_NoTelpPengirim.setText("");
        jTextField_NamaPenerima.setText("");
        jTextArea_AlamatPenerima.setText("");
        jTextField_KodePosPenerima.setText("");
        jTextField_NoTelpPenerima.setText("");
        jTextField_Deskripsi.setText("");
        jTextField_Instruksi.setText("");
        jTextField_Berat.setText("0");
        jComboBox_JenisPaket.setSelectedIndex(0);
        jTextField_Total.setText("");
    }//GEN-LAST:event_jButton_BaruActionPerformed

    private void jButton_UbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_UbahActionPerformed
        // update data yang sudah ada
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
     String d = sdf.format(jDateChooser_Tanggal.getDate());
        jButton_Simpan.setEnabled(true);
        String query = "UPDATE `tb_pengirimanbarang` SET `tanggal`='" + d + "', `petugas`='" + jComboBox_Petugas.getSelectedItem() + "', `kotatujuan`='" + jComboBox_KotaTujuan.getSelectedItem().toString() + "', `namapengirim`='" + jTextField_NamaPengirim.getText() + "', `alamatpengirim`='" + jTextArea_AlamatPengirim.getText() + "', `kodepospengirim`='" + jTextField_KodePosPengirim.getText() + "', `noteleponpengirim`='" + jTextField_NoTelpPengirim.getText() + "', `namapenerima`='" + jTextField_NamaPenerima.getText() + "', `alamatpenerima`='" + jTextArea_AlamatPenerima.getText() + "', `kodepospenerima`='" + jTextField_KodePosPenerima.getText() + "', `noteleponpenerima`='" + jTextField_NoTelpPenerima.getText() + "', `isibarang`='" + jTextField_Deskripsi.getText() + "', `instruksi`='" + jTextField_Instruksi.getText() + "', `berat`='" + jTextField_Berat.getText() + "', `jenispaket`='" + jComboBox_JenisPaket.getSelectedItem().toString() + "', `total`='" + jTextField_Total.getText() + "' WHERE `noresi` = " + jTextField_NoResi.getText();
        ExecuteSQLQuery(query, "Updated");
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton_UbahActionPerformed

    private void jTextField_DeskripsiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_DeskripsiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_DeskripsiActionPerformed

    private void jTextField_TotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_TotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_TotalActionPerformed

    private void jComboBox_JenisPaketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_JenisPaketActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_JenisPaketActionPerformed

    private void jTextField_InstruksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_InstruksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_InstruksiActionPerformed

    private void jTextField_BeratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_BeratActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_BeratActionPerformed

    private void jTextField_NamaPenerimaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_NamaPenerimaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_NamaPenerimaActionPerformed

    private void jTable_TampilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_TampilMouseClicked
        jTextField_NoResi.setEditable(false);
        // menampilkan row pilihan di jtextfield
        
        int i = jTable_Tampil.getSelectedRow();
        TableModel model = jTable_Tampil.getModel();
        
        jTextField_NoResi.setText(model.getValueAt(i, 0).toString());
        
        String s=(String)jTable_Tampil.getModel().getValueAt(i, 1);
        try{
        SimpleDateFormat f=new SimpleDateFormat("yyyy-M-dd"); 
        java.util.Date d=f.parse(s);
        jDateChooser_Tanggal.setDate(d);
        }catch(Exception ex){
        ex.printStackTrace();
        }  
        
        //jTextField_NamaPetugas.setText(model.getValueAt(i, 2).toString());
        
     
        String kotatujuan1 = model.getValueAt(i,3).toString();
            switch(kotatujuan1){
                case "Jakarta":
                    jComboBox_KotaTujuan.setSelectedIndex(0);
                break;
                case "Bandung":
                    jComboBox_KotaTujuan.setSelectedIndex(1);
                break;
                case "Karawang":
                    jComboBox_KotaTujuan.setSelectedIndex(2);
                break;
                case "Purwakarta":
                    jComboBox_KotaTujuan.setSelectedIndex(3);
                break;
                case "Sukabumi":
                    jComboBox_KotaTujuan.setSelectedIndex(4);
                break;
                case "Cianjur":
                    jComboBox_KotaTujuan.setSelectedIndex(5);
                break;
                case "Depok":
                    jComboBox_KotaTujuan.setSelectedIndex(6);
                break;
                case "Tangerang":
                    jComboBox_KotaTujuan.setSelectedIndex(7);
                break;
                case "Cilegon":
                    jComboBox_KotaTujuan.setSelectedIndex(8);
                break;
                case "Bekasi":
                    jComboBox_KotaTujuan.setSelectedIndex(9);
                break;
                case "Bogor":
                    jComboBox_KotaTujuan.setSelectedIndex(10);
                break;
                case "Cimahi":
                    jComboBox_KotaTujuan.setSelectedIndex(11);
                break;
                case "Serang":
                    jComboBox_KotaTujuan.setSelectedIndex(12);
                break;
                case "Banjar":
                    jComboBox_KotaTujuan.setSelectedIndex(13);
                break;
                case "Cirebon":
                    jComboBox_KotaTujuan.setSelectedIndex(14);
                break;
                case "Semarang":
                    jComboBox_KotaTujuan.setSelectedIndex(15);
                break;
                case "Magelang":
                    jComboBox_KotaTujuan.setSelectedIndex(16);
                break;
                case "Kebumen":
                    jComboBox_KotaTujuan.setSelectedIndex(17);
                break;
                case "Pekalongan":
                    jComboBox_KotaTujuan.setSelectedIndex(18);
                break;
                case "Purworejo":
                    jComboBox_KotaTujuan.setSelectedIndex(19);
                break;
                case "Cilacap":
                    jComboBox_KotaTujuan.setSelectedIndex(20);
                break;
                case "Brebes":
                    jComboBox_KotaTujuan.setSelectedIndex(21);
                break;
                case "Wonogiri":
                    jComboBox_KotaTujuan.setSelectedIndex(22);
                break;
                case "Tegal":
                    jComboBox_KotaTujuan.setSelectedIndex(23);
                break;
                case "Banyumas":
                    jComboBox_KotaTujuan.setSelectedIndex(24);
                break;
                case "Pemalang":
                    jComboBox_KotaTujuan.setSelectedIndex(25);
                break;
                case "Kudus":
                    jComboBox_KotaTujuan.setSelectedIndex(26);
                break;
                case "Salatiga":
                    jComboBox_KotaTujuan.setSelectedIndex(27);
                break;
                case "Surakarta":
                    jComboBox_KotaTujuan.setSelectedIndex(28);
                break;
                case "Yogyakarta":
                    jComboBox_KotaTujuan.setSelectedIndex(29);
                break;
                case "Bantul":
                    jComboBox_KotaTujuan.setSelectedIndex(30);
                break;
                case "Malang":
                    jComboBox_KotaTujuan.setSelectedIndex(31);
                break;
                case "Surabaya":
                    jComboBox_KotaTujuan.setSelectedIndex(32);
                break;
                case "Gresik":
                    jComboBox_KotaTujuan.setSelectedIndex(33);
                break;
                case "Sidoarjo":
                    jComboBox_KotaTujuan.setSelectedIndex(34);
                break;
                case "Jombang":
                    jComboBox_KotaTujuan.setSelectedIndex(35);
                break;
                case "Lamongan":
                    jComboBox_KotaTujuan.setSelectedIndex(36);
                break;
                case "Ngawi":
                    jComboBox_KotaTujuan.setSelectedIndex(37);
                break;
                case "Pacitan":
                    jComboBox_KotaTujuan.setSelectedIndex(38);
                break;
                case "Madiun":
                    jComboBox_KotaTujuan.setSelectedIndex(39);
                break;
                case "Probolinggo":
                    jComboBox_KotaTujuan.setSelectedIndex(40);
                break;
                case "Kediri":
                    jComboBox_KotaTujuan.setSelectedIndex(41);
                break;
                case "Mojokerto":
                    jComboBox_KotaTujuan.setSelectedIndex(42);
                break;
                case "Banyuwangi":
                    jComboBox_KotaTujuan.setSelectedIndex(43);
                break;
                case "Bondowoso":
                    jComboBox_KotaTujuan.setSelectedIndex(44);
                break;
                case "Nganjuk":
                    jComboBox_KotaTujuan.setSelectedIndex(45);
                break;
            }
        jTextField_NamaPengirim.setText(model.getValueAt(i, 4).toString());
        jTextArea_AlamatPengirim.setText(model.getValueAt(i, 5).toString());
        jTextField_KodePosPengirim.setText(model.getValueAt(i, 6).toString());
        jTextField_NoTelpPengirim.setText(model.getValueAt(i, 7).toString());
        jTextField_NamaPenerima.setText(model.getValueAt(i, 8).toString());
        jTextArea_AlamatPenerima.setText(model.getValueAt(i, 9).toString());
        jTextField_KodePosPenerima.setText(model.getValueAt(i, 10).toString());
        jTextField_NoTelpPenerima.setText(model.getValueAt(i, 11).toString());
        jTextField_Deskripsi.setText(model.getValueAt(i, 12).toString());
        jTextField_Instruksi.setText(model.getValueAt(i, 13).toString());
        jTextField_Berat.setText(model.getValueAt(i, 14).toString());
        String jenispaket1 = model.getValueAt(i,15).toString();
            switch(jenispaket1){
                case "Normal":
                    jComboBox_JenisPaket.setSelectedIndex(0);
                break;
                case "Ekspres":
                    jComboBox_JenisPaket.setSelectedIndex(1);
                break;
                case "Ekonomis":
                    jComboBox_JenisPaket.setSelectedIndex(2);
                break;
            }
        jTextField_Total.setText(model.getValueAt(i, 16).toString());
    }//GEN-LAST:event_jTable_TampilMouseClicked

    private void jButton_HapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_HapusActionPerformed
        // Menghapus data
        
        int row = jTable_Tampil.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Silahkan terlebih dahulu pilih data yang ingin di hapus di table");
        } else {
            int p = JOptionPane.showConfirmDialog(null, "Apakah Anda Yakin Ingin Menghapus Data?","Hapus",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(p==0){
            String query = "DELETE FROM `tb_pengirimanbarang` WHERE noresi =" + jTextField_NoResi.getText();
            ExecuteSQLQuery(query, "Hapus");
            jButton_Baru.doClick();
            }
            jButton_Baru.doClick();
        }
    }//GEN-LAST:event_jButton_HapusActionPerformed

    private void jButton_HitungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_HitungActionPerformed
        // menghitung total jumlah yang harus dibayar
        int newkotatujuan = 0;
        String kotatujuan = jComboBox_KotaTujuan.getSelectedItem().toString();
        switch (kotatujuan) {
            case "Jakarta":
            case "Bandung":
            case "Karawang":
            case "Purwakarta":
            case "Sukabumi":
            case "Cianjur":
            case "Depok":
            case "Tangerang":
            case "Cilegon":
            case "Bekasi":  
            case "Bogor":
            case "Cimahi":
            case "Serang":
            case "Banjar":
            case "Cirebon":    
                newkotatujuan = 5000;
                break;
            case "Semarang":
            case "Magelang":
            case "Kebumen":
            case "Pekalongan":
            case "Purworejo":
            case "Cilacap":
            case "Brebes":
            case "Wonogiri":
            case "Tegal":
            case "Banyumas":
            case "Pemalang":
            case "Kudus":  
            case "Salatiga":
            case "Surakarta":
            case "Yogyakarta":
            case "Bantul":
                newkotatujuan = 10000;
                break;
            case "Malang":
            case "Surabaya":
            case "Gresik":
            case "Sidoarjo":
            case "Jombang":
            case "Lamongan":
            case "Ngawi":
            case "Pacitan":
            case "Madiun":
            case "Probolinggo":
            case "Kediri":
            case "Mojokerto":
            case "Banyuwangi":
            case "Bondowoso":
            case "Nganjuk":    
                newkotatujuan = 15000;
                break;
            default:
                break;
        }
        
        int newberat = 0;
        int berat = Integer.parseInt(jTextField_Berat.getText());
        if (berat > 1) {
            newberat = berat * 1000;
        } else {
            newberat = 1000;
        }

        int newjenis = 0;
        String jenis = jComboBox_JenisPaket.getSelectedItem().toString();
        switch (jenis) {
            case "Normal":
                newjenis = 3000;
                break;
            case "Ekspres":
                newjenis = 7000;
                break;
            case "Ekonomis":
                newjenis = 2000;
                break;
            default:
                break;
        }

        int total = (newkotatujuan + newberat + newjenis);
        jTextField_Total.setText(String.valueOf(total));
    }//GEN-LAST:event_jButton_HitungActionPerformed

    private void jTextField_NoResiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_NoResiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_NoResiActionPerformed

    private void jButton_Ubah2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Ubah2ActionPerformed
        // kembali ke tab 1
        int row = jTable_Tampil.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Silahkan  terlebih dahulu pilih data yang ingin di ubah di table");
        } else {
            jButton_Simpan.setEnabled(false);
            jTextField_NoResi.setEditable(false);
            jTabbedPane1.setSelectedIndex(0);
            
        }

    }//GEN-LAST:event_jButton_Ubah2ActionPerformed

    private void jLabel_LogOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_LogOutMouseClicked
        // untuk logout kembali ke login.java
        Login lgn = new Login();
        lgn.setVisible(true);
        lgn.pack();
        lgn.setLocationRelativeTo(null);
        lgn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.dispose();
    }//GEN-LAST:event_jLabel_LogOutMouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        jButton_Baru.doClick();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jTextField_NamaDepanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_NamaDepanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_NamaDepanActionPerformed

    private void jTextField_IDPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_IDPetugasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_IDPetugasActionPerformed

    private void jTextField_NamaBelakangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_NamaBelakangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_NamaBelakangActionPerformed

    private void jButton_SimpanPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SimpanPetugasActionPerformed
        // memasukan data baru
        
        String query = "INSERT INTO `tb_petugas`(`idpetugas`, `namadepan`, `namabelakang`) "
                + "VALUES ('" + jTextField_IDPetugas.getText() + "','" + jTextField_NamaDepan.getText() + "','" + jTextField_NamaBelakang.getText() + "')";
        ExecuteSQLQuery2(query, "masukan");
        jTable_TampilPetugas.repaint();
        jComboBox_Petugas.removeAllItems();
        FillCombo();
    }//GEN-LAST:event_jButton_SimpanPetugasActionPerformed

    private void jButton_BaruPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BaruPetugasActionPerformed
        // TODO add your handling code here:
        jTextField_IDPetugas.setEditable(true);
        jTextField_IDPetugas.setText("");
        jTextField_NamaDepan.setText("");
        jTextField_NamaBelakang.setText("");
    }//GEN-LAST:event_jButton_BaruPetugasActionPerformed

    private void jButton_UbahPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_UbahPetugasActionPerformed
        // TODO add your handling code here:
        int row = jTable_TampilPetugas.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Silahkan  terlebih dahulu pilih data yang ingin di ubah di table");
        } else {
            String query = "UPDATE `tb_petugas` SET `namadepan`='"+ jTextField_NamaDepan.getText() + "',`namabelakang`='"+ jTextField_NamaBelakang.getText()+ "' WHERE `idpetugas` = " + jTextField_IDPetugas.getText();
            ExecuteSQLQuery2(query, "Updated");
            jComboBox_Petugas.removeAllItems();
            //DefaultTableModel model = (DefaultTableModel) jTable_TampilPetugas.getModel();
            //model.setRowCount(0);
            //Show_Petugas_In_JTable();
            FillCombo();
        }
    }//GEN-LAST:event_jButton_UbahPetugasActionPerformed

    private void jButton_TentangBeratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_TentangBeratActionPerformed
        
        InfoBerat s = new InfoBerat();
        s.setVisible(true);
        
    }//GEN-LAST:event_jButton_TentangBeratActionPerformed

    private void jTextField_FilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_FilterKeyReleased
        String txt = jTextField_Filter.getText();
        DefaultTableModel model;
        model = (DefaultTableModel)jTable_Tampil.getModel();
        TableRowSorter<DefaultTableModel>tr = new TableRowSorter
                <DefaultTableModel>(model);
        jTable_Tampil.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(txt));
    }//GEN-LAST:event_jTextField_FilterKeyReleased

    private void jTable_TampilPetugasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_TampilPetugasMouseClicked
        // TODO add your handling code here:
        jTextField_IDPetugas.setEditable(false);
        int i = jTable_TampilPetugas.getSelectedRow();
        TableModel model = jTable_TampilPetugas.getModel();
        jTextField_IDPetugas.setText(model.getValueAt(i, 0).toString());
        jTextField_NamaDepan.setText(model.getValueAt(i, 1).toString());
        jTextField_NamaBelakang.setText(model.getValueAt(i, 2).toString());
    }//GEN-LAST:event_jTable_TampilPetugasMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel_RegisterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_RegisterMouseClicked
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jLabel_RegisterMouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        Exit();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
       jButton_Simpan.doClick();
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
          int row = jTable_TampilPetugas.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Silahkan terlebih dahulu pilih data yang ingin di hapus di table");
        } else {
            int p = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Menghapus Data?","Hapus",JOptionPane.YES_NO_OPTION);
            if(p==0){
            String query = "DELETE FROM `tb_petugas` WHERE idpetugas =" + jTextField_IDPetugas.getText();
            ExecuteSQLQuery2(query, "Hapus");
        jComboBox_Petugas.removeAllItems();
        FillCombo();
        jTextField_IDPetugas.setEditable(true);
        jTextField_IDPetugas.setText("");
        jTextField_NamaDepan.setText("");
        jTextField_NamaBelakang.setText("");
            }
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton_TentangJenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_TentangJenisActionPerformed
        // TODO add your handling code here:
        InfoJenis z = new InfoJenis();
        z.setVisible(true);
    }//GEN-LAST:event_jButton_TentangJenisActionPerformed

    private void jTextField_FilterPetugasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_FilterPetugasKeyReleased
        String txt = jTextField_FilterPetugas.getText();
        filter2(txt);
    }//GEN-LAST:event_jTextField_FilterPetugasKeyReleased

    private void jButton_BuatLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BuatLaporanActionPerformed
        int bulan;
        bulan = jMonthChooser1.getMonth();
        bulan +=1;
        
        int tahun;
        tahun = jYearChooser1.getYear();
        
        try{
            //String report="C:\\Users\\rxxxx\\Documents\\NetBeansProjects\\desainekspedisi\\report1.jrxml";
            JasperDesign jd=JRXmlLoader.load("C:\\Users\\rxxxx\\Documents\\NetBeansProjects\\desainekspedisi\\report1.jrxml");
            String sql="SELECT * FROM `tb_pengirimanbarang` WHERE MONTH(tanggal) = "+bulan+" AND YEAR(tanggal) = "+tahun;
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(sql);
            jd.setQuery(newQuery);
            JasperReport jr=JasperCompileManager.compileReport(jd);
            JasperPrint jp=JasperFillManager.fillReport(jr, null, conn);
            JasperViewer.viewReport(jp, false);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_jButton_BuatLaporanActionPerformed

    private void jTextField_FilterNoResiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_FilterNoResiKeyReleased
        String txt = jTextField_FilterNoResi.getText();
        DefaultTableModel model;
        model = (DefaultTableModel)jTable_Tampil.getModel();
        TableRowSorter<DefaultTableModel>tr = new TableRowSorter
                <DefaultTableModel>(model);
        jTable_Tampil.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(txt,0));
    }//GEN-LAST:event_jTextField_FilterNoResiKeyReleased

    private void jTextField_FilterNamaPengirimKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_FilterNamaPengirimKeyReleased
        String txt = jTextField_FilterNamaPengirim.getText();
        DefaultTableModel model;
        model = (DefaultTableModel)jTable_Tampil.getModel();
        TableRowSorter<DefaultTableModel>tr = new TableRowSorter
                <DefaultTableModel>(model);
        jTable_Tampil.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(txt,4));
    }//GEN-LAST:event_jTextField_FilterNamaPengirimKeyReleased

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        jButton_LaporkanBug.doClick();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu5ActionPerformed
        
       
    }//GEN-LAST:event_jMenu5ActionPerformed

    private void jButton_LaporkanBugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LaporkanBugActionPerformed
        ReportMail r = new ReportMail();
        r.setVisible(true);
    }//GEN-LAST:event_jButton_LaporkanBugActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        jButton_Simpan.doClick();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
        jButton_Ubah2.doClick();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
        jButton_Hapus.doClick();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(formmaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formmaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formmaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formmaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formmaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton_Baru;
    private javax.swing.JButton jButton_BaruPetugas;
    private javax.swing.JButton jButton_BuatLaporan;
    private javax.swing.JButton jButton_Hapus;
    private javax.swing.JButton jButton_Hitung;
    private javax.swing.JButton jButton_LaporkanBug;
    private javax.swing.JButton jButton_Simpan;
    private javax.swing.JButton jButton_SimpanPetugas;
    private javax.swing.JButton jButton_TentangBerat;
    private javax.swing.JButton jButton_TentangJenis;
    private javax.swing.JButton jButton_Ubah;
    private javax.swing.JButton jButton_Ubah2;
    private javax.swing.JButton jButton_UbahPetugas;
    private javax.swing.JComboBox<String> jComboBox_JenisPaket;
    private javax.swing.JComboBox<String> jComboBox_KotaTujuan;
    private javax.swing.JComboBox<String> jComboBox_Petugas;
    private com.toedter.calendar.JDateChooser jDateChooser_Tanggal;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_Date;
    private javax.swing.JLabel jLabel_LogOut;
    private javax.swing.JLabel jLabel_Register;
    private javax.swing.JLabel jLabel_Time;
    public static javax.swing.JLabel jLabel_welcome;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private com.toedter.calendar.JMonthChooser jMonthChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable_Tampil;
    private javax.swing.JTable jTable_TampilPetugas;
    private javax.swing.JTextArea jTextArea_AlamatPenerima;
    private javax.swing.JTextArea jTextArea_AlamatPengirim;
    private javax.swing.JTextField jTextField_Berat;
    private javax.swing.JTextField jTextField_Deskripsi;
    private javax.swing.JTextField jTextField_Filter;
    private javax.swing.JTextField jTextField_FilterNamaPengirim;
    private javax.swing.JTextField jTextField_FilterNoResi;
    private javax.swing.JTextField jTextField_FilterPetugas;
    private javax.swing.JTextField jTextField_IDPetugas;
    private javax.swing.JTextField jTextField_Instruksi;
    private javax.swing.JTextField jTextField_KodePosPenerima;
    private javax.swing.JTextField jTextField_KodePosPengirim;
    private javax.swing.JTextField jTextField_NamaBelakang;
    private javax.swing.JTextField jTextField_NamaDepan;
    private javax.swing.JTextField jTextField_NamaPenerima;
    private javax.swing.JTextField jTextField_NamaPengirim;
    private javax.swing.JTextField jTextField_NoResi;
    private javax.swing.JTextField jTextField_NoTelpPenerima;
    private javax.swing.JTextField jTextField_NoTelpPengirim;
    private javax.swing.JTextField jTextField_Total;
    private com.toedter.calendar.JYearChooser jYearChooser1;
    // End of variables declaration//GEN-END:variables
}
