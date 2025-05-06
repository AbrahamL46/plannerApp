import java.util.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.util.Pair;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * A simple JavaFX GUI application
 */

public class HelloWorld extends Application {

    private YearMonth currentYearMonth;
    private GridPane calendarGrid;
    private Label monthLabel;
    private Map<LocalDate, String> events = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    } //end main method

    @Override
    public void start(Stage primaryStage) {
        currentYearMonth = YearMonth.now();

        Label messageLabel = new Label("Welcome to [Planner App]");
        messageLabel.getStyleClass().add("label");

        Button prev = new Button("<");
        Button next = new Button(">");
        monthLabel = new Label();
        HBox header = new HBox(10, prev, monthLabel, next);
        header.setAlignment(Pos.CENTER);

        prev.setOnAction(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            drawCalendar();
        });

        next.setOnAction(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            drawCalendar();
        });

        /* 
        Text box1 = new Text("April 2025");
        box1.getStyleClass().add("text");
        */

        //Calendar creation
        //GridPane calendarGrid = new GridPane();
        calendarGrid = new GridPane();
        calendarGrid.setHgap(5);
        calendarGrid.setVgap(5);
        calendarGrid.setAlignment(Pos.CENTER);

        /* 
        YearMonth currentMonth = YearMonth.of(2025, 4);
        LocalDate firstOfMonth = currentMonth.atDay(1);
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; //makes 0 = Sunday
        int daysInMonth = currentMonth.lengthOfMonth();
        */

        //Day headers
        String[] days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        for(int i = 0; i < 7; i++) {
            /* 
            Label day = new Label(days[i]);
            day.setMinSize(50, 30);
            day.setAlignment(Pos.CENTER);
            calendarGrid.add(day, i, 0);
            */
            calendarGrid.add(new Label(days[i]), i, 0);
        } //end for loop

        HBox taskSummaryBox = new HBox(10, 
            new Label("Your Tasks:"), 
            new Button("Click for full list") {{
                setOnAction(e -> showFullTasksList());
            }}
        );
        taskSummaryBox.setAlignment(Pos.CENTER);
       
        //Add a task should use TODAY's date
        Button addTaskButton = new Button("Add a Task");
        addTaskButton.setOnAction(e -> showAddTaskDialogFor(LocalDate.now()));

        VBox root = new VBox(10, header, calendarGrid, taskSummaryBox, addTaskButton);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);

        drawCalendar();

        primaryStage.setScene(new Scene(root, 600, 700));
        primaryStage.setTitle("Planner App");
        primaryStage.show();

    } //end start method

    /** Re-builds the day buttons for the current YearMonth */
    private void drawCalendar() {
        //update month label
        monthLabel.setText(currentYearMonth.getMonth().name() + " " + currentYearMonth.getYear());

        //clear old day-cells (but keep the header row at row = 0)
        calendarGrid.getChildren().removeIf(node -> 
            GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0
        );

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int startDow = firstOfMonth.getDayOfWeek().getValue() % 7; //Sunday = 0
        int daysInMonth = currentYearMonth.lengthOfMonth();
        
        //populate day buttons
        int dayCounter = 1;
        for(int r = 1; r <= 6; r++) {
            for(int c = 0; c < 7; c++) {
                if((r == 1 && c < startDow) || dayCounter > daysInMonth) {
                    //empty cell
                    calendarGrid.add(new Label(""), c, r);
                } 
                else {
                    LocalDate date = currentYearMonth.atDay(dayCounter);
                    Button dayBtn = new Button(String.valueOf(dayCounter) + (events.containsKey(date) ? " *" : ""));
                    dayBtn.setMaxWidth(Double.MAX_VALUE);
                    dayBtn.setOnAction(e -> 
                        //maybe show details or allow edit
                        showAddTaskDialogFor(date)
                    );
                    calendarGrid.add(dayBtn, c, r);
                    dayCounter++;
                } //end if-else 
            } //end inner if
        } //end outer if
    } //end drawCalendar method

    private void showFullTasksList() {
        Stage stage = new Stage();
        VBox root = new VBox(8);
        root.setPadding(new Insets(10));
        //for each date/event pair, show YYYY-MM-DD: event text
        events.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(e -> root.getChildren().add(new Text(e.getKey() + ": " + e.getValue())));
        root.getChildren().add(new Text("Total Tasks: " + events.size()));  
        stage.setScene(new Scene(root, 300, 400));
        stage.setTitle("All Tasks");
        stage.show();
    } //end showFullTasksList method

    private void showAddTaskDialogFor(LocalDate date) {
        TextInputDialog dlg = new TextInputDialog(events.getOrDefault(date, ""));
        dlg.setHeaderText("Add/Edit task for " + date);
        dlg.showAndWait().ifPresent(text -> {
            events.put(date, text);
            drawCalendar(); //redraw right after adding
        });

    } //end showAddTaskDialogFor method
    
} //end HelloWorld class
