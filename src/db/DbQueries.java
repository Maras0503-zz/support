/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import entities.ContractorEntity;
import entities.DocEntity;
import entities.DocProductEntity;
import entities.ProductEntity;
import entities.groupEntity;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import static utilities.TimeFunctions.longToTimestamp;

/**
 *
 * @author Marek
 */
public class DbQueries {
    public DbConnect conn = new DbConnect();
    
    //WITHDRAW PRODUCT
    
    public void withdrawUnwithdraw(int id, int status){
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "UPDATE product_tab SET product_status=? WHERE product_id=?"
        );
        conn.stmt.setInt(1, status);
        conn.stmt.setInt(2, id);
        
        int rowInserted = conn.stmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    //GET LIST OF GROUPS FROM DB
    public List<groupEntity> getGroups(){
        List<groupEntity> ans = new ArrayList<>();
        int id;
        String name;
        String nameShort;
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "SELECT * FROM product_group_tab"
            );
            conn.result = conn.stmt.executeQuery();
            while(conn.result.next()){
                id = conn.result.getInt("product_group_id");
                name = conn.result.getString("product_group_name");
                nameShort = conn.result.getString("product_group_short");
                ans.add(new groupEntity(id,name,nameShort));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }        
        conn.disconnect();
        return ans;
    }
    
    //ADD PRODUCT
    public void addProduct(String description){
        conn.connect();      
        
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "insert into product_tab (product_description) "
                            + "values (?)"
        );
        conn.stmt.setString(1, description); 
        conn.stmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    } 
    
    //LOOKING FOR CONTRACTOR
    
    public List<ContractorEntity> findContracor(String namePart, String nipPart, boolean IsProvider){
       List<ContractorEntity> resultList = new ArrayList<>();
       int id, provider;
       String name, city, street, nip, postalCode, country;
       
       conn.connect();
       try{
            if(IsProvider == false){
                conn.stmt = (PreparedStatement) conn.connection.prepareStatement("SELECT * FROM contractor_tab WHERE contractor_name LIKE ? AND replace(contractor_nip,'-','') LIKE ?");
            }else{
                conn.stmt = (PreparedStatement) conn.connection.prepareStatement("SELECT * FROM contractor_tab WHERE contractor_name LIKE ? AND replace(contractor_nip,'-','') LIKE ? AND contractor_provider=1");
            }
            conn.stmt.setString(1, '%'+namePart+'%');
            conn.stmt.setString(2, '%'+nipPart+'%');
            conn.result = conn.stmt.executeQuery();
            while(conn.result.next()){
                id = conn.result.getInt("contractor_id");
                name = conn.result.getString("contractor_name");
                nip = conn.result.getString("contractor_nip");
                postalCode = conn.result.getString("contractor_postal_code");
                city = conn.result.getString("contractor_city");
                street = conn.result.getString("contractor_street");
                country = conn.result.getString("contractor_country");
                provider = conn.result.getInt("contractor_provider");
                resultList.add(new ContractorEntity(id, name, nip, postalCode, city, street, country, provider));
            }
        }
       catch(Exception e){
           e.printStackTrace();
       }
       return resultList;
    }
    
    //GET DOCUMENT LIST
    public List<DocEntity> getWZDocs(){
        List<DocEntity> resultList = new ArrayList<>();
        int id;
        int docNumber;
        int docType;
        Timestamp docLeavingDate;
        Timestamp docRepairDate;
        Timestamp docReceiptDate;
        int docContractorId;
        String docContractorName;
        String docFvatNumber;
        Timestamp docFvatDate;
        int docSesin;
        int docOpti;
        String docStatus;
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "SELECT document_id, document_type, document_number, document_leaving_date, document_repair_date, document_receipt_date, document_contractor_id, contractor_name, document_fvat_number, document_fvat_date, document_sesin, document_opti, status_name"
                            + " FROM document_tab"
                            + " inner join contractor_tab on document_tab.document_contractor_id=contractor_tab.contractor_id"
                            + " inner join status_tab on document_tab.document_status = status_tab.status_id"
                            + " where document_type=1 and document_number = 0 order by document_id desc"
            );
 
            conn.result = conn.stmt.executeQuery();
                        
            while(conn.result.next()){
                
                id = conn.result.getInt("document_id");
                docNumber = conn.result.getInt("document_number");
                docType = conn.result.getInt("document_type");
                docLeavingDate = longToTimestamp(conn.result.getLong("document_leaving_date"));
                docRepairDate = longToTimestamp(conn.result.getLong("document_repair_date"));
                docReceiptDate = longToTimestamp(conn.result.getLong("document_receipt_date"));
                docContractorId = conn.result.getInt("document_contractor_id");
                docContractorName = conn.result.getString("contractor_name");
                docFvatNumber = conn.result.getString("document_fvat_number");
                docFvatDate = longToTimestamp(conn.result.getLong("document_fvat_date"));
                docSesin = conn.result.getInt("document_sesin");
                docOpti = conn.result.getInt("document_opti");
                docStatus = conn.result.getString("status_name");
                
                resultList.add(new DocEntity(id, docNumber, docType, docLeavingDate, docRepairDate, docReceiptDate, docContractorId, docContractorName, docFvatNumber, docFvatDate, docSesin, docOpti, docStatus));
            }
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "SELECT document_id, document_type, document_number, document_leaving_date, document_repair_date, document_receipt_date, document_contractor_id, contractor_name, document_fvat_number, document_fvat_date, document_sesin, document_opti, status_name"
                            + " FROM document_tab"
                            + " inner join contractor_tab on document_tab.document_contractor_id=contractor_tab.contractor_id"
                            + " inner join status_tab on document_tab.document_status = status_tab.status_id"
                            + " where document_type=1 and document_number <> 0 order by document_number desc limit 30"
            );
            conn.result = conn.stmt.executeQuery();
                        
            while(conn.result.next()){
                
                id = conn.result.getInt("document_id");
                docNumber = conn.result.getInt("document_number");
                docType = conn.result.getInt("document_type");
                docLeavingDate = longToTimestamp(conn.result.getLong("document_leaving_date"));
                docRepairDate = longToTimestamp(conn.result.getLong("document_repair_date"));
                docReceiptDate = longToTimestamp(conn.result.getLong("document_receipt_date"));
                docContractorId = conn.result.getInt("document_contractor_id");
                docContractorName = conn.result.getString("contractor_name");
                docFvatNumber = conn.result.getString("document_fvat_number");
                docFvatDate = longToTimestamp(conn.result.getLong("document_fvat_date"));
                docSesin = conn.result.getInt("document_sesin");
                docOpti = conn.result.getInt("document_opti");
                docStatus = conn.result.getString("status_name");
                
                resultList.add(new DocEntity(id, docNumber, docType, docLeavingDate, docRepairDate, docReceiptDate, docContractorId, docContractorName, docFvatNumber, docFvatDate, docSesin, docOpti, docStatus));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }        
        conn.disconnect();
        return resultList;
    }
    
    //POBIERZ OSTATNI DOKUMENT WZ
    public DocEntity getLastWZ(){
        int id;
        int docNumber;
        int docType;
        Timestamp docLeavingDate;
        Timestamp docRepairDate;
        Timestamp docReceiptDate;
        int docContractorId;
        String docContractorName;
        String docFvatNumber;
        Timestamp docFvatDate;
        int docSesin;
        int docOpti;
        String docStatus;

        DocEntity result = new DocEntity();
        
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "select * from document_tab where document_type=1 order by document_id desc limit 1"
            );
            conn.result = conn.stmt.executeQuery();
            while(conn.result.next()){
                id = conn.result.getInt("document_id");
                docNumber = conn.result.getInt("document_number");
                docType = conn.result.getInt("document_type");
                docLeavingDate = longToTimestamp(conn.result.getLong("document_leaving_date"));
                docRepairDate = longToTimestamp(conn.result.getLong("document_repair_date"));
                docReceiptDate = longToTimestamp(conn.result.getLong("document_receipt_date"));
                docContractorId = conn.result.getInt("document_contractor_id");
                docContractorName = conn.result.getString("contractor_name");
                docFvatNumber = conn.result.getString("document_fvat_number");
                docFvatDate = longToTimestamp(conn.result.getLong("document_fvat_date"));
                docSesin = conn.result.getInt("document_sesin");
                docOpti = conn.result.getInt("document_opti");
                docStatus = conn.result.getString("status_name");
                result = new DocEntity(id, docNumber, docType, docLeavingDate, docRepairDate, docReceiptDate, docContractorId, docContractorName, docFvatNumber, docFvatDate, docSesin, docOpti, docStatus);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    //ADD DOCUMENT
    public void addDoc(int docType, long docDate, long docAcceptDate, int contractorId, int DocNumber, int docYear){
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "insert into document_tab (document_type, document_date, document_accept_date, document_contractor_id, document_number) "
                            + "values (?,?,?,?,?,?)"
        );
        conn.stmt.setInt(1, docType);
        conn.stmt.setLong(2, docDate);
        conn.stmt.setLong(3, docAcceptDate);
        conn.stmt.setInt(4, contractorId);
        conn.stmt.setInt(5, DocNumber);
        conn.stmt.setInt(6, docYear);
        
        int rowInserted = conn.stmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //DELETE DOCUMENT
    public void delDoc(int docId){
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "delete from document_tab where document_id=?"
        );
        conn.stmt.setInt(1, docId);
        
        int rowInserted = conn.stmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    
    //GET PRODUCT LIST

    public List<ProductEntity> getProducts(){
        List<ProductEntity> resultList = new ArrayList<>();       
        
        int id = 0;
        String description = "";
                
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                "SELECT product_id, product_description FROM product_tab"
            );
       
            conn.result = conn.stmt.executeQuery();
                        
            while(conn.result.next()){
                id = conn.result.getInt("product_id");
                description = conn.result.getString("product_description");
                resultList.add(new ProductEntity(id, description));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return resultList;
    }
    
    //GET PRODUCTS ON DOCUMENT
    public List<DocProductEntity> getDocProducts(int docId){
        List<DocProductEntity> resultList = new ArrayList<>();
        int id = 0;
        String description = "";
        float price;
        String problem = "";
        String repair = "";
        String serial = "";
        String place = "";
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "SELECT product_id, product_description, document_records_serial, document_records_price, document_records_problem, document_records_repair, repair_place_name FROM document_rekords"
                        +" inner join product_tab on product_tab.product_id = document_rekords.document_rekords_product_id"
                        +" inner join repair_place_tab on repair_place_tab.repair_place_id = document_rekords.document_rekords_repair_place"
                        +" where document_rekords_document_id=?"
            );
            conn.stmt.setInt(1, docId);
            conn.result = conn.stmt.executeQuery();
                        
            while(conn.result.next()){
                id = conn.result.getInt("product_id");
                description = conn.result.getString("product_description");
                serial = conn.result.getString("document_records_serial");
                price = conn.result.getFloat("document_rekords_product_number");
                problem = conn.result.getString("document_records_problem");
                repair = conn.result.getString("document_records_repair");
                resultList.add(new DocProductEntity(id, description, serial, price, problem, repair, place));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return resultList;
    }
    
}
