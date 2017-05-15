package EmployeeGui;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import BasicCommonClasses.Login;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.WorkerAlreadyExists;
import EmployeeDefs.AEmployeeException.WorkerDoesNotExist;
import EmployeeImplementations.Manager;
import GuiUtils.RadioButtonEnabler;
import UtilsImplementations.InjectionFactory;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;

/**
 * ManageEmployeesTab - manages the employee tab
 * 
 * @author Shimon Azulay
 * @since 2017-01-04
 */
public class ManageEmployeesTab implements Initializable {

	@FXML
    private JFXListView<String> employeesList;

    @FXML
    private JFXTextField searchEmployee;

    @FXML
    private JFXTextField userTxt;

    @FXML
    private JFXPasswordField passTxt;

    @FXML
    private JFXComboBox<String> securityCombo;

    @FXML
    private JFXTextField securityAnswerTxt;

    @FXML
    private JFXRadioButton workerRadioBtn;

    @FXML
    private JFXRadioButton managerRadioBtn;
    
    @FXML
    private JFXButton finishBtn;
    
    @FXML
    private JFXButton removeEmployeesBtn;

	RadioButtonEnabler radioBtnCont = new RadioButtonEnabler();

	IManager manager = InjectionFactory.getInstance(Manager.class);

	static Logger log = Logger.getLogger(ManageEmployeesTab.class.getName());
	
	private HashSet<String> selectedEmployees = new HashSet<String>();
	
	private ObservableList<String> dataEmployees;
	
	private FilteredList<String> filteredDataEmployees;
	
//	private HashMap<String,>
	
	

	@FXML
	void finishBtnPressed(ActionEvent event) {
		try {
			manager.registerNewWorker(new Login(userTxt.getText(), passTxt.getText()));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | WorkerAlreadyExists e) {
			log.debug(e.getStackTrace());
			log.fatal(e.getMessage());
		}
		createEmployeesList();
		enableRemoveButton();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		userTxt.textProperty().addListener((observable, oldValue, newValue) -> {
			enableFinishBtn();
		});

		passTxt.textProperty().addListener((observable, oldValue, newValue) -> {
			enableFinishBtn();
		});

		securityAnswerTxt.textProperty().addListener((observable, oldValue, newValue) -> {
			enableFinishBtn();
		});

		radioBtnCont.addRadioButtons((Arrays.asList((new RadioButton[] { workerRadioBtn, managerRadioBtn }))));

		securityCombo.getItems().addAll("Q1", "Q2", "Q3", "Q4");


		RequiredFieldValidator validator2 = new RequiredFieldValidator();
		validator2.setMessage("Input Required");
		validator2.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());

		userTxt.getValidators().add(validator2);
		userTxt.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				userTxt.validate();
			}
		});

		RequiredFieldValidator validator3 = new RequiredFieldValidator();
		validator3.setMessage("Input Required");
		validator3.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());

		passTxt.getValidators().add(validator3);
		passTxt.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				passTxt.validate();
			}
		});

		RequiredFieldValidator validator4 = new RequiredFieldValidator();
		validator4.setMessage("Input Required");
		validator4.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());

		securityAnswerTxt.getValidators().add(validator4);
		securityAnswerTxt.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				securityAnswerTxt.validate();
			}
		});

		createEmployeesList();
		
		searchEmployee.textProperty().addListener(obs -> {
			String filter = searchEmployee.getText();
			if (filter == null || filter.length() == 0) {
				filteredDataEmployees.setPredicate(s -> true);
			} else {
				filteredDataEmployees.setPredicate(s -> s.contains(filter));
			}
		});
		
		employeesList.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(String item) {
				BooleanProperty observable = new SimpleBooleanProperty();
				observable.set(selectedEmployees.contains(item));

				observable.addListener((obs, wasSelected, isNowSelected) -> {
					if (isNowSelected) {
						selectedEmployees.add(item);
					} else {
						selectedEmployees.remove(item);
					}
					enableRemoveButton();

				});
				return observable;
			}
		}));
		
		enableFinishBtn();
		enableRemoveButton();
	}
	
	private void enableRemoveButton() {
		removeEmployeesBtn.setDisable(/* TODO check if the selected is now connected and */ selectedEmployees.isEmpty());
		
	}
	

	private void createEmployeesList() {
		
		selectedEmployees = new HashSet<String>();

		dataEmployees = FXCollections.observableArrayList();

		IntStream.range(0, 1000).mapToObj(Integer::toString).forEach(dataEmployees::add);

		filteredDataEmployees = new FilteredList<>(dataEmployees, s -> true);

		employeesList.setItems(filteredDataEmployees);    
	}

	private void enableFinishBtn() {
		finishBtn.setDisable(userTxt.getText().isEmpty() || passTxt.getText().isEmpty()
				|| securityAnswerTxt.getText().isEmpty());
	}

	@FXML
	private void radioButtonHandling(ActionEvent ¢) {
		radioBtnCont.selectRadioButton((RadioButton) ¢.getSource());
		enableFinishBtn();
	}

	@FXML
	void removeBtnPressed(ActionEvent event) {
		selectedEmployees.forEach(eml -> {
			try {
				manager.removeWorker(eml);
			} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure
					| WorkerDoesNotExist e) {
				log.debug(e.getStackTrace());
				log.fatal(e.getMessage());		
			}
		});
		createEmployeesList();
		enableRemoveButton();
	}

}
