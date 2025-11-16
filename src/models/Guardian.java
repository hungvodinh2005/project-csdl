/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author DINHHUNG
 */
public class Guardian {
     
        private String id;
        private String name;

        public Guardian(String id, String name) {
            this.id = id;
            this.name = name;
        }
        public String getId() { return id; }
        public String getName() { return name; }
        @Override
        public String toString() {
            return id + " - " + name; // thuận tiện cho hiển thị JComboBox
        }
    
}
