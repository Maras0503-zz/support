/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import entities.ContractorEntity;
import entities.DocEntity;
import entities.DocProductEntity;
import entities.DocWSProduct;
import entities.ProductEntity;
import entities.groupEntity;
import entities.repairLocationEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import static utilities.TimeFunctions.longToTimestamp;
import static utilities.TimeFunctions.nowTimestamp;
import static utilities.TimeFunctions.timestampToLong;

/**
 *
 * @author Marek
 */
public class DbQueries {
    public DbConnect conn = new DbConnect();
    
    
    //ADD CONTRACTOR
    public void addContractor(String name, String nip, String postal, String city, String street, String country, String phone, String email){
        try {
            conn.connect();
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement("insert into contractor_tab (contractor_name, contractor_nip, contractor_postal_code, contractor_city, contractor_street, contractor_country, contractor_phone, contractor_email) values (?,?,?,?,?,?,?,?)");
            conn.stmt.setString(1, name);
            conn.stmt.setString(2, nip);
            conn.stmt.setString(3, postal);
            conn.stmt.setString(4, city);
            conn.stmt.setString(5, street);
            conn.stmt.setString(6, country);
            conn.stmt.setString(7, phone);
            conn.stmt.setString(8, email);
            conn.stmt.executeUpdate();
            
        } catch (Exception e) {
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
        conn.disconnect();
    } 
    
    //LOOKING FOR CONTRACTOR
    
    public List<ContractorEntity> findContracor(String namePart, String nipPart){
       List<ContractorEntity> resultList = new ArrayList<>();
       int id, provider;
       String name, city, street, nip, postalCode, country, phone, email;
       
       conn.connect();
       try{
       
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement("SELECT * FROM contractor_tab WHERE contractor_name LIKE ? AND replace(contractor_nip,'-','') LIKE ?");

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
                phone = conn.result.getString("contractor_phone");
                email = conn.result.getString("contractor_email");
                resultList.add(new ContractorEntity(id, name, nip, postalCode, city, street, country, phone, email));
            }
        }
       catch(Exception e){
           e.printStackTrace();
       }
       conn.disconnect();
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
                            + " where (document_type=1 or document_type=2) and document_number = 0 order by document_id desc"
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
                            + " where (document_type=1 or document_type=2) and document_number <> 0 order by document_number desc"
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
                    "select *, contractor_name from document_tab"
                            + " join contractor_tab on contractor_tab.contractor_id = document_tab.document_contractor_id"
                            + " join status_tab on status_id = document_tab.document_status"
                            + " where document_type=1 order by document_id desc limit 1"
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
        conn.disconnect();
        return result;
    }
    
    //GET CONTRACOR'S DATA
    
    public ContractorEntity getContractor(int conId){
        ContractorEntity contractor = new ContractorEntity();
        int id, provider;
        String name, city, street, nip, postalCode, country, phone, email;
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
              "select * from contractor_tab where contractor_id=?"      
            );
            conn.stmt.setInt(1, conId);
            conn.result = conn.stmt.executeQuery();
            while(conn.result.next()){
                id = conn.result.getInt("contractor_id");
                name = conn.result.getString("contractor_name");
                nip = conn.result.getString("contractor_nip");
                postalCode = conn.result.getString("contractor_postal_code");
                city = conn.result.getString("contractor_city");
                street = conn.result.getString("contractor_street");
                country = conn.result.getString("contractor_country");
                phone = conn.result.getString("contractor_phone");
                email = conn.result.getString("contractor_email");
                contractor = new ContractorEntity(id, name, nip, postalCode, city, street, country, phone, email);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        conn.disconnect();
        return contractor;
    }
    
    //POBIERZ DANE O DOKUMENCIE O WSKAZANYM ID
    public DocEntity getDocument(int docId){
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
                    "select *, contractor_name from document_tab"
                            + " join contractor_tab on contractor_tab.contractor_id = document_tab.document_contractor_id"
                            + " join status_tab on status_id = document_tab.document_status"
                            + " where document_id=? order by document_id desc limit 1"
            );
            conn.stmt.setInt(1, docId);
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
        conn.disconnect();
        return result;
    }
    //ADD DOCUMENT (CONTRACTOR ID, SESIN, OPTI, DEADLINE)
    public void addDoc(int contractorId, int sesin, int opti, Long dl){
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "insert into document_tab (document_type, document_leaving_date, document_repair_date, document_receipt_date, document_contractor_id, document_number, document_fvat_number, document_fvat_date, document_sesin, document_opti, document_status) "
                            + "values (?,?,?,?,?,?,?,?,?,?,?)"
        );
        conn.stmt.setInt(1, 1);
        conn.stmt.setLong(2, nowTimestamp());
        conn.stmt.setLong(3, dl);
        conn.stmt.setLong(4, 0);
        conn.stmt.setInt(5, contractorId);
        conn.stmt.setInt(6, 0);
        conn.stmt.setString(7, "");
        conn.stmt.setLong(8, 0);
        conn.stmt.setInt(9, sesin);
        conn.stmt.setInt(10, opti);
        conn.stmt.setInt(11, 5);
        
        
        
        int rowInserted = conn.stmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        conn.disconnect();
    }
    
    //ADD DOCUMENT (CON ID, SESIN, WITHOUT OPTI, DEADLINE)
    public void addDoc(int contractorId, int sesin, Long dl){
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "insert into document_tab (document_type, document_leaving_date, document_repair_date, document_receipt_date, document_contractor_id, document_number, document_fvat_number, document_fvat_date, document_sesin, document_opti, document_status) "
                            + "values (?,?,?,?,?,?,?,?,?,null,?)"
        );
        conn.stmt.setInt(1, 1);
        conn.stmt.setLong(2, nowTimestamp());
        conn.stmt.setLong(3, dl);
        conn.stmt.setLong(4, 0);
        conn.stmt.setInt(5, contractorId);
        conn.stmt.setInt(6, 0);
        conn.stmt.setString(7, "");
        conn.stmt.setLong(8, 0);
        conn.stmt.setInt(9, sesin);
        conn.stmt.setInt(10, 5);
        
        
        
        int rowInserted = conn.stmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        conn.disconnect();
    }
    
    //ADD DOCUMENT (CON ID, WITHOUT SESIN, WITHOUT OPTI, DEADLINE)
    public void addDoc(int contractorId, Long dl){
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "insert into document_tab (document_type, document_leaving_date, document_repair_date, document_receipt_date, document_contractor_id, document_number, document_fvat_number, document_fvat_date, document_sesin, document_opti, document_status) "
                            + "values (?,?,?,?,?,?,?,?,null,null,?)"
        );
        conn.stmt.setInt(1, 1);
        conn.stmt.setLong(2, nowTimestamp());
        conn.stmt.setLong(3, dl);
        conn.stmt.setLong(4, 0);
        conn.stmt.setInt(5, contractorId);
        conn.stmt.setInt(6, 0);
        conn.stmt.setString(7, "");
        conn.stmt.setLong(8, 0);
        conn.stmt.setInt(9, 5);
        
        
        
        int rowInserted = conn.stmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        conn.disconnect();
    }
    
    //ADD DOCUMENT (CON ID, WITHOUT SESIN, WITH OPTI, DEADLINE)
    public int addDocOpti(int contractorId, int opti, Long dl){
        int rowInserted = 0;
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "insert into document_tab (document_type, document_leaving_date, document_repair_date, document_receipt_date, document_contractor_id, document_number, document_fvat_number, document_fvat_date, document_sesin, document_opti, document_status) "
                            + "values (?,?,?,?,?,?,?,?,null,?,?)"
        );
        conn.stmt.setInt(1, 1);
        conn.stmt.setLong(2, nowTimestamp());
        conn.stmt.setLong(3, dl);
        conn.stmt.setLong(4, 0);
        conn.stmt.setInt(5, contractorId);
        conn.stmt.setInt(6, 0);
        conn.stmt.setString(7, "");
        conn.stmt.setLong(8, 0);
        conn.stmt.setInt(9, opti);
        conn.stmt.setInt(10, 5);
        
        
        
        rowInserted = conn.stmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        conn.disconnect();
        return rowInserted;
    }
    //GET WS FOR PS
    
    public int getWsForPs(int psId){
        int wsId = 0;
        try{
            conn.connect();
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement("select ws_id from ps_to_ws_tab where ps_id=?");
            conn.stmt.setInt(1, psId);
            conn.result = conn.stmt.executeQuery();
            while(conn.result.next()){
                wsId = conn.result.getInt("ws_id");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return wsId;
    }
    
    public int getPSForWs(int wsId){
        int psId = 0;
        try{
            conn.connect();
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement("select ps_id from ps_to_ws_tab where ws_id=?");
            conn.stmt.setInt(1, wsId);
            conn.result = conn.stmt.executeQuery();
            while(conn.result.next()){
                psId = conn.result.getInt("ps_id");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return psId;
    }
    //DELETE DOCUMENT
    public void delDoc(int docId){
        DocEntity doc = getDocument(docId);
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "delete from document_tab where document_id=?"
            );
            conn.stmt.setInt(1, docId);

            int rowInserted = conn.stmt.executeUpdate();
            if(doc.getDocType()==2){
                conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                     "delete from ps_to_ws_tab where ws_id=?"
                );
                conn.stmt.setInt(1, docId);
                rowInserted = conn.stmt.executeUpdate();
            }      
        }catch(Exception e){
            e.printStackTrace();
        }    
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "delete from document_rekords where document_rekords_document_id=?"
        );
        conn.stmt.setInt(1, docId);
        
        int rowInserted = conn.stmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        conn.disconnect();        
    }
    
    //CHECK REPAIR PLACE ID
    
    public int getRepairPlaceId(String repairPlace){
        conn.connect();
        int id = 0;    
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement("select repair_place_id from repair_place_tab where repair_place_name=?");
            conn.stmt.setString(1, repairPlace);
            conn.result = conn.stmt.executeQuery();
            while(conn.result.next()){
                id = conn.result.getInt("repair_place_id");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        conn.disconnect();
        return id;
    }
    
    //CHECK PRODUCT ID 
    public int getProductId(String productName){
        conn.connect();
        int id = 0;    
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement("select product_id from product_tab where product_description=?");
            conn.stmt.setString(1, productName);
            conn.result = conn.stmt.executeQuery();
            while(conn.result.next()){
                id = conn.result.getInt("product_id");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        conn.disconnect();
        return id;
    }
    
    //DELETE PRODUCT FORM DOCUMENT
    
    public void delProductFromDocument(int docId, int prodId, String serial, String problem, String repairs, float price, int place){
        conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "delete from document_rekords where document_rekords_document_id=? and document_rekords_product_id=? and document_rekords_serial=? and document_rekords_problem=? and document_rekords_repairs=? and document_rekords_price=? and document_rekords_repair_place=? limit 1"
            );
            conn.stmt.setInt(1, docId);
            conn.stmt.setInt(2, prodId);
            conn.stmt.setString(3, serial);
            conn.stmt.setString(4, problem);
            conn.stmt.setString(5, repairs);
            conn.stmt.setFloat(6, price);
            conn.stmt.setInt(7, place);
            conn.stmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //ADD PRODUCT TO DOCUMENT
    public int addProductToDocument(int docId, int prodId, String serial, String problem, String repairs, float price, int place){
    int rowInserted = 0;
    conn.connect();
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "insert into document_rekords (document_rekords_document_id, document_rekords_product_id, document_rekords_serial, document_rekords_problem, document_rekords_repairs, document_rekords_price, document_rekords_repair_place)"
                            + " values (?,?,?,?,?,?,?)"
            );
        conn.stmt.setInt(1, docId);
        conn.stmt.setInt(2, prodId);
        conn.stmt.setString(3, serial);
        conn.stmt.setString(4, problem);
        conn.stmt.setString(5, repairs);
        conn.stmt.setFloat(6, price);
        conn.stmt.setInt(7, place);
        
        
        
        rowInserted = conn.stmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        conn.disconnect();
        return rowInserted;
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
        conn.disconnect();
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
                    "SELECT product_id, product_description, document_rekords_serial, document_rekords_price, document_rekords_problem, document_rekords_repairs, repair_place_name FROM document_rekords"
                        +" inner join product_tab on product_tab.product_id = document_rekords.document_rekords_product_id"
                        +" inner join repair_place_tab on repair_place_tab.repair_place_id = document_rekords.document_rekords_repair_place"
                        +" where document_rekords_document_id=?"
            );
            conn.stmt.setInt(1, docId);
            conn.result = conn.stmt.executeQuery();
                        
            while(conn.result.next()){
                id = conn.result.getInt("product_id");
                description = conn.result.getString("product_description");
                serial = conn.result.getString("document_rekords_serial");
                price = conn.result.getFloat("document_rekords_price");
                problem = conn.result.getString("document_rekords_problem");
                repair = conn.result.getString("document_rekords_repairs");
                place = conn.result.getString("repair_place_name");
                resultList.add(new DocProductEntity(id, description, serial, price, problem, repair, place));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        conn.disconnect();
        return resultList;
    }
    
    //CREATE WS
    public void createWsDocumentFromPs(int docId){
        List<DocWSProduct> resultList = new ArrayList<>();
        int rowInserted = 0;
        ResultSet newId;
        int lastInsertedId = 0;
        int id = 0;
        String serial = "";
        float price = 0;
        String problem = "";
        String repair = "";
        int place = 0;
        DocEntity doc = getDocument(docId);
        conn.connect();
        
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "insert into document_tab (document_type, document_leaving_date, document_repair_date, document_receipt_date, document_contractor_id, document_number, document_fvat_number, document_fvat_date, document_sesin, document_opti, document_status) "
                            + "values (?,?,?,?,?,?,?,?,?,?,?)"
                    
                    , Statement.RETURN_GENERATED_KEYS
        );
        conn.stmt.setInt(1, 2);
        conn.stmt.setLong(2, nowTimestamp());
        conn.stmt.setLong(3, timestampToLong(doc.getDocRepairDate()));
        conn.stmt.setLong(4, 0);
        conn.stmt.setInt(5, doc.getDocContractorId());
        conn.stmt.setInt(6, 0);
        conn.stmt.setString(7, "");
        conn.stmt.setLong(8, 0);
        conn.stmt.setInt(9, doc.getDocSesin());
        conn.stmt.setInt(10, doc.getDocOpti());
        conn.stmt.setInt(11, 3);
        
        rowInserted = conn.stmt.executeUpdate();
            newId = conn.stmt.getGeneratedKeys();
            if(newId.next())
            {
                lastInsertedId = newId.getInt(1);
            }        
        
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "SELECT document_rekords_product_id, document_rekords_serial, document_rekords_price, document_rekords_problem, document_rekords_repairs, document_rekords_repair_place FROM document_rekords"
                        +" where document_rekords_document_id=?"
            );
            conn.stmt.setInt(1, docId);
            conn.result = conn.stmt.executeQuery();
                        
            while(conn.result.next()){
                id = conn.result.getInt("document_rekords_product_id");
                serial = conn.result.getString("document_rekords_serial");
                price = conn.result.getFloat("document_rekords_price");
                problem = conn.result.getString("document_rekords_problem");
                repair = conn.result.getString("document_rekords_repairs");
                place = conn.result.getInt("document_rekords_repair_place");
                resultList.add(new DocWSProduct(id, serial, price, problem, repair, place));
                try{
                conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "insert into document_rekords (document_rekords_document_id, document_rekords_product_id, document_rekords_serial, document_rekords_problem, document_rekords_repairs, document_rekords_price, document_rekords_repair_place)"
                            + " values (?,?,?,?,?,?,?)"
                    );
                conn.stmt.setInt(1, lastInsertedId);
                conn.stmt.setInt(2, id);
                conn.stmt.setString(3, serial);
                conn.stmt.setString(4, problem);
                conn.stmt.setString(5, repair);
                conn.stmt.setFloat(6, price);
                conn.stmt.setInt(7, place);
                
                conn.stmt.executeUpdate();
                
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        try{
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement(
                    "insert into ps_to_ws_tab (ps_id, ws_id) "
                            + "values (?,?)"
            );
            conn.stmt.setInt(1, docId);
            conn.stmt.setInt(2, lastInsertedId);
            conn.stmt.executeUpdate();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        conn.disconnect();
    }
    
    public List<repairLocationEntity> getRepairLocation(){
        List<repairLocationEntity> resultList = new ArrayList<>();
        int id;
        String name;
        conn.connect();
        try{
            conn.stmt = (PreparedStatement)conn.connection.prepareStatement(
                "select * from repair_place_tab"
            );
            conn.result = conn.stmt.executeQuery();
            while(conn.result.next()){
                id = conn.result.getInt("repair_place_id");
                name = conn.result.getString("repair_place_name");
                resultList.add(new repairLocationEntity(id, name));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        conn.disconnect();
        return resultList;
    }
    
    //GET LAST DOC FOR DOC TYPE
    public int getLastNumber(int docType){
        int lastNo = 0;
        try{
            conn.connect();
            conn.stmt = (PreparedStatement) conn.connection.prepareStatement("select document_number from document_tab where document_type=? order by document_number desc limit 1");
            conn.stmt.setInt(1, docType);
            conn.result = conn.stmt.executeQuery();
            while(conn.result.next()){
                lastNo = conn.result.getInt("document_number");
            }
        }catch( Exception e){
            e.printStackTrace();
        }
        return lastNo;
    }
    
    public void acceptDocument(int docId, String status){
        DocEntity doc= getDocument(docId);
        int psId = 0;
        if("GOTOWY DO WYDANIA".equals(status)){
            psId = getPSForWs(docId);
        }
        int lastNo = getLastNumber(doc.getDocType());
        if("TWORZENIE DOKUMENTU".equals(status)){
            try{
                conn.connect();
                conn.stmt = (PreparedStatement) conn.connection.prepareStatement("update document_tab set document_status=1, document_number=? where document_id=?");
                conn.stmt.setInt(1, lastNo+1);
                conn.stmt.setInt(2, docId);
                conn.stmt.executeUpdate();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if("GOTOWY DO WYDANIA".equals(status)){
            try{
                conn.connect();
                conn.stmt = (PreparedStatement) conn.connection.prepareStatement("update document_tab set document_status=4, document_receipt_date=?, document_number=? where document_id=?");
                conn.stmt.setLong(1, nowTimestamp());
                conn.stmt.setInt(2, lastNo+1);
                conn.stmt.setInt(3, docId);
                conn.stmt.executeUpdate();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                conn.connect();
                conn.stmt = (PreparedStatement) conn.connection.prepareStatement("update document_tab set document_status=4 where document_id=?");
                conn.stmt.setLong(1, psId);
                conn.stmt.executeUpdate();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
