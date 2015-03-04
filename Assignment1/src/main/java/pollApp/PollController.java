package pollApp;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@Configuration
@RestController
@EnableWebMvcSecurity
@RequestMapping(value="/api/v1")
public class PollController extends WebSecurityConfigurerAdapter 
{
	private final AtomicInteger mod_counter = new AtomicInteger();
	ArrayList<Moderator> moderatorList=new ArrayList<Moderator>();
	ArrayList<Poll> pollList=new ArrayList<Poll>();
	static int count=0;
	
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	        .httpBasic().and()
	        .csrf().disable()
	        .authorizeRequests()
	        .antMatchers(HttpMethod.GET,"/api/v1/").permitAll()
	        .antMatchers("/api/v1/polls/*").permitAll()
	        .antMatchers("/api/v1/moderators/*").fullyAuthenticated().anyRequest().hasRole("USER");
	    }
	 
	 @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth
	            .inMemoryAuthentication()
	                .withUser("foo").password("bar").roles("USER");
	    }



	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/moderators",method=RequestMethod.POST,consumes="application/json")
	public ResponseEntity addModerator(@Valid @RequestBody Moderator bodyelements) 
	{
				
		Moderator mobj=null;	
		String name=bodyelements.getName();
		String email=bodyelements.getEmail();
		String password=bodyelements.getPassword();
		mobj=new Moderator(mod_counter.incrementAndGet(),name,email,password,getCurrentTimestamp());
		moderatorList.add(mobj);
		return new  ResponseEntity<Moderator>(mobj,HttpStatus.CREATED);
		
	}
	
	@SuppressWarnings("rawtypes")
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity handleBadInput(MethodArgumentNotValidException e)
	{
		String errors="";
		for(FieldError obj: e.getBindingResult().getFieldErrors())
			{
				errors+=obj.getDefaultMessage();
				errors+="\n";
			}	
		return new ResponseEntity(errors,HttpStatus.BAD_REQUEST);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/moderators/{id}",method=RequestMethod.GET)
	public ResponseEntity getModerator(@PathVariable("id") int moderator_id) 
	{
		Moderator mobj=null;
		boolean modFound=false;
		for(Moderator modSearch : moderatorList)
		{
				if(modSearch.getId()==moderator_id)
				{
					modFound=true;
					mobj=modSearch;
				
				}
			
		}
			
	 if(!modFound)
		{
				String modnotFound="MODERATOR NOT FOUND";
				return new  ResponseEntity ("MODERATOR NOT FOUND",HttpStatus.BAD_REQUEST);
		}
		else
		{
				return new  ResponseEntity (mobj,HttpStatus.OK);
		}		
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/moderators/{id}",method=RequestMethod.PUT)
	public ResponseEntity editModerator(@PathVariable("id") int moderator_id,@RequestBody Moderator bodyelements) 
	{
		Moderator mobj=null;
		String email=bodyelements.getEmail();
		String password=bodyelements.getPassword();
		boolean modFound=false;
	
		for(Moderator modSearch : moderatorList)
			{
				if(modSearch.getId()==moderator_id)
				{
						modFound=true;
						mobj=modSearch;
						mobj.setEmail(email);
						mobj.setPassword(password);
						break;
				}
				
			}
				
		if(!modFound)
		{
				String modnotFound="MODERATOR NOT FOUND";
				return new ResponseEntity (modnotFound,HttpStatus.BAD_REQUEST);					
		}
		else
		{
			return new ResponseEntity(mobj,HttpStatus.CREATED);
		}
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/moderators/{moderator_id}/polls",method=RequestMethod.POST)
	public ResponseEntity createPoll(@PathVariable("moderator_id") int moderator_id,@Valid @RequestBody Poll bodyelements)
	{
		Poll pollObject=null;
		Moderator modObj=null;
		boolean pollAdded=false;
		
		String question=bodyelements.getQuestion();
		String startedAt=bodyelements.getStarted_at();
		String expiredAt=bodyelements.getExpired_at();
		String choice[]=bodyelements.getChoice();
		
		
		pollObject=new Poll(getRandomPollId(),question,startedAt,expiredAt,choice);
			
		pollList.add(pollObject);
	
		for(Moderator modSearch : moderatorList)
			{
				if(moderator_id==modSearch.getId())
				{			
					modObj=modSearch;
					modObj.getPollList().add(pollObject);
					pollAdded=true;
					break;
				}
			}
			
		if(pollAdded)
			return new ResponseEntity (pollObject,HttpStatus.CREATED);
		else
			return new ResponseEntity ("Poll was not added",HttpStatus.BAD_GATEWAY);
		
	}
	
	
	@JsonView(Pollview.ViewPollWithoutResult.class)
	@RequestMapping(value="/polls/{poll_id}",method=RequestMethod.GET)
	public ResponseEntity<Poll> viewPoll(@PathVariable("poll_id") String poll_id)
	{
		Poll pollObject=null;
			for(Poll pollSearch : pollList)
			{
				if(pollSearch.getId().equals(poll_id))
				{
					return new ResponseEntity<Poll>(pollSearch,HttpStatus.OK);
				}
			}
			
				return new ResponseEntity<Poll>(pollObject,HttpStatus.NO_CONTENT);
		
	}
	
	@RequestMapping(value="/moderators/{moderator_id}/polls/{poll_id}",method=RequestMethod.GET)
	public ResponseEntity viewModeratorPoll(@PathVariable("moderator_id") String moderator_id,@PathVariable("poll_id") String poll_id)
	{
		Poll pollObject=null;
		
			for(Poll pollSearch : pollList)
			{
				if(pollSearch.getId().equals(poll_id))
				{
					return new ResponseEntity<Poll>(pollSearch,HttpStatus.OK);
				}
			 }
			
			return new ResponseEntity("Poll Could not be found",HttpStatus.BAD_REQUEST);
		
	}
	
	@RequestMapping(value="/moderators/{moderator_id}/polls",method=RequestMethod.GET)
	public ResponseEntity viewAllPolls(@PathVariable("moderator_id") int moderator_id)
	{
		ArrayList<Poll> pollObject=null;
			
			for(Moderator modSearch : moderatorList)
			{
				if(modSearch.getId()==moderator_id)
				{
					return new ResponseEntity<ArrayList<Poll>>(modSearch.getPollList(),HttpStatus.OK);
				}
			}
			
			
				return new ResponseEntity("Poll could not be found",HttpStatus.BAD_REQUEST);
		
	}
	
	@RequestMapping(value="/moderators/{moderator_id}/polls/{poll_id}",method=RequestMethod.DELETE)
	public ResponseEntity<String> deletePoll(@PathVariable("moderator_id") int moderator_id,@PathVariable("poll_id") String poll_id)
	{
		boolean pollFound=false;
		
			for(Moderator modSearch : moderatorList)
			{
				if(modSearch.getId()==moderator_id)
				{
					
					for(int i=0;i<modSearch.getPollList().size();i++)
					{
						if(poll_id.equals(modSearch.getPollList().get(i).getId()))
						{
							pollFound=true;
							modSearch.getPollList().remove(i);
							int j=0;
							while(j<pollList.size())
							{
								if(pollList.get(j).getId().equals(poll_id))
									{pollList.remove(j);break;}
								j++;
							}
							
						}
							
					}
					
				}
			}
			
			if(pollFound)
				return new ResponseEntity<String>("Poll "+poll_id+" deleted successfully",HttpStatus.valueOf(204) );
			else
				return new ResponseEntity<String>("No such poll Found",HttpStatus.BAD_REQUEST);
		}

	
	@RequestMapping(value="/polls/{poll_id}",method=RequestMethod.PUT,params="choice")
   public  ResponseEntity<String> votePoll(@PathVariable("poll_id") String poll_id,@RequestParam("choice") int choice_id)
   {
		
		if(searchPollList(poll_id))
		{
			Poll pollObj=getPollObject(poll_id);
			
			int[] getCurrentReults=pollObj.getResults();
			
			
			getCurrentReults[choice_id]=getCurrentReults[choice_id]+1;
			
			pollObj.setResults(getCurrentReults);	
			
			for(Poll p: pollList)
			{
				if(p.getId().equals(poll_id))
				{
					p.setResults(getCurrentReults);
				}
			}
			
			Moderator modObj=getModeratorForPoll(poll_id);
			
			for(Poll pollSearch : modObj.getPollList())
			{
				if(pollSearch.getId().equals(poll_id))
				{
					pollSearch.setResults(getCurrentReults);
				}
			}
			
			return new ResponseEntity<String>("Poll Response "+poll_id+" was recorded  successfully",HttpStatus.valueOf(204));
		}
		else
		{
			return new ResponseEntity<String>("No Such Poll was Found",HttpStatus.BAD_REQUEST);
		}
			
		
		
   }
	
	
	public boolean searchPollList(String poll_id)
	{
			boolean flag=false;
			int j=0;
			while(j<pollList.size())
			{
				if(pollList.get(j).getId().equals(poll_id))
					{
					flag=true;
					break;
					}
				j++;
			}
			
			return flag;
			
	}
	public Poll getPollObject(String poll_id)
	{
		
			Poll pollObj=null;
			int j=0;
			while(j<pollList.size())
			{
				if(pollList.get(j).getId().equals(poll_id))
					{
					
					pollObj=pollList.get(j);
					break;
					}
				j++;
			}
			
			return pollObj;
			
	}
	
	public Moderator getModeratorForPoll(String poll_id)
	{
		Moderator modObj=null;
		for(Moderator modSearch : moderatorList)
		{
			
				for(int i=0;i<modSearch.getPollList().size();i++)
				{
					if(poll_id.equals(modSearch.getPollList().get(i).getId()))
					{
						modObj=modSearch;
						return modSearch;
					}
						
				}
		}
		return modObj;
		
	}
	
	public String getCurrentTimestamp()
	{
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String formattedDate = sdf.format(date);
		return formattedDate;
	}

	
	String getRandomPollId() 
	{
		count++;
		return Integer.toString(count,36);
	}


}
