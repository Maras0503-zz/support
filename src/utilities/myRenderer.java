/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.awt.Component;
import java.sql.Timestamp;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import static utilities.TimeFunctions.nowTimestamp;
import static utilities.TimeFunctions.timestampToLong;

/**
 *
 * @author Marek
 */
public class myRenderer extends DefaultTableCellRenderer{ 
    String stat;
    public Component getTableCellRendererComponent(JTable table, Object value, boolean   isSelected, boolean hasFocus, int row, int column) { 
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
        if (! table.isRowSelected(row)){
            stat = table.getValueAt(row, 11).toString();
            if(column == 5 && !"SPRZĘT WYDANY".equals(stat)){
            long test1 = timestampToLong(Timestamp.valueOf(value.toString().substring(0, 18)))-86400000;
            if(nowTimestamp() > test1)
                c.setBackground(new java.awt.Color(255, 0, 0));
            else
                c.setBackground(new java.awt.Color(255, 255, 255));
            }
            else{
                c.setBackground(new java.awt.Color(255, 255, 255));
            }

        }

        return c; 
    } 

} 
