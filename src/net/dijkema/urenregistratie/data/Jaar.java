package net.dijkema.urenregistratie.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.Vector;

import net.dijkema.jndbm.NDbm2;
import net.dijkema.jndbm.NDbm2ObjectReader;
import net.dijkema.jndbm.NDbm2ObjectWriter;
import net.dijkema.jndbm.NDbmEncDec;
import net.dijkema.jndbm2.exceptions.NDbmException;
import net.dijkema.urenregistratie.dialogs.VerwijderProject;

public class Jaar {
	
	static int VERSION=1;
	
	private Vector<Project> _projecten;
	private Integer			_jaar;
	private NDbm2			_dbm;
	
	class JaarReader implements NDbm2ObjectReader {
		public void nildata() {
			_projecten=new Vector<Project>();
		}

		public void read(NDbmEncDec dec, DataInput din) throws NDbmException {
			_projecten=new Vector<Project>();
			int N=dec.readInt(din);
			while(N>0) {
				String sid=dec.readString(din);
				Id id=new Id(sid);
				Project p=new Project(_dbm,id);
				_projecten.add(p);
				N-=1;
			}
		}
	}
	
	class JaarWriter implements NDbm2ObjectWriter {
		public void write(NDbmEncDec enc, DataOutput dout) throws NDbmException {
			int N=_projecten.size();
			enc.writeInt(dout, N);
			for(int i=0;i<N;i++) {
				Project p=_projecten.get(i);
				Id id=p.id();
				enc.writeString(dout, id.get());
			}
		}
	}
	
	public NDbm2 db() {
		return _dbm;
	}
	
	
	public int maxDagen() {
		return 366;
	}
	
	public int nProjects() {
		int N=_projecten.size();
		return N;
	}
	
	public Project project(int i) {
		return _projecten.get(i);
	}
	
	public void addProject(Project p) throws NDbmException {
		_projecten.add(p);
		_dbm.putObject(_jaar.toString(), new JaarWriter());
	}

	public void removeProject(Project p) throws NDbmException {
		p.remove();
		_projecten.remove(p);
		_dbm.putInt(_jaar.toString()+".version", VERSION);
		_dbm.putObject(_jaar.toString(), new JaarWriter());
	}
	
	public Jaar(NDbm2 dbm,Integer jaar) throws NDbmException {
		_dbm=dbm;
		_jaar=jaar; 
		_dbm.getInt(_jaar.toString()+".version");
		_dbm.getObject(_jaar.toString(), new JaarReader());
	}


}
