package ikifp.plasmik.model;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="constructs")
public class Construct {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column
	@NotNull
	private String plazmidName;
	
	@Column
	@NotNull
	private String insertSequence;
	
	@Column
	@NotNull
	private String primers;
	
	@Column
	@NotNull
	private Date date;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column
	private byte[] map;
	
	//gettery settery:
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPlazmidName() {
		return plazmidName;
	}

	public void setPlazmidName(String plazmidName) {
		this.plazmidName = plazmidName;
	}

	public String getInsertSequence() {
		return insertSequence;
	}

	public void setInsertSequence(String insertSequence) {
		this.insertSequence = insertSequence;
	}

	public String getPrimers() {
		return primers;
	}

	public void setPrimers(String primers) {
		this.primers = primers;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public byte[] getMap() {
		return map;
	}

	public void setMap(byte[] map) {
		this.map = map;
	}

}
