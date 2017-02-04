/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import db.DbQueries;
import entities.ContractorEntity;
import entities.DocEntity;
import entities.DocProductEntity;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Math.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import tableTemplates.ProductOnDocumentTableTemplate;
import tableTemplates.wzTableTemplate;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import utilities.Footer;
import static utilities.Other.getScreenWidth;
import utilities.StampPageXofY;
import static utilities.TimeFunctions.timestampToLong;
import static utilities.TimeFunctions.nowTimestamp;
import static utilities.amountInW.amountInWords;
import utilities.companyAdress;
import utilities.myRenderer;

/**
 *
 * @author Marek
 */
public class wzListWindow extends javax.swing.JFrame {
    public MainWindow parentFrame;
    public int newWzContracorId;
    int selectedDocId =0;
    float snetto = 0;
    int setNowModelCounter = 0;
    Long tmpTime;
    Long tempName;
    
    wzTableTemplate dtm = new wzTableTemplate();
    DbQueries wz = new DbQueries();
    public List<DocEntity> toShow = wz.getWZDocs();
    List<DocProductEntity> productToShow = new ArrayList<>();
    public wzListWindow() {
        initComponents();
        this.setExtendedState( this.getExtendedState()|JFrame.MAXIMIZED_BOTH);
        drawTable(toShow);
        drawProductTable(productToShow);

        ListSelectionModel selectionModel = WZTable.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                refreshWindow();
            }
        });
        if(!toShow.isEmpty()){
        WZTable.changeSelection(0, 0, false, false);
        }
    }
    public void refreshWindow(){
        long close = timestampToLong(Timestamp.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(), 5).toString()))-86400000;
                long delay = timestampToLong(Timestamp.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(), 5).toString()));
                if((nowTimestamp() > delay && (!"SPRZĘT WYDANY".equals(WZTable.getValueAt(WZTable.getSelectedRow(), 11).toString()) && !"TWORZENIE DOKUMENTU".equals(WZTable.getValueAt(WZTable.getSelectedRow(), 11).toString())))){
                    terminComm.setText("TERMIN PRZEKROCZONY !");
                    terminComm.setForeground(Color.red);
                    
                }
                else if((nowTimestamp() > close && (!"SPRZĘT WYDANY".equals(WZTable.getValueAt(WZTable.getSelectedRow(), 11).toString()) && !"TWORZENIE DOKUMENTU".equals(WZTable.getValueAt(WZTable.getSelectedRow(), 11).toString())))) {
                    terminComm.setText("ZBLIŻA SIĘ TERMIN ODBIORU");
                    terminComm.setForeground(Color.orange);
                } else {
                    terminComm.setText("");
                }
                if("TWORZENIE DOKUMENTU".equals(WZTable.getValueAt(WZTable.getSelectedRow(), 11).toString())){
                    printDoc.hide();
                    accDoc.show();
                    delWZBtt.show();
                    endDoc.hide();
                    openWZBtt.show();
                }
                if("SPRZĘT PRZYJĘTY".equals(WZTable.getValueAt(WZTable.getSelectedRow(), 11).toString())){
                    printDoc.show();
                    accDoc.hide();
                    delWZBtt.hide();
                    endDoc.show();
                    openWZBtt.show();
                }
                if("SPRZĘT WYDANY".equals(WZTable.getValueAt(WZTable.getSelectedRow(), 11).toString())){
                    printDoc.show();
                    accDoc.hide();
                    delWZBtt.hide();
                    endDoc.hide();
                    openWZBtt.show();
                }
                if("GOTOWY DO WYDANIA".equals(WZTable.getValueAt(WZTable.getSelectedRow(), 11).toString())){
                    printDoc.hide();
                    accDoc.show();
                    delWZBtt.show();
                    endDoc.hide();
                    openWZBtt.show();
                }
                selectedDocId = Integer.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(),0).toString());
                productToShow = wz.getDocProducts(selectedDocId);
                contractorNameLabel.setText(WZTable.getValueAt(WZTable.getSelectedRow(),3).toString());
                drawProductTable(productToShow);
    }
    
    public void drawTable(List<DocEntity> docList){
        if(setNowModelCounter == 0){
        WZTable.setModel(dtm);
        setNowModelCounter++;
        }
        WZTable.setDefaultRenderer(Object.class, new myRenderer());
        //SET SIZE OF TABLE 6PX LESS THAN SCREEN RESOLUTION
        WZTable.setSize(getScreenWidth()-21, 300);
        WZTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int WZTableWidth = WZTable.getWidth();
        WZTable.getColumnModel().getColumn(0).setPreferredWidth((int)round(WZTableWidth*0.05));
        WZTable.getColumnModel().getColumn(1).setPreferredWidth((int)round(WZTableWidth*0.04));
        WZTable.getColumnModel().getColumn(2).setPreferredWidth((int)round(WZTableWidth*0.04));
        WZTable.getColumnModel().getColumn(3).setPreferredWidth((int)round(WZTableWidth*0.17));
        WZTable.getColumnModel().getColumn(4).setPreferredWidth((int)round(WZTableWidth*0.10));
        WZTable.getColumnModel().getColumn(5).setPreferredWidth((int)round(WZTableWidth*0.10));
        WZTable.getColumnModel().getColumn(6).setPreferredWidth((int)round(WZTableWidth*0.10));
        WZTable.getColumnModel().getColumn(7).setPreferredWidth((int)round(WZTableWidth*0.05));
        WZTable.getColumnModel().getColumn(8).setPreferredWidth((int)round(WZTableWidth*0.10));
        WZTable.getColumnModel().getColumn(9).setPreferredWidth((int)round(WZTableWidth*0.05));
        WZTable.getColumnModel().getColumn(10).setPreferredWidth((int)round(WZTableWidth*0.05));
        WZTable.getColumnModel().getColumn(11).setPreferredWidth((int)round(WZTableWidth*0.12));
        WZTable.getColumnModel().getColumn(12).setPreferredWidth((int)round(WZTableWidth*0.03));
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //INPUT DATA INTO TABLE
        dtm.setRowCount(docList.size());    
        for(int i = 0; i < docList.size(); i++){
              WZTable.getModel().setValueAt(docList.get(i).getId(), i, 0);
              WZTable.getModel().setValueAt(docList.get(i).getDocNumber(), i, 1);
              WZTable.getModel().setValueAt(docList.get(i).getDocContractorId(), i, 2);
              WZTable.getModel().setValueAt(docList.get(i).getDocContractorName(), i, 3);
              WZTable.getModel().setValueAt(dt.format(docList.get(i).getDocLeavingDate()), i, 4);
              WZTable.getModel().setValueAt(dt.format(docList.get(i).getDocRepairDate()), i, 5);
              tmpTime = timestampToLong(docList.get(i).getDocReceiptDate());
              if(tmpTime.intValue() != 0){
                    WZTable.getModel().setValueAt(dt.format(docList.get(i).getDocReceiptDate()), i, 6);
              }else{
                    WZTable.getModel().setValueAt("---", i, 6);
              }
              if(!"".equals(docList.get(i).getDocFvatNumber())){
                    WZTable.getModel().setValueAt(docList.get(i).getDocFvatNumber(), i, 7);
              }else{
                    WZTable.getModel().setValueAt("---", i, 7);
              }
              tmpTime = timestampToLong(docList.get(i).getDocFvatDate());
              if(tmpTime.intValue() != 0){
                    WZTable.getModel().setValueAt(dt.format(docList.get(i).getDocFvatDate()), i, 8);
              }else{
                    WZTable.getModel().setValueAt("---", i, 8);
              }
              if(docList.get(i).getDocSesin() != 0){
                    WZTable.getModel().setValueAt(docList.get(i).getDocSesin(), i, 9);
              }else{
                    WZTable.getModel().setValueAt("---", i, 9);
              }
              if(docList.get(i).getDocOpti() != 0){
                    WZTable.getModel().setValueAt(docList.get(i).getDocOpti(), i, 10);
              }else{
                    WZTable.getModel().setValueAt("---", i, 10);
              }
              WZTable.getModel().setValueAt(docList.get(i).getDocStatus(), i, 11);
              if(docList.get(i).getDocType() == 1){
                WZTable.getModel().setValueAt("PS",i,12);
              }else if(docList.get(i).getDocType() == 2){
                WZTable.getModel().setValueAt("WS",i,12);  
              }
        }  
    }
    public void drawProductTable(List<DocProductEntity> prodList){
        ProductOnDocumentTableTemplate dtm = new ProductOnDocumentTableTemplate();
        productTable.setModel(dtm);
        
        //INITIALIZE RIGHT RENDERER
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);        
        //INITIALIZE CENTER RENDERER
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER); 


        productTable.setSize(getScreenWidth()-22, 300);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int prodTableWidth = productTable.getWidth();
        
        productTable.getColumnModel().getColumn(0).setPreferredWidth((int)round(prodTableWidth*0.05));
        productTable.getColumnModel().getColumn(1).setPreferredWidth((int)round(prodTableWidth*0.10));
        productTable.getColumnModel().getColumn(2).setPreferredWidth((int)round(prodTableWidth*0.10));
        productTable.getColumnModel().getColumn(3).setPreferredWidth((int)round(prodTableWidth*0.10));
        productTable.getColumnModel().getColumn(4).setPreferredWidth((int)round(prodTableWidth*0.25));
        productTable.getColumnModel().getColumn(5).setPreferredWidth((int)round(prodTableWidth*0.25));
        productTable.getColumnModel().getColumn(6).setPreferredWidth((int)round(prodTableWidth*0.15));
        //CHANGE COLUMN ALIGMENT
        productTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        productTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);


        
        
        DecimalFormat dc = new DecimalFormat("0.00");
        dtm.setRowCount(prodList.size());    
        for(int i = 0; i < prodList.size(); i++){
                productTable.getModel().setValueAt(prodList.get(i).getId(), i, 0);
                productTable.getModel().setValueAt(prodList.get(i).getName(), i, 1);
                productTable.getModel().setValueAt(prodList.get(i).getSerial(), i, 2);
                productTable.getModel().setValueAt(prodList.get(i).getPrice(), i, 3);
                snetto += prodList.get(i).getPrice();
                productTable.getModel().setValueAt(prodList.get(i).getProblem(), i, 4);
                productTable.getModel().setValueAt(prodList.get(i).getRepair(), i, 5);
                productTable.getModel().setValueAt(prodList.get(i).getPlace(), i, 6);
        }  
        nettoLabel.setText(String.valueOf(dc.format(snetto)));
        bruttoLabel.setText(String.valueOf(dc.format(snetto+snetto*0.23)));
        snetto = 0;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        WZTable = new javax.swing.JTable();
        delWZBtt = new javax.swing.JButton();
        newWZBtt = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        nettoLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        bruttoLabel = new javax.swing.JLabel();
        openWZBtt = new javax.swing.JButton();
        contractorNameLabel = new javax.swing.JLabel();
        terminComm = new javax.swing.JLabel();
        accDoc = new javax.swing.JButton();
        printDoc = new javax.swing.JButton();
        endDoc = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        newWZ = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        setResizable(false);

        WZTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NUMER", "ID KLIENTA", "KLIENT", "PRZYJĘTO", "TERMIN", "WYDANO", "NR FVAT", "DATA FVAT", "SESIN", "OPTI", "STATUS", "TYP"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(WZTable);

        delWZBtt.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        delWZBtt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delDoc.png"))); // NOI18N
        delWZBtt.setToolTipText("USUŃ DOKUMENT");
        delWZBtt.setMaximumSize(new java.awt.Dimension(40, 40));
        delWZBtt.setMinimumSize(new java.awt.Dimension(40, 40));
        delWZBtt.setPreferredSize(new java.awt.Dimension(40, 40));
        delWZBtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delWZBttActionPerformed(evt);
            }
        });

        newWZBtt.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        newWZBtt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addDoc.png"))); // NOI18N
        newWZBtt.setToolTipText("NOWY DOKUMENT");
        newWZBtt.setMaximumSize(new java.awt.Dimension(40, 40));
        newWZBtt.setMinimumSize(new java.awt.Dimension(40, 40));
        newWZBtt.setPreferredSize(new java.awt.Dimension(40, 40));
        newWZBtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newWZBttActionPerformed(evt);
            }
        });

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NAZWA", "NR SERYJNY", "KOSZT", "PROBLEM", "NAPRAWY", "MIEJSCE NAPRAWY"
            }
        ));
        jScrollPane2.setViewportView(productTable);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("WARTOŚĆ NETTO:");

        nettoLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("WARTOŚĆ BRUTTO:");

        bruttoLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        openWZBtt.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        openWZBtt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/editDoc.png"))); // NOI18N
        openWZBtt.setToolTipText("EDYTUJ DOKUMENT");
        openWZBtt.setMaximumSize(new java.awt.Dimension(40, 40));
        openWZBtt.setMinimumSize(new java.awt.Dimension(40, 40));
        openWZBtt.setPreferredSize(new java.awt.Dimension(40, 40));
        openWZBtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openWZBttActionPerformed(evt);
            }
        });

        contractorNameLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        contractorNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        terminComm.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        terminComm.setForeground(new java.awt.Color(255, 0, 0));
        terminComm.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        accDoc.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        accDoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/confirmDoc.png"))); // NOI18N
        accDoc.setToolTipText("ZATWIERDZ DOKUMENT");
        accDoc.setMaximumSize(new java.awt.Dimension(40, 40));
        accDoc.setMinimumSize(new java.awt.Dimension(40, 40));
        accDoc.setPreferredSize(new java.awt.Dimension(40, 40));
        accDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accDocActionPerformed(evt);
            }
        });

        printDoc.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        printDoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print.png"))); // NOI18N
        printDoc.setToolTipText("DRUKUJ DOKUMENT");
        printDoc.setMaximumSize(new java.awt.Dimension(40, 40));
        printDoc.setMinimumSize(new java.awt.Dimension(40, 40));
        printDoc.setPreferredSize(new java.awt.Dimension(40, 40));
        printDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printDocActionPerformed(evt);
            }
        });

        endDoc.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        endDoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/endDoc.png"))); // NOI18N
        endDoc.setToolTipText("WYSTAW DOKUMENT KOŃCOWY");
        endDoc.setMaximumSize(new java.awt.Dimension(40, 40));
        endDoc.setMinimumSize(new java.awt.Dimension(40, 40));
        endDoc.setPreferredSize(new java.awt.Dimension(40, 40));
        endDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endDocActionPerformed(evt);
            }
        });

        jMenu1.setText("Dokumenty");

        newWZ.setText("Nowy dokument WZ");
        newWZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newWZActionPerformed(evt);
            }
        });
        jMenu1.add(newWZ);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Okno");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Zamknij");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(contractorNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(newWZBtt, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(openWZBtt, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(delWZBtt, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(accDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(printDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nettoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bruttoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(terminComm, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 284, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jScrollPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(delWZBtt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(openWZBtt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accDoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(printDoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(endDoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newWZBtt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(terminComm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bruttoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nettoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(contractorNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void newWZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newWZActionPerformed
        DocEntity doc = wz.getLastWZ();
        ContractorChoice cont = new ContractorChoice();
        cont.parentFrameWzWindow = this;
        cont.parentFrameId = 1;
        cont.show();
        this.disable();
    }//GEN-LAST:event_newWZActionPerformed
    public void addDocument(int id, int sesin, int opti, Long dl){
        wz.addDoc(id, sesin, opti, dl);
    }
    public void addDocument(int id, int sesin, Long dl){
        wz.addDoc(id, sesin, dl);
    }
    
    public void addDocument(int id, Long dl){
        wz.addDoc(id, dl);
    }
    public void addDocumentOpti(int id, int opti, Long dl){
        wz.addDocOpti(id, opti, dl);
    }
    private void newWZBttActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newWZBttActionPerformed
        DocEntity doc = wz.getLastWZ();
        ContractorChoice cont = new ContractorChoice();
        cont.parentFrameWzWindow = this;
        cont.parentFrameId = 1;
        cont.show();
        this.disable();
    }//GEN-LAST:event_newWZBttActionPerformed

    private void delWZBttActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delWZBttActionPerformed
        selectedDocId = Integer.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(),0).toString());
        if ( "TWORZENIE DOKUMENTU".equals(WZTable.getValueAt(WZTable.getSelectedRow(),11).toString()) || "GOTOWY DO WYDANIA".equals(WZTable.getValueAt(WZTable.getSelectedRow(),11).toString())){
            confirmDeleteDocument confDel = new confirmDeleteDocument();
            confDel.setDocumentID(selectedDocId);
            confDel.setSelectedRow(WZTable.getSelectedRow());
            this.disable();
            confDel.setWzL(this);
            confDel.show();
        }
        else{
            DocumentConfirmedAlert DocConfAlert = new DocumentConfirmedAlert();
            DocConfAlert.parentFrame = this;
            this.disable();
            DocConfAlert.show();
        }
    }//GEN-LAST:event_delWZBttActionPerformed

    private void openWZBttActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openWZBttActionPerformed
        wzWindow wzWind = new wzWindow(Integer.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(),0).toString()));
        wzWind.parentFrame = this;
        this.disable();
        wzWind.show();
    }//GEN-LAST:event_openWZBttActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        parentFrame.enable();
        this.dispose();
        parentFrame.show();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void accDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accDocActionPerformed
        if(productToShow.isEmpty()){
            DocumentEmptyAlert allert = new DocumentEmptyAlert();
            allert.parentFrame = this;
            this.disable();
            allert.show();
        }else if(Float.valueOf(nettoLabel.getText().replace(",", ".")) == 0){
            confirmZeroAmount allert = new confirmZeroAmount();
            allert.parentFrame = this;
            this.disable();
            allert.show();
        }else{
            wz.acceptDocument(Integer.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(),0).toString()), WZTable.getValueAt(WZTable.getSelectedRow(),11).toString());
            toShow = wz.getWZDocs();
            drawTable(toShow);
        }
        refreshWindow();
    }//GEN-LAST:event_accDocActionPerformed
    public PdfPCell createCell(String content, int alignment) throws IOException, DocumentException {
        BaseFont ft = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
        Font ffont = new Font(ft,9);
        Phrase ph = new Phrase(content);
        ph.setFont(ffont);
        PdfPCell cell = new PdfPCell(Phrase.getInstance(Element.ALIGN_CENTER, content, ffont));
        cell.setHorizontalAlignment(alignment);
        return cell;
    }
    private void printDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printDocActionPerformed
      Document document = new Document();
      Footer ft = new Footer();
      companyAdress comp = new companyAdress();
      
      try
      {  
        BaseFont ft1 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
        Font ffont = new Font(ft1,12);
        
        BaseFont ft3 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
        Font bold = new Font(ft1,12,Font.BOLD);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
        document.open();
        Paragraph pr = new Paragraph();
        pr.setFont(ffont);
        writer.setPageEvent(ft);
        ContractorEntity  contractor = wz.getContractor(Integer.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(), 2).toString()));
        pr.add(WZTable.getValueAt(WZTable.getSelectedRow(), 4).toString());
        pr.setAlignment(Element.ALIGN_RIGHT);
        document.add(pr);
        pr.clear();
        if("SPRZĘT WYDANY".equals(WZTable.getValueAt(WZTable.getSelectedRow(), 11).toString())){
        Paragraph nr = new Paragraph("WYADNIE SPRZĘTU NR ", ffont);
        String yearSlashnr = WZTable.getValueAt(WZTable.getSelectedRow(), 1).toString();
        nr.add(new Chunk(yearSlashnr,bold));
        nr.setAlignment(Element.ALIGN_LEFT);
        document.add(nr);
        if("WS".equals(WZTable.getValueAt(WZTable.getSelectedRow(), 12).toString())){
        pr.setAlignment(Element.ALIGN_LEFT);
        DocEntity docPS = wz.getDocument(wz.getPSForWs(Integer.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(), 0).toString())));
        pr.add("DOTYCZY: PS NR "+docPS.getDocNumber());
        document.add(pr);
        pr.clear();
        pr.setAlignment(Element.ALIGN_RIGHT);
        }
        }
        
        
        if("SPRZĘT PRZYJĘTY".equals(WZTable.getValueAt(WZTable.getSelectedRow(), 11).toString())){
        int rok = Timestamp.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(), 4).toString()).getYear()+1900;
        Paragraph nr = new Paragraph("PRZYJĘCIE SPRZĘTU NR ", ffont);
        String yearSlashnr = rok+"/"+WZTable.getValueAt(WZTable.getSelectedRow(), 1).toString();
        nr.add(new Chunk(yearSlashnr,bold));
        nr.setAlignment(Element.ALIGN_LEFT);
        document.add(nr);
        }
        pr.clear();
        pr.add(Chunk.NEWLINE);
        pr.add(Chunk.NEWLINE);
        document.add(pr);
        pr.clear();
        pr.setAlignment(Element.ALIGN_LEFT);
        pr.add(comp.getName());
        pr.add(Chunk.NEWLINE);
        pr.add("ul. "+comp.getStreet());
        pr.add(Chunk.NEWLINE);
        pr.add(comp.getPostal()+" "+comp.getCity());
        pr.add(Chunk.NEWLINE);
        pr.add("NIP: "+comp.getNip());
        pr.add(Chunk.NEWLINE);
        pr.add("Tel: "+comp.getPhone());
        pr.add(Chunk.NEWLINE);
        pr.add("Fax: "+comp.getFax());
        pr.add(Chunk.NEWLINE);
        pr.add("E-mail: "+comp.getEmail());
        pr.add(Chunk.NEWLINE);
        pr.add(Chunk.NEWLINE);
        pr.add("KLIENT:");
        pr.add(Chunk.NEWLINE);
        pr.add(contractor.getName());
        pr.add(Chunk.NEWLINE);
        pr.add("ul. "+contractor.getStreet());
        pr.add(Chunk.NEWLINE);
        pr.add(contractor.getPostalCode()+" "+contractor.getCity());
        pr.add(Chunk.NEWLINE);
        pr.add("NIP: "+contractor.getNip());
        pr.add(Chunk.NEWLINE);
        pr.add("Tel: "+contractor.getPhone());
        pr.add(Chunk.NEWLINE);
        pr.add("E-mail: "+contractor.getEmail());
        document.add(pr);
        
        pr.clear();
        pr.add(Chunk.NEWLINE);
        pr.add(Chunk.NEWLINE);
        pr.add("PRZYJĘTE URZĄDZENIA:");
        document.add(pr);
        
        //PUT IMAGE FROM DRIVE
        java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage("C:/GIT/support/logo.jpg");
        Image img = com.itextpdf.text.Image.getInstance(awtImage, null);
        int indentation = 0;
        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
               - document.rightMargin() - indentation) / img.getWidth()) * 20;
        img.scalePercent(scaler);
        img.setAbsolutePosition(document.right()-90, document.top()-150);
        document.add(new Paragraph());
        document.add(img);
        
        //ADD TABLE
        
        PdfPTable table = new PdfPTable(7); // 3 columns.
        table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f); //Space after table
        //Set Column widths
        float[] columnWidths = {1f, 4f, 3f, 2f, 2f, 6f, 6f};
        table.setWidths(columnWidths);
        BaseFont ft2 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
        Font headerFont = new Font(ft2,9);
        Paragraph pos1 = new Paragraph("",headerFont);
        Paragraph pos2 = new Paragraph("",headerFont);
        Paragraph pos3 = new Paragraph("",headerFont);
        Paragraph pos4 = new Paragraph("",headerFont);
        Paragraph pos5 = new Paragraph("",headerFont);
        Paragraph pos6 = new Paragraph("",headerFont);
        Paragraph pos7 = new Paragraph("",headerFont);

        //FORMATING TABLE
        pos1.clear();
        pos1.add("LP");
        PdfPCell cell1 = new PdfPCell(pos1);
        cell1.setBorderColor(BaseColor.BLACK);
        cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell1);
        pos2.clear();
        pos2.add("NAZWA");
        PdfPCell cell2 = new PdfPCell(pos2);
        cell2.setBorderColor(BaseColor.BLACK);
        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell2);
        pos3.clear();
        pos3.add("NR SERYJNY");
        PdfPCell cell3 = new PdfPCell(pos3);
        cell3.setBorderColor(BaseColor.BLACK);
        cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell3);
        pos4.clear();
        pos4.add("CENA NETTO");
        PdfPCell cell4 = new PdfPCell(pos4);
        cell4.setBorderColor(BaseColor.BLACK);
        cell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell4);
        pos5.clear();
        pos5.add("CENA BRUTTO");
        PdfPCell cell5 = new PdfPCell(pos5);
        cell5.setBorderColor(BaseColor.BLACK);
        cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell5);
        pos6.clear();
        pos6.add("PROBLEM");
        PdfPCell cell6 = new PdfPCell(pos6);
        cell6.setBorderColor(BaseColor.BLACK);
        cell6.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell6);
        pos7.clear();
        pos7.add("NAPRAWA");
        PdfPCell cell7 = new PdfPCell(pos7);
        cell7.setBorderColor(BaseColor.BLACK);
        cell7.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell7);
        
        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setBackgroundColor(BaseColor.WHITE);
        cell2.setBackgroundColor(BaseColor.WHITE);
        cell3.setBackgroundColor(BaseColor.WHITE);
        cell4.setBackgroundColor(BaseColor.WHITE);
        cell5.setBackgroundColor(BaseColor.WHITE);
        cell6.setBackgroundColor(BaseColor.WHITE);
        cell7.setBackgroundColor(BaseColor.WHITE);
        int counter = 1;
        for(DocProductEntity prod : productToShow){
            //col 1 (LP)
            table.addCell(createCell(""+counter, Element.ALIGN_CENTER));
            counter++;
            //col 2 (NAME)
            table.addCell(createCell(prod.getName(), Element.ALIGN_LEFT));
            //col 3 (NR SER)
            table.addCell(createCell(prod.getSerial(), Element.ALIGN_CENTER));
            //col 4 (NETTO)
            table.addCell(createCell(String.valueOf(prod.getPrice()), Element.ALIGN_RIGHT));
            //col 5 (BRUTTO)
            table.addCell(createCell(String.valueOf(prod.getPrice()+(prod.getPrice()*0.23)), Element.ALIGN_RIGHT));
            //col 6 (PROBLEM)
            table.addCell(createCell(prod.getProblem(), Element.ALIGN_LEFT)); 
            //col 7 (REPAIRS)
            table.addCell(createCell(prod.getRepair(), Element.ALIGN_LEFT));
        } 
        document.add(table);
        pr.setAlignment(Element.ALIGN_RIGHT);
        pr.clear();
        pr.add("PRZEWIDYWANIY KOSZT NAPRAWY:");
        document.add(pr);
        pr.clear();
        pr.add("Razem netto: "+ nettoLabel.getText()+" Razem brutto: "+bruttoLabel.getText());
        document.add(pr);
        pr.clear();
        pr.add("Słownie: "+amountInWords(Float.valueOf(bruttoLabel.getText().replace(",", "."))));
        document.add(pr);
        pr.clear();
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        pr.add(".................................................");
        document.add(pr);
        pr.clear();
        pr.add("Podpis i pieczątka pracownika");
        document.add(pr);
        if("SPRZĘT WYDANY".equals(WZTable.getValueAt(WZTable.getSelectedRow(), 11).toString())){
            pr.clear();
            pr.add(Chunk.NEWLINE);
            pr.add(Chunk.NEWLINE);
            pr.add(".................................................");
            document.add(pr);
            pr.clear();
            pr.add("Sprzęt odebrałem (Podpis klienta)");
            document.add(pr);
        }
        //CLOSING DOCUMENT
        document.close(); 
        writer.close();
        
        } catch (DocumentException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
        StampPageXofY numeration = new StampPageXofY();
        tempName=nowTimestamp();
        numeration.manipulatePdf("test.pdf", tempName+".pdf");
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        //OPEN READY DOCUMENT
        try {
            Desktop.getDesktop().open(new File(tempName+".pdf"));
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_printDocActionPerformed

    private void endDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endDocActionPerformed
        int wsId = wz.getWsForPs(Integer.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(),0).toString()));
        if(wsId==0){
            wz.createWsDocumentFromPs(Integer.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(),0).toString()));
            toShow = wz.getWZDocs();
            drawTable(toShow);
            WZTable.changeSelection(0, 0, false, false);
            refreshWindow();
        }else{
            DocumentCreatedAlert docCreated = new DocumentCreatedAlert(wsId);
            docCreated.parentFrame = this;
            this.disable();
            docCreated.show();
        }
    }//GEN-LAST:event_endDocActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(wzListWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(wzListWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(wzListWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(wzListWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new wzListWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable WZTable;
    private javax.swing.JButton accDoc;
    private javax.swing.JLabel bruttoLabel;
    private javax.swing.JLabel contractorNameLabel;
    private javax.swing.JButton delWZBtt;
    private javax.swing.JButton endDoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel nettoLabel;
    private javax.swing.JMenuItem newWZ;
    private javax.swing.JButton newWZBtt;
    private javax.swing.JButton openWZBtt;
    private javax.swing.JButton printDoc;
    public javax.swing.JTable productTable;
    private javax.swing.JLabel terminComm;
    // End of variables declaration//GEN-END:variables
}
