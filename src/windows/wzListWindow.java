/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import db.DbQueries;
import entities.DocEntity;
import entities.DocProductEntity;
import java.awt.Color;
import java.awt.Component;
import static java.lang.Math.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import tableTemplates.ProductOnDocumentTableTemplate;
import tableTemplates.wzTableTemplate;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import static utilities.Other.getScreenWidth;
import utilities.TimeFunctions;
import static utilities.TimeFunctions.timestampToLong;
import static utilities.TimeFunctions.nowTimestamp;
import utilities.myRenderer;

/**
 *
 * @author Marek
 */
public class wzListWindow extends javax.swing.JFrame {
    public MainWindow parentFrame;
    public int newWzContracorId;
    int selectedDocId;
    float snetto = 0;
    int setNowModelCounter = 0;
    Long tmpTime;
    
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
            public void valueChanged(ListSelectionEvent e) {
                long test1 = timestampToLong(Timestamp.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(), 5).toString().substring(0, 18)))-86400000;
                if(nowTimestamp() > test1 && !"SPRZĘT WYDANY".equals(WZTable.getValueAt(WZTable.getSelectedRow(), 11).toString())){
                    terminComm.setText("ZBLIŻA SIĘ TERMIN ODBIORU");
                }
                else{
                    terminComm.setText("");
                }
                selectedDocId = Integer.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(),0).toString());
                productToShow = wz.getDocProducts(selectedDocId);
                contractorNameLabel.setText(WZTable.getValueAt(WZTable.getSelectedRow(),3).toString());
                drawProductTable(productToShow);
            }
        });
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
        WZTable.getColumnModel().getColumn(1).setPreferredWidth((int)round(WZTableWidth*0.05));
        WZTable.getColumnModel().getColumn(2).setPreferredWidth((int)round(WZTableWidth*0.05));
        WZTable.getColumnModel().getColumn(3).setPreferredWidth((int)round(WZTableWidth*0.20));
        WZTable.getColumnModel().getColumn(4).setPreferredWidth((int)round(WZTableWidth*0.10));
        WZTable.getColumnModel().getColumn(5).setPreferredWidth((int)round(WZTableWidth*0.10));
        WZTable.getColumnModel().getColumn(6).setPreferredWidth((int)round(WZTableWidth*0.10));
        WZTable.getColumnModel().getColumn(7).setPreferredWidth((int)round(WZTableWidth*0.05));
        WZTable.getColumnModel().getColumn(8).setPreferredWidth((int)round(WZTableWidth*0.10));
        WZTable.getColumnModel().getColumn(9).setPreferredWidth((int)round(WZTableWidth*0.05));
        WZTable.getColumnModel().getColumn(10).setPreferredWidth((int)round(WZTableWidth*0.05));
        WZTable.getColumnModel().getColumn(11).setPreferredWidth((int)round(WZTableWidth*0.10));
        
        //INPUT DATA INTO TABLE
        dtm.setRowCount(docList.size());    
        for(int i = 0; i < docList.size(); i++){
              WZTable.getModel().setValueAt(docList.get(i).getId(), i, 0);
              WZTable.getModel().setValueAt(docList.get(i).getDocNumber(), i, 1);
              WZTable.getModel().setValueAt(docList.get(i).getDocContractorId(), i, 2);
              WZTable.getModel().setValueAt(docList.get(i).getDocContractorName(), i, 3);
              WZTable.getModel().setValueAt(docList.get(i).getDocLeavingDate(), i, 4);
              WZTable.getModel().setValueAt(docList.get(i).getDocRepairDate(), i, 5);
              tmpTime = timestampToLong(docList.get(i).getDocRepairDate())-86400000;
              if(tmpTime<TimeFunctions.nowTimestamp()){
                  
              }
              tmpTime = timestampToLong(docList.get(i).getDocReceiptDate());
              if(tmpTime.intValue() != 0){
                    WZTable.getModel().setValueAt(docList.get(i).getDocReceiptDate(), i, 6);
              }else{
                    WZTable.getModel().setValueAt("", i, 6);
              }
              WZTable.getModel().setValueAt(docList.get(i).getDocFvatNumber(), i, 7);
              tmpTime = timestampToLong(docList.get(i).getDocFvatDate());
              if(tmpTime.intValue() != 0){
                    WZTable.getModel().setValueAt(docList.get(i).getDocFvatDate(), i, 8);
              }else{
                    WZTable.getModel().setValueAt("", i, 8);
              }
              WZTable.getModel().setValueAt(docList.get(i).getDocSesin(), i, 9);
              WZTable.getModel().setValueAt(docList.get(i).getDocOpti(), i, 10);
              WZTable.getModel().setValueAt(docList.get(i).getDocStatus(), i, 11);
        }  
    }
    private void drawProductTable(List<DocProductEntity> prodList){
        ProductOnDocumentTableTemplate dtm = new ProductOnDocumentTableTemplate();
        productTable.setModel(dtm);
        
        //INITIALIZE RIGHT RENDERER
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);        
        //INITIALIZE CENTER RENDERER
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER); 


        productTable.setSize(getScreenWidth()-21, 300);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int prodTableWidth = productTable.getWidth();
        
        productTable.getColumnModel().getColumn(0).setPreferredWidth((int)round(prodTableWidth*0.05));
        productTable.getColumnModel().getColumn(1).setPreferredWidth((int)round(prodTableWidth*0.10));
        productTable.getColumnModel().getColumn(2).setPreferredWidth((int)round(prodTableWidth*0.10));
        productTable.getColumnModel().getColumn(3).setPreferredWidth((int)round(prodTableWidth*0.10));
        productTable.getColumnModel().getColumn(4).setPreferredWidth((int)round(prodTableWidth*0.30));
        productTable.getColumnModel().getColumn(5).setPreferredWidth((int)round(prodTableWidth*0.35));
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
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NUMER", "ID KLIENTA", "KLIENT", "PRZYJĘTO", "TERMIN", "WYDANO", "NR FVAT", "DATA FVAT", "SESIN", "OPTI", "STATUS"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(WZTable);

        delWZBtt.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        delWZBtt.setText("-");
        delWZBtt.setMaximumSize(new java.awt.Dimension(40, 40));
        delWZBtt.setMinimumSize(new java.awt.Dimension(40, 40));
        delWZBtt.setPreferredSize(new java.awt.Dimension(40, 40));
        delWZBtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delWZBttActionPerformed(evt);
            }
        });

        newWZBtt.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        newWZBtt.setText("+");
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
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NAZWA", "NR SERYJNY", "KOSZT", "PROBLEM", "NAPRAWY"
            }
        ));
        jScrollPane2.setViewportView(productTable);

        jLabel1.setText("WARTOŚĆ NETTO:");

        nettoLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel3.setText("WARTOŚĆ BRUTTO:");

        bruttoLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        openWZBtt.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        openWZBtt.setText("o");
        openWZBtt.setMaximumSize(new java.awt.Dimension(40, 40));
        openWZBtt.setMinimumSize(new java.awt.Dimension(40, 40));
        openWZBtt.setPreferredSize(new java.awt.Dimension(40, 40));
        openWZBtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openWZBttActionPerformed(evt);
            }
        });

        contractorNameLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        contractorNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        terminComm.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        terminComm.setForeground(new java.awt.Color(255, 0, 0));

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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1280, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(newWZBtt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delWZBtt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(openWZBtt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nettoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bruttoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(terminComm, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contractorNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(delWZBtt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newWZBtt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(openWZBtt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contractorNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nettoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel3))
                    .addComponent(bruttoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(terminComm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
    public void addDocument(int id, int sesin, int opti){
        wz.addDoc(id, sesin, opti);
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
        int selectedDocId = Integer.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(),0).toString());
        if ( WZTable.getValueAt(WZTable.getSelectedRow(),6).toString() == "dokument niepotwierdzony"){
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
        wzWindow wz = new wzWindow();
        wz.selectedDocument = Integer.valueOf(WZTable.getValueAt(WZTable.getSelectedRow(),0).toString());
        wz.parentFrame = this;
        this.disable();
        wz.show();
    }//GEN-LAST:event_openWZBttActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        parentFrame.enable();
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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
    private javax.swing.JLabel bruttoLabel;
    private javax.swing.JLabel contractorNameLabel;
    private javax.swing.JButton delWZBtt;
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
    private javax.swing.JTable productTable;
    private javax.swing.JLabel terminComm;
    // End of variables declaration//GEN-END:variables
}
