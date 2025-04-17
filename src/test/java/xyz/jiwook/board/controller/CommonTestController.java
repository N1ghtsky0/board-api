package xyz.jiwook.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Import;
import xyz.jiwook.board.config.SecurityConfig;

@Import(SecurityConfig.class)
public class CommonTestController {
    ObjectMapper objectMapper = new ObjectMapper();
}
