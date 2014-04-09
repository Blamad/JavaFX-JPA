package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.FocusModel;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import model.Katedra;
import model.Student;

public class ViewController implements Initializable{

	@FXML
	private TableView table1, table2;
	@FXML
	private TextField field1, field2;
	@FXML
	private ChoiceBox choiceBox;
	@FXML
	private TableColumn fname,sname, dept, scut, name;
	@FXML
	private ContextMenu cm;
	
	private static final String PERSISTENCE_UNIT_NAME = "PT-J5";
	private static EntityManagerFactory factory;
	private static EntityManager em;
	
	//lista obserwowanych wartosci
		final ObservableList<StudentTable> sData = FXCollections.observableArrayList();
		final ObservableList<KatedraTable> kData = FXCollections.observableArrayList();
		final ObservableList<String> chData = FXCollections.observableArrayList();
		
		
		private StudentTable sSelectedItem;
		private KatedraTable kSelectedItem;
		private FocusModel<TableView> focus2;
		
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	 
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	    em = factory.createEntityManager();
	    
	    //populacja tabeli:
	    
	    //Tabela 1:
	    Query q = em.createQuery("select t from Student t");
	    List<Student> studentList = q.getResultList();
	    for (Student it : studentList) {
	      sData.add(new StudentTable(it));
	    }
	    
	    table1.setEditable(true);
	    fname = new TableColumn("First Name");
	    fname.setMinWidth(88);
	    sname = new TableColumn("Surname");
	    sname.setMinWidth(140);
	    dept = new TableColumn("Department");
	    dept.setMinWidth(104);
	    
	    
	    fname.setCellValueFactory(
                new PropertyValueFactory<StudentTable, String>("name"));
		//Edycja tabeli z miejsca		
		fname.setCellFactory(TextFieldTableCell.forTableColumn());
		fname.setOnEditCommit(
		    new EventHandler<CellEditEvent<StudentTable, String>>() {
		        @Override
		        public void handle(CellEditEvent<StudentTable, String> t) {
		            ((StudentTable) t.getTableView().getItems().get(
		                t.getTablePosition().getRow())
		                ).setName(t.getNewValue(), em);
		        }
		    }
		);
		
	    sname.setCellValueFactory(
                new PropertyValueFactory<StudentTable, String>("surname"));
		//Edycja tabeli z miejsca		
		sname.setCellFactory(TextFieldTableCell.forTableColumn());
		sname.setOnEditCommit(
		    new EventHandler<CellEditEvent<StudentTable, String>>() {
		        @Override
		        public void handle(CellEditEvent<StudentTable, String> t) {
		            ((StudentTable) t.getTableView().getItems().get(
		                t.getTablePosition().getRow())
		                ).setSurname(t.getNewValue(), em);
		        }
		    }
		);

	    dept.setCellValueFactory(
                new PropertyValueFactory<StudentTable, String>("katedra"));
		//Edycja tabeli z miejsca		
		dept.setCellFactory(ChoiceBoxTableCell.forTableColumn(chData));
		dept.setOnEditCommit(
		    new EventHandler<CellEditEvent<StudentTable, String>>() {
		        @Override
		        public void handle(CellEditEvent<StudentTable, String> t) {
		        	for(KatedraTable it: kData)
		        		if(it.getKatedra().getSkrot() == t.getNewValue())
				            ((StudentTable) t.getTableView().getItems().get(
				                t.getTablePosition().getRow())
				                ).setKatedra(it.getKatedra(), em);
					Platform.runLater(new Runnable()
					{
					    @Override
					    public void run()
					    {
					    	for(KatedraTable it: kData)
								if(it.getKatedra().getSkrot() == sSelectedItem.getKatedra()) {
									table2.requestFocus();
									table2.getSelectionModel().select(kData.indexOf(it));
									table2.getFocusModel().focus(kData.indexOf(it));
					    	}
					    }
				});
		        }
		    }
		);

	    
	    table1.setItems(sData);
	    table1.getColumns().clear();
	    table1.getColumns().addAll(fname,sname,dept);
	    
	    
	    //Tabela 2:
	    
	    q = em.createQuery("select t from Katedra t");
	    List<Katedra> katedraList = q.getResultList();
	    for (Katedra it : katedraList) {
	    	kData.add(new KatedraTable(it));
		}
	    
	    table2.setEditable(true);
		name = new TableColumn("Name");
		name.setMinWidth(266);
		scut= new TableColumn("Shortcut");
		scut.setMinWidth(66);
		
	    scut.setCellValueFactory(
                new PropertyValueFactory<StudentTable, String>("shortcut"));

	    name.setCellValueFactory(
                new PropertyValueFactory<StudentTable, String>("name"));

		table2.setItems(kData);
		table2.getColumns().clear();
		table2.getColumns().addAll(scut, name);

		
		//ChoiceBox
		for(Katedra it: katedraList) {
			chData.add(it.getSkrot());
		}
		choiceBox.setItems(chData);
		
		
		cm = new ContextMenu();
		MenuItem cmItem1 = new MenuItem("Delete Student");
		cmItem1.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		       em.getTransaction().begin();
		       System.out.println(sSelectedItem.getStudent().getImie());
		       em.remove(sSelectedItem.getStudent());
		       sData.remove(sSelectedItem);
		       em.getTransaction().commit();
		    }
		});

		cm.getItems().add(cmItem1);
		table1.addEventHandler(MouseEvent.MOUSE_CLICKED,
		    new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent e) {
		            if (e.getButton() == MouseButton.SECONDARY)  
		                cm.show(table1, e.getScreenX(), e.getScreenY());
		        }
		});
		
		//Pobieranie wybranego elementu
		table1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				sSelectedItem = (StudentTable)newValue;
					Platform.runLater(new Runnable()
					{
					    @Override
					    public void run()
					    {
					    	for(KatedraTable it: kData)
								if(it.getKatedra().getSkrot() == sSelectedItem.getKatedra()) {
									table2.requestFocus();
									table2.getSelectionModel().select(kData.indexOf(it));
									table2.getFocusModel().focus(kData.indexOf(it));
					    	}
					    }
				});
				
			}
		});
		
	}
	
	
	public void add() {
	  // create new student
	    em.getTransaction().begin();
	   	Student stud = new Student();
	    stud.setImie(field1.getText());
	    stud.setNazwisko(field2.getText());
	    for(KatedraTable it: kData)
	    	if(it.getKatedra().getSkrot() == choiceBox.getValue())
	    		stud.setKatedra(it.getKatedra());
	    em.persist(stud);
	    em.getTransaction().commit();
	    //add to sData as well
	    sData.add(new StudentTable(stud));
	    field1.clear();
	    field2.clear();
	}
	
	public void raport() {
		
	}
	
}
