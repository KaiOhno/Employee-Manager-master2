import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmployeeManagerController implements Initializable {

    @FXML
    private ListView<Employee> listView;
    @FXML
    private TextField idTextField, nameTextField;
    @FXML
    private ComboBox<String> jobComboBox, searchComboBox;
    @FXML
    private CheckBox fulltimeCheckBox;
    @FXML
    private ToggleGroup genderToggleGroup;
    @FXML
    private Label totalCountLabel, searchCountLabel;

    private List<Employee> employees;
    private String filename = "employees.csv";
    private List<Employee> filteredEmployees;

    public EmployeeManagerController() {
        this.employees = new ArrayList<>();
        this.filteredEmployees = new ArrayList<>();
        load();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jobComboBox.getItems().addAll("Director", "Manager", "Developer", "Tester", "Salesman");
        searchComboBox.getItems().addAll("Director", "Manager", "Developer", "Tester", "Salesman", "Fulltime", "Parttime", "Male", "Female", "Others");
        updateListView();
    }

    @FXML
    public void addEmployee() {
        int id = Integer.parseInt(idTextField.getText());
        String name = nameTextField.getText();
        String job = jobComboBox.getSelectionModel().getSelectedItem();
        boolean fulltime = fulltimeCheckBox.isSelected();
        String gender = ((RadioButton) genderToggleGroup.getSelectedToggle()).getText();

        Employee employee = new Employee(id, name, job, fulltime, gender);
        employees.add(employee);
        save();

        updateListView();
    }

    @FXML
    public void deleteEmployee() {
        Employee selectedEmployee = listView.getSelectionModel().getSelectedItem();
        employees.remove(selectedEmployee);
        save();

        updateListView();
    }

    @FXML
    public void searchEmployees() {
        String selectedItem = searchComboBox.getSelectionModel().getSelectedItem();
        filteredEmployees = employees.stream()
                .filter(employee -> employee.getJob().equalsIgnoreCase(selectedItem)
                        || employee.getGender().equalsIgnoreCase(selectedItem)
                        || (selectedItem.equalsIgnoreCase("Fulltime") && employee.isFulltime())
                        || (selectedItem.equalsIgnoreCase("Parttime") && !employee.isFulltime()))
                .collect(Collectors.toList());

        listView.getItems().setAll(filteredEmployees);
        searchCountLabel.setText("Search count: " + filteredEmployees.size());
    }

    private void load() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] employeeData = line.split(",");
                int id = Integer.parseInt(employeeData[0]);
                String name = employeeData[1];
                String job = employeeData[2];
                boolean fulltime = Boolean.parseBoolean(employeeData[3]);
                String gender = employeeData[4];

                Employee employee = new Employee(id, name, job, fulltime, gender);
                employees.add(employee);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No saved data found");
        } catch (IOException e) {
            System.out.println("Error loading saved data");
        }
    }

    private void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
            for (Employee employee : employees) {
                writer.write(String.format("%d,%s,%s,%s,%s", employee.getId(), employee.getName(), employee.getJob(), employee.isFulltime(), employee.getGender()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data");
        }
    }

    private void updateListView() {
        listView.getItems().setAll(employees);
        totalCountLabel.setText("Total count: " + employees.size());
    }

    @FXML
    public void clearFields() {
        idTextField.clear();
        nameTextField.clear();
        jobComboBox.getSelectionModel().clearSelection();
        fulltimeCheckBox.setSelected(false);
        genderToggleGroup.selectToggle(null);
    }

    @FXML
    public void resetSearch() {
        searchComboBox.getSelectionModel().clearSelection();
        searchCountLabel.setText("Search count: ");
        updateListView();
    }
}
