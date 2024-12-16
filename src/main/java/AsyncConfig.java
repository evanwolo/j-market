import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAsync
public class AsyncConfig {
    // You can also configure custom thread pools if needed
}