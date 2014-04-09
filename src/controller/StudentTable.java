package controller;

import javax.persistence.EntityManager;

import javafx.beans.property.SimpleStringProperty;
import model.Katedra;
import model.Student;

public class StudentTable {

	private final SimpleStringProperty name;
	private final SimpleStringProperty surname;
	private final SimpleStringProperty katedra;
	private final Student stud;
	
	public StudentTable(Student student) {
		this.name = new SimpleStringProperty(student.getImie());
		this.surname = new SimpleStringProperty(student.getNazwisko());
		this.katedra = new SimpleStringProperty(student.getKatedra().getSkrot());
		this.stud = student;
	}
	
	public Student getStudent() {
		return stud;
	}
	
	public String getName() {
		return name.get();
	}
	
	public void setName(String newName, EntityManager em) {
		name.set(newName);
		em.getTransaction().begin();
		stud.setImie(newName);
		em.merge(stud);
		em.getTransaction().commit();
	}
	
	public String getSurname() {
		return surname.get();
	}
	
	public void setSurname(String newSurname, EntityManager em) {
		surname.set(newSurname);
		em.getTransaction().begin();
		stud.setNazwisko(newSurname);
		em.merge(stud);
		em.getTransaction().commit();
	}
	
	public String getKatedra() {
		return katedra.get();
	}
	
	public void setKatedra(Katedra newKat, EntityManager em) {
		katedra.set(newKat.getSkrot());
		em.getTransaction().begin();
		stud.setKatedra(newKat);
		em.merge(stud);
		em.getTransaction().commit();
	}
	
}
