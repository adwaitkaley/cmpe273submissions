package pollApp;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;


public class Poll 
{
	
	String id;
	
	@JsonView(Pollview.ViewPollWithoutResult.class)
	@NotNull(message="question cannot be Null")
	@NotEmpty(message="question cannot be Empty")
	String question;
	
	@JsonView(Pollview.ViewPollWithoutResult.class)
	@NotNull(message="started_at cannot be Null")
	@NotEmpty(message="started_at cannot be Empty")
	String started_at;
	
	@JsonView(Pollview.ViewPollWithoutResult.class)
	@NotNull(message="expired_at cannot be Null")
	@NotEmpty(message="expired_at cannot be Empty")
	String expired_at;
	
	@JsonInclude(Include.NON_NULL)
	@JsonView(Pollview.ViewPollWithoutResult.class)
	@NotNull(message="expired_at cannot be Null")
	@NotEmpty(message="expired_at cannot be Empty")
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
