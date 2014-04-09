package controller;

import javafx.beans.property.SimpleStringProperty;
import model.Katedra;

public class KatedraTable {

	private final SimpleStringProperty shortcut;
	private final SimpleStringProperty name;
	private final Katedra kat;
	
	public KatedraTable(Katedra katedra) {
		this.shortcut = new SimpleStringProperty(katedra.getSkrot());
		this.name = new SimpleStringProperty(katedra.getNazwa());
		this.kat = katedra;
	}
	
	public Katedra getKatedra() {
		return kat;
	}
	
	public String getName() {
		return name.get();
	}
	
	public String getShortcut() {
		return shortcut.get();
	}
}
