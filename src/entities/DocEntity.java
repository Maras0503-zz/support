/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.sql.Timestamp;
/**
 *
 * @author Marek
 */
public class DocEntity {
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

    public DocEntity() {
    }

    
    
    public DocEntity(int id, int docNumber, int docType, Timestamp docLeavingDate, Timestamp docRepairDate, Timestamp docReceiptDate, int docContractorId, String docContractorName, String docFvatNumber, Timestamp docFvatDate, int docSesin, int docOpti, String docStatus) {
        this.id = id;
        this.docNumber = docNumber;
        this.docType = docType;
        this.docLeavingDate = docLeavingDate;
        this.docRepairDate = docRepairDate;
        this.docReceiptDate = docReceiptDate;
        this.docContractorId = docContractorId;
        this.docContractorName = docContractorName;
        this.docFvatNumber = docFvatNumber;
        this.docFvatDate = docFvatDate;
        this.docSesin = docSesin;
        this.docOpti = docOpti;
        this.docStatus = docStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(int docNumber) {
        this.docNumber = docNumber;
    }

    public int getDocType() {
        return docType;
    }

    public void setDocType(int docType) {
        this.docType = docType;
    }

    public Timestamp getDocLeavingDate() {
        return docLeavingDate;
    }

    public void setDocLeavingDate(Timestamp docLeavingDate) {
        this.docLeavingDate = docLeavingDate;
    }

    public Timestamp getDocRepairDate() {
        return docRepairDate;
    }

    public void setDocRepairDate(Timestamp docRepairDate) {
        this.docRepairDate = docRepairDate;
    }

    public Timestamp getDocReceiptDate() {
        return docReceiptDate;
    }

    public void setDocReceiptDate(Timestamp docReceiptDate) {
        this.docReceiptDate = docReceiptDate;
    }

    public int getDocContractorId() {
        return docContractorId;
    }

    public void setDocContractorId(int docContractorId) {
        this.docContractorId = docContractorId;
    }

    public String getDocContractorName() {
        return docContractorName;
    }

    public void setDocContractorName(String docContractorName) {
        this.docContractorName = docContractorName;
    }

    public String getDocFvatNumber() {
        return docFvatNumber;
    }

    public void setDocFvatNumber(String docFvatNumber) {
        this.docFvatNumber = docFvatNumber;
    }

    public Timestamp getDocFvatDate() {
        return docFvatDate;
    }

    public void setDocFvatDate(Timestamp docFvatDate) {
        this.docFvatDate = docFvatDate;
    }

    public int getDocSesin() {
        return docSesin;
    }

    public void setDocSesin(int docSesin) {
        this.docSesin = docSesin;
    }

    public int getDocOpti() {
        return docOpti;
    }

    public void setDocOpti(int docOpti) {
        this.docOpti = docOpti;
    }

    public String getDocStatus() {
        return docStatus;
    }

    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }
        
}
