package com.github.kaellybot.core;

import com.github.kaellybot.core.service.DiscordService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication(scanBasePackages="com.github.kaellybot")
@EnableReactiveMongoRepositories("com.github.kaellybot")
public class CoreApplication  implements CommandLineRunner {

    private final DiscordService discordService;

    public CoreApplication(DiscordService discordService){
        this.discordService = discordService;
    }

    @Override
    public void run(String... args) {
        discordService.startBot();
    }

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, ArrayUtils.EMPTY_STRING_ARRAY);
	}
}