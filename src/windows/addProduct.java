
package windows;

import db.DbQueries;
import java.awt.Color;
import javax.swing.BorderFactory;


/**
 *
 * @author Marek
 */
public class addProduct extends javax.swing.JFrame {
    public productsWindow parentFrameProductWindow;
    public MainWindow parentFrameMainWidnow;
    public int selectedContractorId = 0;
    public String selectedContractorName = "";
    public int parentFrameId;
    public DbQueries db = new DbQueries();
    /**
     * Creates new form addProduct
     */
    public addProduct() {
        getRootPane().setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));
        initComponents();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameBox = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        addAndClose = new javax.swing.JButton();
        add = new javax.swing.JButton();
        communicatLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        window = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setUndecorated(true);

        nameBox.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        nameBox.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("NAZWA:");

        addAndClose.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        addAndClose.setText("DODAJ I ZAMKNIJ");
        addAndClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAndCloseActionPerformed(evt);
            }
        });

        add.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        add.setText("DODAJ");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        communicatLabel.setForeground(new java.awt.Color(255, 0, 0));

        window.setText("Okno");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Zamknij");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        window.add(jMenuItem1);

        jMenuBar1.add(window);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameBox))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(communicatLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(add)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addAndClose, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameBox, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addAndClose, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(communicatLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if(parentFrameId == 1){
            parentFrameProductWindow.enable();
        }
        if(parentFrameId == 2){
            parentFrameMainWidnow.enable();
        }
        this.hide();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        String alertText = "<html><center>";
        int colonNeeded = 0;
        int errorsCounter = 0;
    
        //checks whether the name is not empty
        if(nameBox.getText().equals("")){
            alertText += "nie podano nazwy";
            colonNeeded++;
            errorsCounter++;
        }
        
        //colon needed?
        if(colonNeeded == 1){
            alertText+=", ";
            colonNeeded--;
        }
        
        //checks whether the name is not too short minimum 10 character
        if(nameBox.getText().length() < 10){
            alertText += "nazwa jest za krótka (minimum 10 znaków)";
            colonNeeded++;
            errorsCounter++;
        }
        
      
        if (errorsCounter == 0){ 
            db.addProduct(nameBox.getText().toUpperCase());
            nameBox.setText("");
            communicatLabel.setText("");
            if(parentFrameId == 1){
                parentFrameProductWindow.drawTable(parentFrameProductWindow.wz.getProducts());
                parentFrameProductWindow.productTable.changeSelection(parentFrameProductWindow.productTable.getRowCount()-1, 1, false , false);
            }
       }else{
            communicatLabel.setText(alertText);
       }
       
    }//GEN-LAST:event_addActionPerformed

    private void addAndCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAndCloseActionPerformed
        String alertText = "<html><center>";
        int colonNeeded = 0;
        int errorsCounter = 0;
    
        //checks whether the name is not empty
        if(nameBox.getText().equals("")){
            alertText += "nie podano nazwy";
            colonNeeded++;
            errorsCounter++;
        }
        
        //colon needed?
        if(colonNeeded == 1){
            alertText+=", ";
            colonNeeded--;
        }
        
        //checks whether the name is not too short minimum 10 character
        if(nameBox.getText().length() < 10){
            alertText += "nazwa jest za krótka (minimum 10 znaków)";
            colonNeeded++;
            errorsCounter++;
        }
        
       
       if (errorsCounter == 0){ 
            db.addProduct(nameBox.getText().toUpperCase());
            if(parentFrameId == 1){
                parentFrameProductWindow.enable();
                parentFrameProductWindow.drawTable(parentFrameProductWindow.wz.getProducts());
                parentFrameProductWindow.productTable.changeSelection(parentFrameProductWindow.productTable.getRowCount()-1, 1, false , false);
            }
            if(parentFrameId == 2){
                parentFrameMainWidnow.enable();
            }
            this.dispose();
       }else{
            communicatLabel.setText(alertText);
       }
    }//GEN-LAST:event_addAndCloseActionPerformed

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
            java.util.logging.Logger.getLogger(addProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(addProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(addProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(addProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new addProduct().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add;
    private javax.swing.JButton addAndClose;
    private javax.swing.JLabel communicatLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JTextField nameBox;
    private javax.swing.JMenu window;
    // End of variables declaration//GEN-END:variables
}
