package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the student database table.
 * 
 */
@Entity
@NamedQuery(name="Student.findAll", query="SELECT s FROM Student s")
public class Student implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_studenta")
	private int idStudenta;

	private String imie;

	private String nazwisko;

	//bi-directional many-to-one association to Katedra
	@ManyToOne
	private Katedra katedra;

	public Student() {
	}

	public int getIdStudenta() {
		return this.idStudenta;
	}

	public void setIdStudenta(int idStudenta) {
		this.idStudenta = idStudenta;
	}

	public String getImie() {
		return this.imie;
	}

	public void setImie(String imie) {
		this.imie = imie;
	}

	public String getNazwisko() {
		return this.nazwisko;
	}

	public void setNazwisko(String nazwisko) {
		this.nazwisko = nazwisko;
	}

	public Katedra getKatedra() {
		return this.katedra;
	}

	public void setKatedra(Katedra katedra) {
		this.katedra = katedra;
	}

}