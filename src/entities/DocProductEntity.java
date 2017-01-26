/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author Marek
 */
public class DocProductEntity {
    int id;
    String name;
    String serial;
    String problem;
    String repair;
    float price;
    String place;

    public DocProductEntity(int id, String name, String serial, float price, String problem, String repair, String place) {
        this.id = id;
        this.name = name;
        this.serial = serial;
        this.problem = problem;
        this.repair = repair;
        this.price = price;
        this.place = place;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
    
    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getRepair() {
        return repair;
    }

    public void setRepair(String repair) {
        this.repair = repair;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
    
    
    
  
}
