package klx.tech.community.workshop.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatGPTConfig {

    public static final String CHATGPT_WORKSHOP = "CHATGPT_WORKSHOP";

    @Bean
    public String chatGptKey() {
        return System.getenv(CHATGPT_WORKSHOP);
    }

}
