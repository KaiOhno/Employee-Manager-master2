import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class EmployeeSearchController {
    @FXML
    private ComboBox<String> jobComboBox;
    @FXML
    private CheckBox fulltimeCheckBox;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private Button searchButton;
    @FXML
    private ListView<Employee> employeeListView;
    @FXML
    private Label resultsCountLabel;

    private EmployeeManagerController manager;

    public void initialize() {
        // Initialize jobComboBox and genderComboBox options
        // ...
        searchButton.setOnAction(event -> searchEmployees());
    }

    public void setManager(EmployeeManagerController manager) {
        this.manager = manager;
    }

    private void searchEmployees() {
        String job = jobComboBox.getValue();
        boolean fulltime = fulltimeCheckBox.isSelected();
        String gender = genderComboBox.getValue();

        List<Employee> results = manager.searchEmployees(job, fulltime, gender);
        employeeListView.getItems().setAll(results);
        resultsCountLabel.setText("Results: " + results.size());
    }
}
