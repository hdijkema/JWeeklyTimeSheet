package net.dijkema.urenregistratie.data;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import net.dijkema.jndbm.NDbm;
import net.dijkema.jndbm.NDbm2;
import net.dijkema.jndbm2.exceptions.NDbmException;

public class Project {
	
	static int VERSION=1;
	
	String				_nummer;
	String 				_naam;
	Id     				_id;
	Vector<Id> 			_kostensoorten;
	Hashtable<Id,Kostensoort> _ksrt;
	boolean				_isActive;
	NDbm2  				_dbm;
	
	
	public Project(NDbm2 dbm,Id id) throws NDbmException {
		_dbm=dbm;
		_id=id;
		_kostensoorten=new Vector<Id>();
		_ksrt=new Hashtable<Id,Kostensoort>();
		_isActive=true;
		read();
	}
	
	public Project(NDbm2 dbm,String nummer,String naam) throws NDbmException {
		_dbm=dbm;
		_nummer=nummer;
		_naam=naam;
		_id=new Id();
		_kostensoorten=new Vector<Id>();
		_ksrt=new Hashtable<Id,Kostensoort>();
		_isActive=true;
		write();
	}
	
	private void read() throws NDbmException {
		int typeVersion=_dbm.getInt(_id.get("version"));
		_nummer=_dbm.getStr(_id.get("nummer"));
		_naam=_dbm.getStr(_id.get("naam"));
		_isActive=_dbm.getBoolean(_id.get("active"));
		Vector<String> str=_dbm.getVectorOfString(_id.get("kostensoorten"));
		Iterator<String> it=str.iterator();
		_kostensoorten=new Vector<Id>();
		while (it.hasNext()) {
			_kostensoorten.add(new Id(it.next()));
		}
	}
	
	private void write() throws NDbmException {
		_dbm.putInt(_id.get("version"), VERSION);
		_dbm.putStr(_id.get("nummer"), _nummer);
		_dbm.putStr(_id.get("naam"), _naam);
		_dbm.putBoolean(_id.get("active"),_isActive);
		Vector<String> str=new Vector<String>();
		Iterator<Id> it=_kostensoorten.iterator();
		while (it.hasNext())  { str.add(it.next().get()); }
		_dbm.putVectorOfString(_id.get("kostensoorten"), str);
	}
	
	public boolean isActive() {
		return _isActive;
	}
	
	public void setActive(boolean b) throws NDbmException {
		_isActive=b;
		_dbm.putBoolean(_id.get("active"), _isActive);
	}
	
	public String toString() {
		return String.format("%5s %s",_nummer,_naam);
	}
	
	public Id id() {
		return _id;
	}
	
	public String getNaam() {
		return _naam;
	}
	
	public void setNaam(String nm) throws NDbmException {
		_naam=nm;
		_dbm.putStr(_id.get("naam"), _naam);
	}
	
	public int nKostensoorten() {
		return _kostensoorten.size();
	}
	
	public Kostensoort kostensoort(int i) throws NDbmException {
		Id id=_kostensoorten.get(i);
		Kostensoort k=_ksrt.get(id);
		if (k!=null) {
			return k;
		} else {
			_ksrt.put(id,new Kostensoort(_dbm,id));
			return kostensoort(i);
		}
	}
	
	public Kostensoort addKostensoort(String ksrt) throws NDbmException {
		Id i=new Id();
		Kostensoort k=new Kostensoort(_dbm,i,ksrt); 
		_kostensoorten.add(i);
		write();
		return k;
	}

	public int delKostensoort(Kostensoort obj) throws NDbmException {
		int i,N;
		for(i=0,N=this.nKostensoorten();i<N && obj!=this.kostensoort(i);i++);
		if (i!=N) {
			_kostensoorten.remove(i);
			obj.remove();
			write();
			return i;
		} else {
			return -1;
		}
	}
	
	public Kostensoort insertKostensoort(String ksrt,int index) throws NDbmException {
		Id i=new Id();
		Kostensoort k=new Kostensoort(_dbm,i,ksrt);
		_kostensoorten.insertElementAt(i,index);
		return k;
	}
	
	public void remove() throws NDbmException {
		int i,N;
		_dbm.begin();

		for(i=0;i<this.nKostensoorten();i++) {
			this.kostensoort(i).remove();
		}
		
		_dbm.remove(_id.get("nummer"));
		_dbm.remove(_id.get("naam"));
		_dbm.remove(_id.get("kostensoorten"));
		_nummer=_dbm.getStr(_id.get("nummer"));
		_naam=_dbm.getStr(_id.get("naam"));
		
		_dbm.commit();
	}

}
