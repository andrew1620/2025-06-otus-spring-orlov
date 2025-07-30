package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;
import ru.otus.hw.service.TestRunnerService;

@Command(group = "Test students")
@RequiredArgsConstructor
public class TestStudentShellCommands {

    private final TestRunnerService testRunnerService;

    @Command(command = "start", alias = {"s"}, description = "Start testing")
    public void start() {
        testRunnerService.run();
    }
}
