package pollApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/*
 * @Author Adwait Kaley
 * 
 * This class acts as an initiator for Poll Application
 * 
 * */
@ComponentScan
@EnableAutoConfiguration
public class PollApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(PollApplication.class,args);
	}

}
