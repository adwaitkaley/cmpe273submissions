package pollApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class PollApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(PollApplication.class,args);
	}

}
