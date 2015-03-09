package pollApp;

import java.util.ArrayList;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/*
 * @Author Adwait Kaley
 * 
 * This class is the Bean Class for Moderators.
 * 
 * */

public class Moderator 
{
	
	int id;
	
	@JsonInclude(Include.NON_EMPTY)
	@NotNull(message="Name cannot be Null",groups={SpecialViews.ViewModerator.class})
	@Size(min=1,message="Name should be minimum 1 Character long",groups={SpecialViews.ViewModerator.class})
	String name;
	
	
	@JsonInclude(Include.NON_EMPTY)
	@NotNull(message="Email cannot be Null",groups={SpecialViews.ViewModeratorWithoutName.class,SpecialViews.ViewModerator.class})
	@Size(min=1,message="Email should be minimum 1 Character long",groups={SpecialViews.ViewModeratorWithoutName.class,SpecialViews.ViewModerator.class})
	String email;
	
	
	@JsonInclude(Include.NON_EMPTY)
	@NotNull(message="Password cannot be Null",groups={SpecialViews.ViewModeratorWithoutName.class,SpecialViews.ViewModerator.class})
	@Size(min=1,message="Password should be minimum 1 Character long",groups={SpecialViews.ViewModeratorWithoutName.class,SpecialViews.ViewModerator.class})
	String password;
	
	String created_at;
	

	@JsonIgnore
	ArrayList<Poll> pollList;
	
	public Moderator() {
		// TODO Auto-generated constructor stub
	}
	
	public Moderator(String email, String password) {
		super();
		this.email = email;
		this.password = password;
		//pollList= new ArrayList<Poll>();
	}
	
	public Moderator(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		
	}

	public Moderator(int id, String name, String email, String password,
			String created_at) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.created_at = created_at;
		pollList= new ArrayList<Poll>();
	}

	
	
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
		
	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public ArrayList<Poll> getPollList() {
		return pollList;
	}

	public void setPollList(ArrayList<Poll> pollList) {
		this.pollList = pollList;
	}

	
	


}
