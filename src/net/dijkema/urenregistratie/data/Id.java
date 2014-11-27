package net.dijkema.urenregistratie.data;

import java.util.UUID;

public class Id {
	private String _id;
	
	private String makeId() {
		UUID u=UUID.randomUUID();
		String id=u.toString();
		return id;
	}
	
	public String toString() {
		return "id="+_id;
	}
	
	public String get(String type) {
		return type+":"+_id;
	}
	
	public String get() {
		return _id;
	}
	
	public Id() {
		_id=makeId();
	}
	
	public Id(String id) {
		_id=id;
	}

}
