package ru.fmtk.khlystov.yatt;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "YATT", version = "1.0", description = "Yet Another Task Tracker api"))
public class YattApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(YattApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}
}
