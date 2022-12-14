package ro.tuc.ds2020;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.validation.annotation.Validated;
import ro.tuc.ds2020.services.MeasurementService;
import ro.tuc.ds2020.services.ReceiverService;


import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@Validated
public class Ds2020Application extends SpringBootServletInitializer {


    private static ReceiverService receiverServiceStatic;

    @Autowired
    private ReceiverService receiverService;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Ds2020Application.class);
    }

    @PostConstruct
    public void init() {
        Ds2020Application.receiverServiceStatic = receiverService;
    }


    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(Ds2020Application.class, args);
        receive();
    }

    public static void receive(){
        //ReceiverService receiverService = new ReceiverService();
        System.out.println("here");
        try {
            receiverServiceStatic.receive();
        } catch (Exception e) {
            e.printStackTrace();
           // receive();
        }
    }
}
