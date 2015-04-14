package pollApp;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.stereotype.Repository;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pollApp.SpecialViews.ViewModerator;
import pollApp.SpecialViews.ViewModeratorWithoutName;

import com.fasterxml.jackson.annotation.JsonView;

/*
 * @Author Adwait Kaley
 * 
 * This class is the Controller Class for the Poll Application.
 * 
 * */
@Repository
@Configuration
@RestController
@EnableWebMvcSecurity
@RequestMapping(value="/api/v1")
public class PollController extends WebSecurityConfigurerAdapter 
{
    @Autowired
    private ModeratorRepository repository;

    @Autowired
    private PollRepository pollRepository;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	        .httpBasic().and()
	        .csrf().disable()
	        .authorizeRequests()
	        .antMatchers(HttpMethod.GET,"/api/v1/").permitAll()
	        .antMatchers("/api/v1/polls/*").permitAll()
	        .antMatchers(HttpMethod.POST,"/api/v1/moderators").permitAll()
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
	public ResponseEntity addModerator(@Validated(ViewModerator.class) @RequestBody Moderator bodyelements) 
	{
				
		Moderator mobj=null;	
		String name=bodyelements.getName();
		String email=bodyelements.getEmail();
		String password=bodyelements.getPassword();
        int mod_count=(int)repository.count();
        mod_count++;
		mobj=new Moderator(mod_count,name,email,password,getCurrentTimestamp());
        repository.save(mobj);
		return new  ResponseEntity<Moderator>(mobj,HttpStatus.CREATED);
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity handleBadInput(MethodArgumentNotValidException e)
	{
		String errors;
		StringBuffer result = new StringBuffer();
		for(FieldError obj: e.getBindingResult().getFieldErrors())
			{
				result.append("{");
				result.append("\n");
				result.append("field : "+obj.getField()+",");
				result.append("\n");
				result.append("rejectedValue : "+obj.getRejectedValue()+",");
				result.append("\n");
				result.append("objectName : "+obj.getObjectName()+",");
				result.append("\n");
				result.append("code : "+obj.getCode()+",");
				result.append("\n");
				result.append("Message : "+obj.getDefaultMessage());
				result.append("\n");
				result.append("}\n");
				
			}	
		errors=result.toString();
	    return new ResponseEntity(errors,HttpStatus.BAD_REQUEST);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/moderators/{id}",method=RequestMethod.GET)
	public ResponseEntity getModerator(@PathVariable("id") int moderator_id) 
	{
		Moderator mobj=null;
		boolean modFound=false;
        mobj=repository.findById(moderator_id);
        if(mobj!=null) {
            modFound = true;
        }
			
	 if(!modFound)
		{
				String modnotFound="Moderator with ModeratorID : "+moderator_id+" not found !!";
				return new  ResponseEntity(modnotFound,HttpStatus.BAD_REQUEST);
		}
		else
		{
				return new  ResponseEntity(mobj,HttpStatus.OK);
		}		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/moderators/{id}",method=RequestMethod.PUT)
	public ResponseEntity editModerator(@PathVariable("id") int moderator_id,@Validated(ViewModeratorWithoutName.class) @RequestBody Moderator bodyelements) 
	{
		Moderator mobj=null;
		String email=bodyelements.getEmail();
		String password=bodyelements.getPassword();
		boolean modFound=false;

        mobj=repository.findById(moderator_id);
        if(mobj!=null)
        {
            modFound = true;
            mobj.setEmail(email);
            mobj.setPassword(password);
            repository.save(mobj);
        }
				
		if(!modFound)
		{
			String modnotFound="Moderator with ModeratorID : "+moderator_id+" NOT FOUND";
			return new ResponseEntity (modnotFound,HttpStatus.BAD_REQUEST);					
		}
		else
		{
			return new ResponseEntity(mobj,HttpStatus.OK);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/moderators/{moderator_id}/polls",method=RequestMethod.POST)
	public ResponseEntity createPoll(@PathVariable("moderator_id") int moderator_id,@Valid @RequestBody Poll bodyelements)
	{
		Poll pollObject=null;
		Moderator mobj=null;
		boolean pollAdded=false;
		
		String question=bodyelements.getQuestion();
		String startedAt=bodyelements.getStarted_at();
		String expiredAt=bodyelements.getExpired_at();
		String choice[]=bodyelements.getChoice();

        if(choice.length<2)
            return new ResponseEntity ("Choices Cannot be less than 2 !!!",HttpStatus.BAD_GATEWAY);

        int poll_count=(int)pollRepository.count();

		pollObject=new Poll(getRandomPollId(poll_count),question,startedAt,expiredAt,choice);

        pollRepository.save(pollObject);

        mobj=repository.findById(moderator_id);
        if(mobj!=null)
        {
            pollAdded = true;
            ArrayList<String> pollList=mobj.getPollList();
            if(pollList.size()==0)
            {
                pollList=new ArrayList<String>();
                pollList.add(pollObject.getId());
            }
            else
            {
                pollList.add(pollObject.getId());
            }
            mobj.setPollList(pollList);
            repository.save(mobj);
        }
			
		if(pollAdded)
			return new ResponseEntity (pollObject,HttpStatus.CREATED);
		else
			return new ResponseEntity ("Unable to add Poll.Please Try again Later !!!",HttpStatus.BAD_GATEWAY);
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@JsonView(SpecialViews.ViewPollWithoutResult.class)
	@RequestMapping(value="/polls/{poll_id}",method=RequestMethod.GET)
	public ResponseEntity viewPoll(@PathVariable("poll_id") String poll_id)
	{
        for(Poll pobj : pollRepository.findAll())
        {
            if(poll_id.equals(pobj.getId()))
            {
                return new ResponseEntity (pobj,HttpStatus.OK);
            }
        }

        return new ResponseEntity("No Poll with Poll ID : "+poll_id+" was Found",HttpStatus.BAD_REQUEST);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/moderators/{moderator_id}/polls/{poll_id}",method=RequestMethod.GET)
	public ResponseEntity viewModeratorPoll(@PathVariable("moderator_id") int moderator_id,@PathVariable("poll_id") String poll_id)
	{
        Moderator mobj=null;
        mobj=repository.findById(moderator_id);

        if(mobj!=null)
        {
            ArrayList<String> pollList=mobj.getPollList();
            if(pollList.size()!=0)
            {
                for(String poll : pollList)
                {
                    if(poll.equals(poll_id))
                        return new ResponseEntity (pollRepository.findById(poll_id),HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity("Poll Could not be found",HttpStatus.BAD_REQUEST);
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/moderators/{moderator_id}/polls",method=RequestMethod.GET)
	public ResponseEntity viewAllPolls(@PathVariable("moderator_id") int moderator_id)
    {
        Moderator mobj=null;
        mobj=repository.findById(moderator_id);

        if(mobj!=null)
        {
            ArrayList<String> pollList=mobj.getPollList();
            ArrayList<Poll> pollArrayList= new  ArrayList<Poll>();
            for(int i=0;i<pollList.size();i++)
            {
                Poll pobj=pollRepository.findById(pollList.get(i));
                pollArrayList.add(pobj);
            }
            return new ResponseEntity<ArrayList<Poll>>(pollArrayList,HttpStatus.OK);
        }
			
        return new ResponseEntity("Polls for ModeratorID :"+moderator_id+" could not be found",HttpStatus.BAD_REQUEST);
		
	}
	
	@RequestMapping(value="/moderators/{moderator_id}/polls/{poll_id}",method=RequestMethod.DELETE)
	public ResponseEntity<String> deletePoll(@PathVariable("moderator_id") int moderator_id,@PathVariable("poll_id") String poll_id)
	{
        for(Moderator mobj : repository.findAll())
        {
            ArrayList<String> pollList=mobj.getPollList();
            for(String poll: pollList)
                if(poll_id.equals(poll))
                {
                    Poll pobj=pollRepository.findById(poll_id);
                    pollRepository.delete(pobj);
                    pollList.remove(poll_id);
                    mobj.setPollList(pollList);
                    repository.save(mobj);
                    return new ResponseEntity<String>("Poll "+poll_id+" deleted successfully",HttpStatus.valueOf(204) );
                }
        }


				return new ResponseEntity<String>("No poll with PollID : "+poll_id+" Found",HttpStatus.BAD_REQUEST);
		}

	
	@RequestMapping(value="/polls/{poll_id}",method=RequestMethod.PUT,params="choice")
   public  ResponseEntity<String> votePoll(@PathVariable("poll_id") String poll_id,@RequestParam("choice") int choice_id)
   {

       boolean pollModified=false;
       Poll pobj=null;
       pobj=pollRepository.findById(poll_id);
       if(pobj!=null)
       {
           int results[]=pobj.getResults();
           results[choice_id]=results[choice_id]+1;
           pobj.setResults(results);
           pollRepository.save(pobj);
           pollModified=true;
       }

       if (pollModified)
           return new ResponseEntity<String>("Poll Response "+poll_id+" was recorded  successfully",HttpStatus.valueOf(204));
       else
           return new ResponseEntity<String>("No Such Poll was Found",HttpStatus.BAD_REQUEST);
   }
	

	public String getCurrentTimestamp()
	{
		Date date = new Date();
		String formattedDate = simpleDateFormat.format(date);
		return formattedDate;
	}

	
	String getRandomPollId(int count)
	{
		count=count+1000;
		return Integer.toString(count,36);
	}

    /*
    * The EmailNotifier() method is a scheduled method that
    * runs every five minutes to notify moderators of the
    * expired polls via Email.
    * */

    @Scheduled(fixedRate = 300000)
    public void EmailNotifier() {
        for(Poll pobj:pollRepository.findAll())
        {
            if(!pobj.isModeratorNotified()) {
                String moderatorEmail=getModeratorEmail(pobj.getId());
                try {
                    Date sysDate = simpleDateFormat.parse(getCurrentTimestamp());
                    Date pollDate = simpleDateFormat.parse(pobj.getExpired_at());
                    if (sysDate.after(pollDate)) {
                        String resultString = moderatorEmail+":010120357:Poll Result[";
                        for (int i = 0; i < pobj.getChoice().length; i++) {
                            if (i == pobj.getChoice().length - 1)
                                resultString += pobj.getChoice()[i] + "=" + pobj.getResults()[i] + "]";
                            else
                                resultString += pobj.getChoice()[i] + "=" + pobj.getResults()[i] + ",";
                        }
                        System.out.println(resultString);
                        PollProducer pollProducer = new PollProducer();
                        pollProducer.sendMessage("cmpe273-topic", resultString);
                        pobj.setModeratorNotified(true);
                        pollRepository.save(pobj);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /*
    * The getModeratorEmail() method gets the Moderator Email for a given poll
    * uniquely identified by the pollId
    * */
    public String getModeratorEmail(String pollId)
    {
        String moderatorEmail=null;
        for(Moderator mobj : repository.findAll())
        {
            for(String poll : mobj.getPollList())
            {
                if(pollId.equals(poll))
                {
                    moderatorEmail=mobj.getEmail();
                    return moderatorEmail;
                }

            }
        }
        return moderatorEmail;
    }



}
