package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class Controller {
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private ListView<String> playersInArena;

    @FXML
    private ListView<String> playerStats;

    @FXML
    private TextArea textToPlayer;

    @FXML
    private GridPane controllerPanel;

    @FXML
    private TextArea textToComputer;

    @FXML
    private Button submitPlayerInput;

    @FXML
    private VBox playerInputContainer;

    @FXML
    private ListView<String> deadFighters;

    private FightArena arena = new FightArena();
    private HashMap<String, BooleanProperty> boolsListed;
    private BooleanProperty Up, Right, Down, Left, Attack, Move, Special, Quit, Sepuku, Nothing, Square, Rectangle, Archer, BigMan, Knight;
    private Spinner tempSpinner;
    private Spinner tempSpinner2;
    private Button tempBtn;
    private int Width;
    private int Height;
    private int turn = 0;

    @FXML
    private Label player1;
    @FXML
    private Label player2;
    @FXML
    private Label player3;
    @FXML
    private Label player4;

    public void initialize() {

        playersInArena.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        bindVisibilities();
        gridPane.setAlignment(Pos.BOTTOM_CENTER);
        beginningText();
        controllerPanel.getChildren().forEach(node -> {
            if (node instanceof Button) {
                ((Button) node).setOnAction(event -> arenaShape((Button) event.getSource()));
            }
        });
    }

    /*
    All Methods are nested to guarantee linear movement throughout gameplay
    Methods are ordered based off call sequence
     */

    private void beginningText() {
        textToPlayer.setText("Hello and welcome to a state of the art fighting simulation where you and your friends(or just you) can battle it out to determine who is the best fighter!" +
                "\nNow please, create your fighting arena!\n" +
                "Please choose a size for your arena, you can choose a square or rectangular arena, which would you like?");
        String[] s = new String[]{"Square", "Rectangle"};
        revealButtons(s);
    }

    private void revealButtons(String[] buttonBools) {
        for (String s : buttonBools)
            for (String t : boolsListed.keySet())
                if (s.equals(t))
                    boolsListed.get(s).setValue(true);
    }

    private void arenaShape(Button button) {
        Rectangle.setValue(false);
        Square.setValue(false);
        String a = button.getText();
        if (a.equals("Square")) {
            playerInputContainer.getChildren().add(new Label("L&W"));
            playerInputContainer.getChildren().add(new Spinner(5, 10, 5));
            submitPlayerInput.setVisible(true);
        } else if (a.equals("Rectangle")) {
            playerInputContainer.getChildren().add(new Label("Width"));
            playerInputContainer.getChildren().add(new Spinner(5, 10, 5));
            playerInputContainer.getChildren().add(new Label("Height"));
            playerInputContainer.getChildren().add(new Spinner(5, 10, 5));
            submitPlayerInput.setVisible(true);
        }
        playerInputContainer.getChildren().forEach(node -> {
            if (node instanceof Spinner) {
                if (tempSpinner == null)
                    tempSpinner = (Spinner) node;
                else if ((playerInputContainer.getChildren().size() > 4) && tempSpinner2 == null)
                    tempSpinner2 = (Spinner) node;
            }
        });
        if (tempSpinner != null)
            submitPlayerInput.setOnAction(event -> {
                if (tempSpinner2 == null)
                    printMap(Integer.parseInt(tempSpinner.getValue().toString()), Integer.parseInt(tempSpinner.getValue().toString()));
                else
                    printMap(Integer.parseInt(tempSpinner.getValue().toString()), Integer.parseInt(tempSpinner2.getValue().toString()));
                Square.setValue(false);
                Rectangle.setValue(false);
                if (playerInputContainer.getChildren().size() == 4) {
                    playerInputContainer.getChildren().remove(1, 4);
                    textToPlayer.setText("You have chosen a square with the parameters: " + tempSpinner.getValue() + "X" + tempSpinner.getValue());
                } else {
                    playerInputContainer.getChildren().remove(1, 6);
                    textToPlayer.setText("You have chosen a rectangle with the parameters: " + tempSpinner.getValue() + "X" + tempSpinner2.getValue());
                }
                fighterSize();
            });
    }

    private void printMap(int x, int y) {
        Width = y;
        Height = x;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if ((i == 0) && (j == 0))
                    gridPane.add(player1 = new Label("X"), 0, 0);
                else if ((i == 0) && (j == y - 1))
                    gridPane.add(player2 = new Label("X"), i, j);
                else if ((i == x - 1) && (j == 0))
                    gridPane.add(player3 = new Label("X"), i, j);
                else if ((i == x - 1) && (j == y - 1))
                    gridPane.add(player4 = new Label("X"), i, j);
                else gridPane.add(new Label("X"), i, j);
                arena.buildMap(y, x);
            }
        }
        gridPane.getChildren().forEach(node -> {
            node.setStyle(("-fx-padding: 10px; -fx-border-color: black; -fx-text-fill: white;"));
            if (((Label) node).getText().equals("X"))
                node.setStyle(node.getStyle() + "-fx-background-color: gray;");
            else
                node.setStyle(node.getStyle() + "-fx-background-color: blue;");
        });
    }

    private void fighterSize() {
        textToPlayer.setText(textToPlayer.getText() + "\n\nNow it is time to add fighters, please choose a number of fighters, followed by their class and name.");
        playerInputContainer.getChildren().add(tempSpinner = new Spinner(2, 4, 2));
        playerInputContainer.getChildren().add(submitPlayerInput);
        submitPlayerInput.setOnAction(event -> {
            playerInputContainer.getChildren().remove(1, playerInputContainer.getChildren().size());
            revealButtons(new String[]{"Archer", "BigMan", "Knight"});
            textToComputer.setDisable(false);
            textToComputer.setText("Enter Hero Name Here! Then click on preferred class!");
            controllerPanel.getChildren().forEach(node -> {
                if (node instanceof Button)
                    ((Button) node).setOnAction(event1 -> {
                        createFighters((Button) event1.getSource());
                        setSelectedFighter();
                    });
            });
        });
    }

    private void createFighters(Button button) {
        String s = textToComputer.getText();
        if (arena.getFighters().size() < Integer.parseInt(tempSpinner.getValue().toString())) {
            if (textToComputer.getText().matches("\\w+")) {
                switch (button.getText()) {
                    case "Archer":
                        if (arena.addFighter(s, new Archer(s))) {
                            textToPlayer.setText("Player, " + s + " created.");
                            playersInArena.setItems(arena.getLivingFighters());
                        } else textToPlayer.setText("Fighter with that name already exists.\nPlease try again.");
                        break;
                    case "BigMan":
                        if (arena.addFighter(s, new BigMan(s))) {
                            textToPlayer.setText("Player, " + s + " created.");
                            playersInArena.setItems(arena.getLivingFighters());
                        } else textToPlayer.setText("Fighter with that name already exists.\nPlease try again.");
                        break;
                    case "Knight":
                        if (arena.addFighter(s, new Knight(s))) {
                            textToPlayer.setText("Player, " + s + " created.");
                            playersInArena.setItems(arena.getLivingFighters());
                        } else textToPlayer.setText("Fighter with that name already exists.\nPlease try again.");
                        break;
                }

                textToComputer.setText("");
            } else textToPlayer.setText("Invalid name try again");
        }
    }

    private void setSelectedFighter() {
        if (arena.getFighters().size() >= Integer.parseInt(tempSpinner.getValue().toString())) {
            setFighterSymbols();
            for (String[] s : arena.getTheMap()) {
                for (String t : s)
                    System.out.print(t);
                System.out.println();
            }
            clearControllerButtons();
            playersInArena.getSelectionModel().selectFirst();
            textToComputer.setText("");
            textToComputer.setDisable(true);
            fightSequence();
        }
    }

    private void setFighterSymbols() {
        for (int i = 0; i < arena.getFighters().size(); i++) {
            for (Fighter fighter : arena.getFighters().values()) {
                if (fighter.getHealth() > 0)
                    switch (i) {
                        case 0:
                            fighter.setSymbol("@");
                            player1.setText("@");
                            i++;
                            break;
                        case 1:
                            fighter.setSymbol("#");
                            player2.setText("#");
                            i++;
                            break;
                        case 2:
                            fighter.setSymbol("$");
                            player3.setText("$");
                            i++;
                            break;
                        case 3:
                            fighter.setSymbol("%");
                            player4.setText("%");
                            i++;
                            break;
                        default:
                            break;
                    }
            }
        }
        int i = 0;
        for (Fighter fighter : arena.getFighters().values()) {
            if (i == 0) {
                arena.setLocation(fighter, 0, 0);
            }
            if (i == 1) {
                arena.setLocation(fighter, 0, arena.getTheMap()[0].length - 1);
//                addFighterToMap(fighter);
            }
            if (i == 2)
                arena.setLocation(fighter, arena.getTheMap().length - 1, 0);
            if (i == 3)
                arena.setLocation(fighter, Width - 1, Height - 1);
            i++;
            updateFighterPosition();
        }
    }

    private void fightSequence() {
        Fighter tempFighter;
        if (playersInArena.getItems().size() == 1) {
            playersInArena.getSelectionModel().select(0);
            fighterStats();
            textToPlayer.setText(playersInArena.getSelectionModel().getSelectedItem() + " is the winner!");
            clearControllerButtons();
        } else if (playersInArena.getItems().size() > 0) {
            playersInArena.getSelectionModel().select(turn);
            String[] s = playersInArena.getSelectionModel().getSelectedItem().split(",\\W");
            tempFighter = arena.getFighter(s[0]);
            if (tempFighter.getMovement() > 0) {
                actionText(tempFighter.getName());
                fighterStats();
                controllerPanel.getChildren().forEach(node -> {
                    if (node instanceof Button)
                        ((Button) node).setOnAction(event -> actionEvent((Button) event.getSource()));
                });
            } else {
                tempFighter.setMovement(tempFighter.getMovement() + 1);
                tempFighter.setSpecialAction(tempFighter.getSpecialAction() + 1);
                turn++;
                if (turn > arena.getFighters().size() - 1)
                    turn = 0;
                fightSequence();
            }
        }
    }

    private Fighter getFightingFighter() {
        String[] s = playersInArena.getSelectionModel().getSelectedItem().split(",\\W");
        return arena.getFighter(s[0]);
    }

    private void updateFighterPosition() {
        gridPane.getChildren().remove(0, gridPane.getChildren().size());
        for (int j = 0; j < Width; j++) {
            for (int i = 0; i < Height; i++) {
                gridPane.add(new Label(arena.getTheMap()[j][i]), i, j);
            }
        }
        gridPane.getChildren().forEach(node -> {
            node.setStyle(("-fx-padding: 10px; -fx-border-color: black; -fx-text-fill: white;"));
            if (node instanceof Label) {
                if (((Label) node).getText().equals("X"))
                    node.setStyle(node.getStyle() + "-fx-background-color: gray;");
                else
                    node.setStyle(node.getStyle() + "-fx-background-color: blue;");
            }
        });
    }

    private void actionText(String s) {
        Fighter tempFighter = arena.getFighter(s);
        if (textToPlayer.getParagraphs().size() == 2)
            textToPlayer.setText(textToPlayer.getText() + "\n" + s + "! Its time to act! What will you do?");
        else textToPlayer.setText(s + "! Its time to act! What will you do?");
        int options = 0;
        String[] buttonString;
        if (tempFighter.getMovement() > 0) {
            if (attackAvailable(tempFighter) != null)
                options++;
            options++;
        }
        if (tempFighter.getSpecialAction() >= 4)
            options++;
        buttonString = new String[options];

        if (tempFighter.getMovement() > 0) {
            buttonString[0] = "Move";
            if (attackAvailable(tempFighter) != null) {
                buttonString[1] = "Attack";
                if (tempFighter.getSpecialAction() >= 4)
                    buttonString[2] = "Special";
            } else if (tempFighter.getSpecialAction() >= 4)
                buttonString[1] = "Special";

        }
        revealButtons(buttonString);
        revealButtons(new String[]{"Quit", "Sepuku", "Nothing"});
    }

    private String[] attackAvailable(Fighter fighter) {
        int x = arena.getFighterX(fighter);
        int y = arena.getFighterY(fighter);
        String[] dirAvailable = null;
        int fightersAvailable = 0;
        boolean up, down, left, right;
        up = down = left = right = false;
        if (fighter.getClassType().matches("(Tank)*(Melee)*")) {
            if (arena.locateFighter(x - 1, y) != null) {
                up = true;
                fightersAvailable++;
            }
            if (arena.locateFighter(x + 1, y) != null) {
                down = true;
                fightersAvailable++;
            }
            if (arena.locateFighter(x, y - 1) != null) {
                left = true;
                fightersAvailable++;
            }
            if (arena.locateFighter(x, y + 1) != null) {
                right = true;
                fightersAvailable++;
            }
        } else {
            for (Fighter tempFighter : arena.getFighters().values()) {
                if (x == arena.getFighterX(tempFighter)) {
                    if (y > arena.getFighterY(tempFighter)) {
                        left = true;
                        fightersAvailable++;
                    } else if (y < arena.getFighterY(tempFighter)) {
                        right = true;
                        fightersAvailable++;
                    }
                } else if (y == arena.getFighterY(tempFighter)) {
                    if (x > arena.getFighterX(tempFighter)) {
                        up = true;
                        fightersAvailable++;
                    } else if (x < arena.getFighterX(tempFighter)) {
                        down = true;
                        fightersAvailable++;
                    }
                }
            }
        }

        if (fightersAvailable > 0)
            dirAvailable = new String[fightersAvailable];
        for (int i = 0; i < fightersAvailable; i++) {
            if (up) {
                dirAvailable[i] = "Up";
                up = false;
                continue;
            }
            if (down) {
                dirAvailable[i] = "Down";
                down = false;
                continue;
            }
            if (right) {
                dirAvailable[i] = "Right";
                right = false;
                continue;
            }
            if (left) {
                dirAvailable[i] = "Left";
                left = false;
            }
        }
        return dirAvailable;

    }

    private boolean moveAvailable(Fighter fighter) {

        return false;
    }

    private void actionEvent(Button button) {
        String buttonName = button.getText();
        String[] fighterName = playersInArena.getSelectionModel().getSelectedItem().split(",\\W");
        Fighter selectedFighter = arena.getFighter(fighterName[0]);
        switch (buttonName) {
            case "Attack":
                attemptAttack(selectedFighter);
                break;
            case "Move":
                getDirection(selectedFighter);
                break;
            case "Special":
                selectedFighter.performSpecial();
                fighterStats();
                break;
            case "Nothing":
                selectedFighter.setMovement(selectedFighter.getMovement() - 1);
                fightSequence();
                break;
            case "Sepuku":
                die(selectedFighter, ", the suicidal");
                fightSequence();
                break;
            case "Quit":
                fightSequence();
                break;
        }
    }

    private void attemptAttack(Fighter fighter) {
        clearControllerButtons();
        revealButtons(attackAvailable(fighter));
        controllerPanel.getChildren().forEach(node -> {
            if (node instanceof Button)
                ((Button) node).setOnAction(event -> {
                    if (fighter.getClassType().matches("(Tank)*(Melee)*"))
                        closeAttack(fighter, (Button) event.getSource());
                    else rangedAttack(fighter, (Button) event.getSource());
                });
        });

    }

    private void rangedAttack(Fighter fighter, Button button) {
        String s = button.getText();
        int x = arena.getFighterX(fighter);
        int y = arena.getFighterY(fighter);
        switch (s) {
            case "Up":
                for (int i = x - 1; i >= 0; i--) {
                    if (arena.locateFighter(i, y) != null) {
                        x = i;
                        break;
                    }
                }
                break;
            case "Down":
                for (int i = x + 1; i < arena.getTheMap().length; i++) {
                    if (arena.locateFighter(i, y) != null) {
                        x = i;
                        break;
                    }
                }
                break;
            case "Left":
                for (int i = y - 1; i >= 0; i--) {
                    if (arena.locateFighter(x, i) != null) {
                        y = i;
                        break;
                    }
                }
                break;
            case "Right":
                for (int i = y + 1; i < arena.getTheMap()[0].length; i++) {
                    if (arena.locateFighter(x, i) != null) {
                        y = i;
                        break;
                    }
                }
        }
        dealDamage(fighter, x, y, ", the Pierced");
    }

    private void closeAttack(Fighter fighter, Button button) {
        String s = button.getText();
        int x = arena.getFighterX(fighter);
        int y = arena.getFighterY(fighter);
        switch (s) {
            case "Up":
                x -= 1;
                break;
            case "Down":
                x += 1;
                break;
            case "Left":
                y -= 1;
                break;
            case "Right":
                y += 1;
                break;
        }
        if (fighter.getClassType().equals("Tank"))
            dealDamage(fighter, x, y, ",the Mangled");
        else dealDamage(fighter, x, y, ", the Slit");
    }

    private void dealDamage(Fighter fighter, int x, int y, String s) {
        double attackeeHealth = arena.locateFighter(x, y).getHealth();
        textToPlayer.setText(fighter.attack(arena.locateFighter(x, y)));
        if (attackeeHealth == arena.locateFighter(x, y).getHealth())
            textToPlayer.setText(textToPlayer.getText() + "\nThe attack was dodged!");
        else textToPlayer.setText(textToPlayer.getText() + "\nPlayer Hit!");
        if (arena.locateFighter(x, y).getHealth() == 0)
            die(arena.locateFighter(x, y), s);
        clearControllerButtons();
        fighter.setMovement(fighter.getMovement() - 1);
        fightSequence();
    }

    private void getDirection(Fighter fighter) {
        clearControllerButtons();
        int x = arena.getFighterX(fighter);
        int y = arena.getFighterY(fighter);
        if ((x > 0) && arena.getTheMap()[x - 1][y].equals("X"))
            Up.setValue(true);
        if ((x < arena.getTheMap().length - 1) && arena.getTheMap()[x + 1][y].equals("X"))
            Down.setValue(true);
        if ((y > 0) && arena.getTheMap()[x][y - 1].equals("X"))
            Left.setValue(true);
        if ((y < arena.getTheMap()[0].length - 1) && arena.getTheMap()[x][y + 1].equals("X"))
            Right.setValue(true);
        controllerPanel.getChildren().forEach(node -> {
            if (node instanceof Button)
                ((Button) node).setOnAction(event -> move((Button) event.getSource(), fighter));
        });
    }

    private void move(Button button, Fighter fighter) {
        int x = arena.getFighterX(fighter);
        int y = arena.getFighterY(fighter);
        switch (button.getText()) {
            case "Up":
                arena.setLocation(fighter, x - 1, y);
                break;
            case "Down":
                arena.setLocation(fighter, x + 1, y);
                break;
            case "Left":
                arena.setLocation(fighter, x, y - 1);
                break;
            case "Right":
                arena.setLocation(fighter, x, y + 1);
                break;
        }
        updateFighterPosition();
        fighter.setMovement(fighter.getMovement() - 1);
        clearControllerButtons();
        fightSequence();
    }

    private void fighterStats() {
        playerStats.getItems().remove(0, playerStats.getItems().size());
        Fighter tempFighter = getFightingFighter();
        tempFighter.fighterStats().forEach(s -> playerStats.getItems().add(s));
    }

    private void die(Fighter fighter, String s) {
        fighter.setHealth(0);
        fighter.setMovement(0);
        arena.removePlayer(fighter, s);
        arena.getTheMap()[arena.getFighterX(fighter)][arena.getFighterY(fighter)] = "X";
        setDeadFighters();
        updateFighterPosition();
        playersInArena.setItems(arena.getLivingFighters());
    }

    private void setDeadFighters() {
        if (arena.getDeadFighters() != null)
            deadFighters.setItems(arena.getDeadFighters());
    }

    //Extra method to move all bindings to an organized method to allow for easier manipulation of gui design
    private void bindVisibilities() {
        boolsListed = new HashMap<>();
        boolsListed.put("Up", Up = new SimpleBooleanProperty(false));
        boolsListed.put("Left", Left = new SimpleBooleanProperty(false));
        boolsListed.put("Down", Down = new SimpleBooleanProperty(false));
        boolsListed.put("Right", Right = new SimpleBooleanProperty(false));
        boolsListed.put("Attack", Attack = new SimpleBooleanProperty(false));
        boolsListed.put("Move", Move = new SimpleBooleanProperty(false));
        boolsListed.put("Special", Special = new SimpleBooleanProperty(false));
        boolsListed.put("Quit", Quit = new SimpleBooleanProperty(false));
        boolsListed.put("Sepuku", Sepuku = new SimpleBooleanProperty(false));
        boolsListed.put("Nothing", Nothing = new SimpleBooleanProperty(false));
        boolsListed.put("Square", Square = new SimpleBooleanProperty(false));
        boolsListed.put("Rectangle", Rectangle = new SimpleBooleanProperty(false));
        boolsListed.put("Archer", Archer = new SimpleBooleanProperty(false));
        boolsListed.put("Knight", Knight = new SimpleBooleanProperty(false));
        boolsListed.put("BigMan", BigMan = new SimpleBooleanProperty(false));
        Button button = null;
        for (Node node : controllerPanel.getChildren()) {
            for (String s : boolsListed.keySet()) {
                button = (Button) node;
                if (button.getText().equals(s))
                    button.visibleProperty().bind(boolsListed.get(s));
            }
        }
    }

    private void clearControllerButtons() {
        boolsListed.values().forEach(booleanProperty -> booleanProperty.setValue(false));
    }
}
