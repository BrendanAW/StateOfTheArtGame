package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Random;

public abstract class Fighter {
    String symbol;
    private String name;
    private double health;
    private int evasion;
    private double strength;
    private String special;
    private String classType;
    private int movement = 1;
    private int specialAction = 1;

    public Fighter(String name, String classType) {
        this.name = name;
        this.classType = classType;
        this.health = 15.0;
        this.evasion = 5;
        this.strength = 5.0;
        this.special = "block";
        this.symbol = "     0";
    }

    public Fighter(String name, String classType, double health, int evasion, double strength, String special) {
        this.name = name;
        this.classType = classType;
        this.health = health;
        this.evasion = evasion;
        this.strength = strength;
        this.special = special;
        this.symbol = "     0";
    }

    public String attack(Fighter victim) {
        switch (this.getClassType().toLowerCase()) {
            case "tank":
                victim.takeDamage(this.getStrength());
               return this.getName() + " swings their fat fist at " + victim.getName() + ", for " + this.getStrength() + " points!";
            case "range":
                victim.takeDamage(this.getStrength());
               return this.getName() + " shoots an arrow at  " + victim.getName() + ", for " + this.getStrength() + " points!";
            case "melee":
                victim.takeDamage(this.getStrength());
                return this.getName() + " swings a sword at " + victim.getName() + ", for " + this.getStrength() + " points!";
            default:
                return null;
        }
    }

    public boolean dodge() {
        System.out.println(this.getName() + " is attempting to dodge!");
        Random random = new Random();
        if (this.evasion == 10) {
            System.out.println(this.getName() + " blocked the attack with their shield!");
            this.setEvasion(5);
            return true;
        }
        if ((random.nextInt(9) + 1) > this.evasion)
            return false;
        System.out.println("Dodge successful!!");
        System.out.println(this.evasion);
        return true;
    }

    public void performSpecial() {
        switch (this.getSpecial()) {
            case "heal":
                this.health += 5;
                System.out.println(this.getName() + " has healed 5pts!");
                break;
            case "block":
                this.evasion = 10;
                System.out.println(this.getName() + " is using their shield and will dodge the next attack!");
                break;
            case "doubleMove":
                this.movement = 2;
                System.out.println(this.getName() + " has earned an additional move");
                break;
            default:
                break;
        }
        this.specialAction = 0;
    }


    public double takeDamage(double amt) {
        if (this.dodge())
            return this.health;
        if (amt >= this.health) {
            System.out.println("Player has been killed");
            return this.health = 0;
        }
        System.out.println("Hero " + this.name + " took " + amt + " points of damage!");
        this.health -= amt;
        return this.health;
    }

    public ObservableList<String> fighterStats() {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        /*
        name classtype health evasion strength special symbol special action
         */
        observableList.add(0, "Name:             |   " + this.name);
        observableList.add(1, "Class Type:      |   " + this.classType);
        observableList.add(2, "Health:            |   " + this.health);
        observableList.add(3, "Evasion:           |   " + this.evasion);
        observableList.add(4, "Strength:         |   " + this.strength);
        observableList.add(5, "Special:           |   " + this.special);
        observableList.add(6, "Symbol:           |   " + this.symbol);
        observableList.add(7, "Special Read:    |   " + this.specialAction);
        return observableList;
    }


    @Override
    public String toString() {
        return this.name;
    }

    public double getHealth() {
        return this.health;
    }

    public String getName() {
        return this.name;
    }

    public String getSpecial() {
        return this.special;
    }

    public double getStrength() {
        return this.strength;
    }

    public int getMovement() {
        return this.movement;
    }

    public int getSpecialAction() {
        return this.specialAction;
    }

    public void setHealth(int i) {
        this.health = i;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public void setEvasion(int evasion) {
        this.evasion = evasion;
    }

    public void decrementMovement() {
        this.movement--;
    }

    public void setSpecialAction(int specialAction) {
        this.specialAction = specialAction;
    }

    private void incrementSpecial() {
        this.specialAction++;
    }

    public void doNothing() {
        setMovement(0);
    }

    public void reset() {
        incrementSpecial();
        setMovement(1);
    }

    public String getClassType() {
        return this.classType;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


}
