package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the katedra database table.
 * 
 */
@Entity
@NamedQuery(name="Katedra.findAll", query="SELECT k FROM Katedra k")
public class Katedra implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_katedry")
	private int idKatedry;

	private String nazwa;

	private String skrot;

	//bi-directional many-to-one association to Student
	@OneToMany(mappedBy="katedra")
	private List<Student> students;

	public Katedra() {
	}

	public int getIdKatedry() {
		return this.idKatedry;
	}

	public void setIdKatedry(int idKatedry) {
		this.idKatedry = idKatedry;
	}

	public String getNazwa() {
		return this.nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public String getSkrot() {
		return this.skrot;
	}

	public void setSkrot(String skrot) {
		this.skrot = skrot;
	}

	public List<Student> getStudents() {
		return this.students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public Student addStudent(Student student) {
		getStudents().add(student);
		student.setKatedra(this);

		return student;
	}

	public Student removeStudent(Student student) {
		getStudents().remove(student);
		student.setKatedra(null);

		return student;
	}

}