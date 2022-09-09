package spending_management_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import spending_management_project.po.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class SpendingManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpendingManagementApplication.class,args);
    }
}
