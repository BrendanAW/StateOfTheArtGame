package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.HashMap;
import java.util.function.Predicate;

public class FightArena {
    private HashMap<String, Fighter> fighters;
    private String[][] theMap;

    private Predicate<Fighter> aliveFighting;
    private Predicate<Fighter> deadNotFighting;
    private ObservableList<Fighter> fighterObservableList;
    private FilteredList<Fighter> fighterFilteredList;
    private ObservableList<String> observableListNames;
    private ObservableList<String> deadFighters;

    public FightArena() {
        aliveFighting = fighter -> fighter.getHealth() > 0;
        deadNotFighting = fighter -> fighter.getHealth() == 0;
        this.deadFighters = FXCollections.observableArrayList();
        this.fighters = new HashMap<>();
    }

   public ObservableList<String> getDeadFighters(){
        return this.deadFighters;
   }

    public ObservableList<String> getLivingFighters(){
        fighterObservableList = FXCollections.observableArrayList();
        observableListNames = FXCollections.observableArrayList();
        this.getFighters().forEach((s, fighter) -> fighterObservableList.add(fighter));
        fighterFilteredList = new FilteredList<>(fighterObservableList, aliveFighting);
        fighterFilteredList.forEach(fighter -> observableListNames.add(fighter.getName() + ", the " + fighter.getClassType()));
        return observableListNames;
    }

    public boolean addFighter(String name, Fighter fighter) {
        for (String s : this.fighters.keySet())
            if (s.equals(name))
                return false;
        this.fighters.put(name, fighter);
        return true;
    }

    public Fighter removePlayer(Fighter fighter, String s) {
        deadFighters.add(fighter.getName() + s);
        fighters.remove(fighter.getName());
        return fighter;
    }

    public String[][] buildMap(int width, int height) {
        String[][] tempMap = new String[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                tempMap[i][j] = "X";
            }
        this.theMap = tempMap;
        return tempMap;
    }

    public void printMap() {
        for (int i = 0; i < theMap.length; i++)
            for (int j = 0; j < theMap[0].length; j++) {
                if ((j == 0) && (i != 0))
                    System.out.println("\n");
                System.out.print(this.theMap[i][j]);
            }
        System.out.println("\n\n");
    }

    public Fighter locateFighter(int x, int y) {
        for (Fighter fighter : fighters.values()) {
            String[] tempLoc = this.getLocation(fighter).split(",");
            int a = Integer.parseInt(tempLoc[0]);
            int b = Integer.parseInt(tempLoc[1]);
            if ((a == x) && (b == y))
                return fighter;
        }
        return null;
    }

    public Fighter getFighter(String s){
        for(Fighter fighter1 : this.getFighters().values())
            if(fighter1.getName().equals(s))
                return fighter1;
        return null;
    }

    public boolean setLocation(Fighter fighter, int x, int y) {
        try {
            this.theMap[this.getFighterX(fighter)][this.getFighterY(fighter)] = "X";
        }catch (NumberFormatException e){

        }
        if ((x < this.theMap.length) && (y < this.theMap[0].length)) {
            this.theMap[x][y] = fighter.symbol;
            return true;
        } else {
            System.out.println("Invalid location selected");
            return false;
        }
    }

    public String getLocation(Fighter fighter) {
        for (int i = 0; i < this.theMap.length; i++) {
            for (int j = 0; j < this.theMap[0].length; j++) {
                if (this.theMap[i][j].equals(fighter.symbol)) {
                    return i + "," + j;
                }
            }
        }
        return "Location not found";
    }

    public String[][] getTheMap() {
        return this.theMap;
    }

    public HashMap<String, Fighter> getFighters() {
        return this.fighters;
    }

    public String getChampion() {
        for (Fighter fighter : fighters.values()) {
            return fighter.getName() + ", the " + fighter.getClassType().toLowerCase();
        }
        return null;
    }

    public int getFighterX(Fighter fighter) {
        String[] tempLoc = this.getLocation(fighter).split(",");
        return Integer.parseInt(tempLoc[0]);
    }

    public int getFighterY(Fighter fighter) {
        String[] tempLoc = this.getLocation(fighter).split(",");
        return Integer.parseInt(tempLoc[1]);
    }
}
