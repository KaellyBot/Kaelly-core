package com.github.kaysoro.kaellybot.core;

import com.github.kaysoro.kaellybot.core.service.IDiscordService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoreApplication  implements CommandLineRunner {

    private final IDiscordService discordService;

    public CoreApplication(IDiscordService discordService){
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