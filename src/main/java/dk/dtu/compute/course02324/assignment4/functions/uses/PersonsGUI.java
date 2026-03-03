package dk.dtu.compute.course02324.assignment4.functions.uses;


import dk.dtu.compute.course02324.assignment4.functions.implementations.GenericComparator;
// import dk.dtu.compute.course02324.assignment4.functions.types.List;
import java.util.List;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * A GUI element that is allows the user to interact and
 * change a list of persons.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Carlos E. Budde, cesbu@dtu.dk
 */
public class PersonsGUI extends VBox {


    /**
     * The list of persons to be maintained in this GUI.
     * NOTE: must be Java standard List for Assignment 4a
     */
    final private List<Person> persons;

    final private double DEFAULT_WEIGHT = 99999.99;

    final private int DEFAULT_AGE = 999;

    /** Name with highest value in that map ^^^ */
    Label mostFrequentNameLabel = new Label("Most frequent name: ");

    /** Average weight of all persons in list */
    Label averageWeightLabel = new Label("Average weight: " + "kg");

    // Assignment 4a:
    //  Show Min and Max age of all Persons in list
    Label maxWeightLabel = new Label("Maximum weight: ");
    Label minWeightLabel = new Label("Minimum weight: ");

    GridPane gridPane;

    private GridPane personsPane;

    /**
     * Text area in which the exceptions are shown.
     */
    private TextArea textAreaExceptions;

    private Label statusLabel;

    /**
     * Constructor which sets up the GUI attached a list of persons.
     *
     * @param persons the list of persons which is to be maintained in
     *                this GUI component; it must not be <code>null</code>
     */
    public PersonsGUI(@NotNull List<Person> persons) {
        this.persons = persons;

        gridPane = new GridPane();

        gridPane.setVgap(5.0);
        gridPane.setHgap(5.0);

        // text filed for user entering a name
        Label nameLabel = new Label(" Name (not empty):  ");
        nameLabel.setMinSize(0, 23);
        TextField nameField = new TextField();
        nameField.setPrefColumnCount(8);
        nameField.setText("name");

        // Assignment 3: this is a Label about adding the Weight of a person;
        //      you must add a TextField to take the input of the user (a positive int/double)
        //      and pass it to the constructor of the new Person created in the list
        Label weightLabel = new Label(" Weight (> 0):   ");

        // text field for users to enter the weight of the person
        TextField weightField = new TextField();
        weightField.setPrefColumnCount(5);
        weightField.setText("0");
        weightField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                try {
                    Integer.parseInt(newValue);
                    weightField.setText(newValue);
                } catch (NumberFormatException e) {
                    if (newValue.isEmpty()) {
                        weightField.setText("0");
                    } else {
                        weightField.setText(oldValue);
                    }
                }
            }
        });

        // Assignment 4a: add a Label and TextField for a Person's age
        Label ageLabel = new Label(" Age:  ");

        TextField ageField = new TextField();
        ageField.setPrefColumnCount(5);
        ageField.setText("0");
        ageField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                try {
                    Integer.parseInt(newValue);
                    ageField.setText(newValue);
                } catch (NumberFormatException e) {
                    if (newValue.isEmpty()) {
                        ageField.setText("0");
                    } else {
                        ageField.setText(oldValue);
                    }
                }
            }
        });

        // TODO for all buttons installed below, the actions need to properly
        //      handle (catch) exceptions, and it would be nice if the GUI
        //      could also show the exceptions thrown by user actions on
        //      button pressed (cf. Assignment 2).

        // button for adding a new person to the list (based on
        // the name in the nameField and age in weightField)
        Button addButton = new Button("Add");
        addButton.setOnAction(
                e -> {
                    // Assignments 3 and 4a:
                    // a Person's weight and age should be input by the user in a TextField each
                    try {
                        Person person = new Person(nameField.getText(), Double.parseDouble(weightField.getText()), Integer.parseInt(ageField.getText()));
                        persons.add(person);
                        statusLabel.setText(person + " added!");
                    } catch (IllegalArgumentException add) {
                        textAreaExceptions.appendText(add.getMessage() + "\n");
                    }
                    // makes sure that the GUI is updated accordingly
                    update();
                });

        Comparator<Person> comparator = new GenericComparator<>();

        // button for sorting the list (according to the order of Persons,
        // which implement the interface Comparable, which is converted
        // to a Comparator by the GenericComparator above)
        Button sortButton = new Button("Sort");
        sortButton.setOnAction(
                e -> {
                    try {
                        persons.sort(comparator);
                        statusLabel.setText("List sorted!");
                    } catch (Exception sort) {
                        textAreaExceptions.appendText(sort.getMessage() + "\n");
                    }
                    // makes sure that the GUI is updated accordingly
                    update();
                });

        // button for clearing the list
        Button clearButton = new Button("Clear all");
        clearButton.setOnAction(
                e -> {
                    try {
                        persons.clear();
                        statusLabel.setText("Persons list cleared!");
                        textAreaExceptions.setText("");
                    } catch (Exception clear) {
                        textAreaExceptions.appendText(clear.getMessage() + "\n");
                    }
                    // makes sure that the GUI is updated accordingly
                    update();
                });

        Button timeButton = new Button("Time passes");

        TextField indexField = new TextField();
        indexField.setPrefColumnCount(2);
        NonNegativeIntegerWatcher indexWatcher = new NonNegativeIntegerWatcher(indexField, 0); // FIXME could be done in a slightly nicer way

        // button for adding a new person to the list at the given index (based on
        // the attributes in the nameField and the weightField
        // Assignment 3: implement "add at index" functionality
        Button addAtIndex = new Button("Add at index:");
        addAtIndex.setOnAction(
                e -> {
                    try {
                        Person person = new Person(nameField.getText(), Double.parseDouble(weightField.getText()), Integer.parseInt(ageField.getText()));
                        persons.add(Integer.parseInt(indexField.getText()), person);
                    } catch (IndexOutOfBoundsException add) {
                        textAreaExceptions.appendText(add.getMessage() + "\n");
                    }
                    // makes sure that the GUI is updated accordingly
                    update();
                });

        // elements that appear horizontally side-by-side
        // add age
        HBox nameAction = new HBox(nameLabel, nameField);
        HBox weightAction = new HBox(weightLabel, weightField);
        HBox ageAction = new HBox(ageLabel, ageField);
        HBox indexAction = new HBox(addAtIndex, indexField);

        // combines the above elements into vertically arranged boxes
        // which are then added to the left column of the grid pane
        VBox actionBox = new VBox(
                nameAction,
                weightAction,
                ageAction,
                addButton,
                indexAction,
                sortButton,
                clearButton,
                mostFrequentNameLabel,
                averageWeightLabel,
                maxWeightLabel,
                minWeightLabel);
        actionBox.setSpacing(5.0);
        gridPane.add(actionBox, 0, 0);

        // create the elements of the right column of the GUI
        // (scrollable person list) ...
        Label labelPersonsList = new Label("Persons:");

        personsPane = new GridPane();
        personsPane.setPadding(new Insets(5));
        personsPane.setHgap(5);
        personsPane.setVgap(5);

        // FIXME the exact values of the width, height and general layout
        //       of the below fields could be made much nicer
        ScrollPane scrollPane = new ScrollPane(personsPane);
        scrollPane.setMinWidth(300);
        scrollPane.setMaxWidth(300);
        scrollPane.setMinHeight(300);
        scrollPane.setMaxHeight(300);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // ... and adds these elements to the right-hand columns of
        // the grid pane
        VBox personsList = new VBox(labelPersonsList, scrollPane);
        personsList.setSpacing(5.0);
        gridPane.add(personsList, 1, 0);

        statusLabel = new Label("<status>");
        Label labelExceptions = new Label("Exceptions:");
        textAreaExceptions = new TextArea();
        textAreaExceptions.setMinWidth(250);
        textAreaExceptions.setMaxWidth(448);
        textAreaExceptions.setWrapText(true);
        textAreaExceptions.setText("");
        textAreaExceptions.setEditable(false);
        textAreaExceptions.setScrollTop(Double.MAX_VALUE);
        textAreaExceptions.setScrollLeft(Double.MIN_VALUE);

        ScrollPane scrollPane2 = new ScrollPane(textAreaExceptions);
        scrollPane2.setMinWidth(250);
        scrollPane2.setMaxWidth(450);
        scrollPane2.setMinHeight(200);
        scrollPane2.setMaxHeight(200);
        scrollPane2.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane2.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.getChildren().addAll(gridPane, statusLabel, labelExceptions, scrollPane2);
        this.setSpacing(5.0);

        // updates the values of the different components with the values
        // from the stack
        update();
    }

    /**
     * Updates the values of the GUI elements with the current values
     * from the list.
     */
    private void update() {
        personsPane.getChildren().clear();
        // adds all persons to the list in the personsPane (with
        // a delete button in front of it)
        for (int i=0; i < persons.size(); i++) {
            Person person = persons.get(i);
            Label personLabel = new Label(i + ": " + person.toString());
            Button deleteButton = new Button("Delete");
            int finalI = i;
            deleteButton.setOnAction(
                    e -> {
                        Person person2 = persons.remove(finalI);
                        statusLabel.setText(person2 + " at position " + finalI +" removed!");
                        update();
                    }
            );
            HBox entry = new HBox(deleteButton, personLabel);
            entry.setSpacing(5.0);
            entry.setAlignment(Pos.BASELINE_LEFT);
            personsPane.add(entry, 0, i);
        }

        String mostFrequentName = persons.stream().map(person -> person.name)
                .reduce("",(currentMax, name) -> {
                    // "" is start value, ( , ) is the two values being compared to each other
                    long currentCount = persons.stream().filter(person -> person.name.equals(currentMax)).count();
                    // using long instead of int because that is what count() returns
                    // filters for and **stores the count** of the name that is the current maximum
                    long newCount = persons.stream().filter(person -> person.name.equals(name)).count();
                    // filters and **stores the count** of the newest name
                    return newCount > currentCount ? name : currentMax;
                    // if the new name count is higher than the current maximum, then new name becomes currentMax
                    // else just keep the currentMax
                });

        // update display fields
        mostFrequentNameLabel.setText("Most frequent name: " + mostFrequentName);

        // Assignment 4a:
        //      compute the average weight of all persons in the list without using loops;
        //      instead use the stream()...map(...)...reduce(...) interfaces from Lecture 07

        // Note: there are other declarative interfaces of Java that can be useful here,
        //       see https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html

        double avgWeight = persons.stream().mapToDouble(person -> person.weight).average()
                .orElseGet(() -> 0.0);

        averageWeightLabel.setText("Average weight: " +  avgWeight + " kg");

        // Assignment 4a:
        //      compute the min and max age of all persons in the list without using loops;
        //      instead use the stream()...map(...)...reduce(...) interfaces from Lecture 07

        int maxAge = persons.stream().map(Person::getAge)
                .reduce((a, b) -> a > b ? a : b).orElseGet(() -> 0);

        // int maxAge2 = persons.stream().mapToInt(Person::getAge).max().orElseGet(() -> 0);

        maxWeightLabel.setText("Maximum age: " + maxAge);

        int minAge = persons.stream().map(Person::getAge)
                .reduce((a, b) -> a < b ? a : b).orElseGet(() -> 0);

        minWeightLabel.setText("Minimum age: " + minAge);


    }
}

/**
 * This class watches over a {@link TextField} and guarantees that
 * the field's values represent some non-negative integer value.
 * And the actual value can be obtained by the method {@link #getValue()}.
 */
class NonNegativeIntegerWatcher implements ChangeListener<String> {

    private TextField textField;

    private int init;

    /**
     * This constructor initializes the watcher on a text fields with some
     * initial value. Note that this constructor registers this watcher
     * as a {@link ChangeListener<String>} on the text field-
     *
     * @param textField the text field to watch over
     * @param init the initial value in the text field
     */
    public NonNegativeIntegerWatcher(@NotNull TextField textField, int init) {
        this.textField = textField;
        this.init = init;
        textField.setText(init + "");
        textField.textProperty().addListener(this);
    }

    @Override
    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        try {
            int i = Integer.parseInt(newValue);
            if (i >= 0) {
                textField.setText(newValue);
            } else {
                textField.setText(oldValue);
            }
        } catch (NumberFormatException e) {
            if (newValue.isEmpty()) {
                textField.setText("" + init);
            } else {
                textField.setText(oldValue);
            }
        }
    }

    /**
     * Returns the integer value currently represented in the watched text field.
     *
     * @return the integer value in the text field
     */
    public int getValue() {
        try {
            String text = textField.getText();
            int i = Integer.parseInt(text);
            if (i >= 0) {
                return i;
            }
        } catch (NumberFormatException e) {
        }
        return 0;
    }

}
