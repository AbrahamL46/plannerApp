import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Represents a task with a date, description, and completion status
 */
public class Event {
    private final LocalDate date;
    private final String description;
    private final BooleanProperty completed;

    public Event(LocalDate date, String description) {
        this.date = date;
        this.description = description;
        this.completed = new SimpleBooleanProperty(false);
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public BooleanProperty completedProperty() {
        return completed;
    }

    
}
