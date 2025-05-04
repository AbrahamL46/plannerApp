import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Optional;

/**
 * A simple JavaFX GUI application
 */

public class HelloWorld extends Application {

    private HashMap<LocalDate, String> events = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    } //end main method

    @Override
    public void start(Stage primaryStage) {
        Label messageLabel = new Label("Welcome to [Planner App]");
        messageLabel.getStyleClass().add("label");

        Text box1 = new Text("April 2025");
        box1.getStyleClass().add("text");

        //Calendar creation
        GridPane calendarGrid = new GridPane();
        calendarGrid.setHgap(25);
        calendarGrid.setVgap(25);
        calendarGrid.setAlignment(Pos.CENTER);

        YearMonth currentMonth = YearMonth.of(2025, 4);
        LocalDate firstOfMonth = currentMonth.atDay(1);
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; //makes 0 = Sunday
        int daysInMonth = currentMonth.lengthOfMonth();

        //Day headers
        String[] days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        for(int i = 0; i < days.length; i++) {
            Label day = new Label(days[i]);
            day.setMinSize(50, 30);
            day.setAlignment(Pos.CENTER);
            calendarGrid.add(day, i, 0);
        } //end for loop

        //Day buttons
        int dayCount = 1;
        for(int r = 1; r <= 6; r++) {
            for(int c = 0; c < 7; c++) {
                if(r == 1 && c < startDayOfWeek) {
                    continue;
                }
                if(dayCount > daysInMonth) {
                    break;
                }

                LocalDate date = currentMonth.atDay(dayCount);
                Button dayButton = new Button(String.valueOf(dayCount));
                dayButton.setMinSize(50, 50);

                dayButton.setOnAction(e -> {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Add Event");
                    dialog.setHeaderText("Add event for: " + date.toString());
                    dialog.setContentText("Event:");
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(event -> {
                        events.put(date, event);
                        dayButton.setText(dayButton.getText() + "\n" + event);
                    });
                });

                calendarGrid.add(dayButton, c, r);
                dayCount++;
            } //end inner for loop
        } //end outer for loop

        //Mini "Your Tasks" summary and "Click for full list" button
        HBox taskSummaryBox = new HBox(10);

        Text taskList = new Text("Your Tasks:");
        taskList.getStyleClass().add("text");

        //Everything in vbox
        VBox vbox = new VBox(10, messageLabel, box1, calendarGrid, taskList);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.TOP_CENTER);


        Scene scene = new Scene(vbox, 800, 800);
        scene.getStylesheets().add("style.css");


        primaryStage.setScene(scene);
        primaryStage.setTitle("Planner App");
        primaryStage.show();



    } //end start method
    
} //end HelloWorld class
