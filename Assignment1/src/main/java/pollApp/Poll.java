package pollApp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

/*
 * @Author Adwait Kaley
 * 
 * This class is the Bean Class for Polls.
 * 
 * */

public class Poll 
{
	
	String id;
	
	@JsonView(Pollview.ViewPollWithoutResult.class)
	@NotNull(message="question cannot be Null")
	@Size(min=1,message="question should be minimum 1 Character long")
	String question;
	
	@JsonView(Pollview.ViewPollWithoutResult.class)
	@NotNull(message="started_at cannot be Null")
	@Size(min=24,message="started_at should be in yyyy-MM-ddTHH:mm:ss.SSSZ format")
	
	String started_at;
	
	@JsonView(Pollview.ViewPollWithoutResult.class)
	@NotNull(message="expired_at cannot be Null")
	@Size(min=24,message="expired_at should be in yyyy-MM-ddTHH:mm:ss.SSSZ format")
	
	String expired_at;
	
	@JsonInclude(Include.NON_NULL)
	@JsonView(Pollview.ViewPollWithoutResult.class)
	@NotNull(message="choice cannot be Null")
	@Size(min=2,message="choice should contain atleast 2 options")
	String choice[];
	
	@JsonInclude(Include.NON_NULL)
	int results[];
	
	Poll(){}
	
	public Poll(String question, String started_at, String expired_at,
			String[] choice) {
		super();
		this.question = question;
		this.started_at = started_at;
		this.expired_at = expired_at;
		this.choice = choice;
	}
	public Poll(String id, String question, String started_at,
			String expired_at, String[] choice) {
		super();
		this.id = id;
		this.question = question;
		this.started_at = started_at;
		this.expired_at = expired_at;
		this.choice = choice;
		this.results=new int[choice.length];
		for(int i=0;i<choice.length;i++)
			this.results[i]=0;
	}
	public Poll(String id, String question, String started_at,
			String expired_at, String[] choice, int[] results) {
		super();
		this.id = id;
		this.question = question;
		this.started_at = started_at;
		this.expired_at = expired_at;
		this.choice = choice;
		this.results = results;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getStarted_at() {
		return started_at;
	}
	public void setStarted_at(String started_at) {
		this.started_at = started_at;
	}
	public String getExpired_at() {
		return expired_at;
	}
	public void setExpired_at(String expired_at) {
		this.expired_at = expired_at;
	}
	public String[] getChoice() {
		return choice;
	}
	public void setChoice(String[] choice) {
		this.choice = choice;
	}
	public int[] getResults() {
		return results;
	}
	public void setResults(int[] results) {
		this.results = results;
	}
	
	
		
}
