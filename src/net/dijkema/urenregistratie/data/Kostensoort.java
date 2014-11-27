package net.dijkema.urenregistratie.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import org.joda.time.DateTime;

import net.dijkema.jndbm.NDbm;
import net.dijkema.jndbm.NDbm2;
import net.dijkema.jndbm.NDbm2ObjectReader;
import net.dijkema.jndbm.NDbm2ObjectWriter;
import net.dijkema.jndbm.NDbmEncDec;
import net.dijkema.jndbm.NDbmObjectReader;
import net.dijkema.jndbm.NDbmObjectWriter;
import net.dijkema.jndbm2.exceptions.NDbmException;

public class Kostensoort {
	
	static int VERSION=1;
	
	class UrenReader implements NDbm2ObjectReader {
		public void nildata() {
			throw new RuntimeException("Uren kunnen niet nil zijn");
		}

		public void read(NDbmEncDec db, DataInput din) throws NDbmException {
			int size=db.readInt(din);
			int i;
			for(i=0;i<size;i++) {
				_uren[i]=db.readInt(din);
			}
		}
	}
	
	class UrenWriter implements NDbm2ObjectWriter {
		public void write(NDbmEncDec db, DataOutput dout) throws NDbmException {
			db.writeInt(dout, _uren.length);
			int i;
			for(i=0;i<_uren.length;i++) {
				db.writeInt(dout,_uren[i]);
			}
		}
	}
	
	private Id 		_id;
	private String 	_naam;
	private NDbm2	_dbm;
	private int		_uren[]=new int[366]; 
	private int     _budget;
	
	public Kostensoort(NDbm2 dbm,Id id) throws NDbmException {
		int version=dbm.getInt(id.get("version"));
		_naam=dbm.getStr(id.get("naam"));
		_budget=dbm.getInt(id.get("budget"));
		dbm.getObject(id.get("uren"),new UrenReader());
		_id=id;
		_dbm=dbm;
		init();
	}
	
	public Kostensoort(NDbm2 dbm,Id id,String ksrt) throws NDbmException {
		_naam=ksrt;
		_id=id;
		dbm.putInt(id.get("version"), VERSION);
		dbm.putStr(_id.get("naam"), _naam);
		dbm.putInt(id.get("budget"), _budget);
		dbm.putObject(id.get("uren"),new UrenWriter());
		_dbm=dbm;
		init();
	}
	
	public void remove() throws NDbmException {
		_dbm.remove(_id.get("naam"));
		_dbm.remove(_id.get("uren"));
	}
	
	private void init() {
	}
	
	public float getBudget() {
		return _budget/10.0f;
	}
	
	public float getRestand() {
		int tot=0;
		for(int i=0;i<_uren.length;i++) { tot+=_uren[i]; }
		return (_budget-tot)/10.0f;
	}
	
	public void setBudget(float b) throws NDbmException {
		_budget=Math.round(b*10.0f);
		_dbm.putInt(_id.get("budget"), _budget);
	}
	
	public void setNaam(String nm) throws NDbmException {
		_naam=nm;
		_dbm.putStr(_id.get("naam"),_naam);
	}
	
	public String getNaam() {
		return _naam;
	}
	
	public String toString() {
		return _naam;
	}
	
	private int getDay(DateTime d) {
		return d.getDayOfYear();
	}
	
	public Float totaalUren() {
		int i;
		float f=0.0f;
		for(i=0;i<366;i++) {
			f+=_uren[i];
		}
		return f/10.0f;
	}
	
	public void writeUren() throws NDbmException {
		_dbm.putObject(_id.get("uren"), new UrenWriter());
	}
	
	public Float getUur(DateTime dag) {
		int idag=getDay(dag);
		if (idag<366) {
			return ((float) _uren[idag])/10.0f;
		} else {
			return null;
		}
	}
	
	public void setUur(DateTime dag,Float uren) throws NDbmException {
		int idag=getDay(dag);
		if (idag<366) {
			_uren[idag]=Math.round(uren*10.0f);
			writeUren();
		}
	}
}
