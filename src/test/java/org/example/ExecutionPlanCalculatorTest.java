package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ExecutionPlanCalculatorTest {

    ExecutionPlanCalculator executionPlanCalculator = new ExecutionPlanCalculator();

    @Test
    void shouldReturnExecutionOrderForValidInput() {
        List<VulnerabilityScript> scripts = Arrays.asList(
                new VulnerabilityScript(1, List.of(2, 3)),
                new VulnerabilityScript(2, List.of()),
                new VulnerabilityScript(3, List.of(4)),
                new VulnerabilityScript(4, List.of(5)),
                new VulnerabilityScript(5, List.of())
        );
        List<Integer> executionPlan = executionPlanCalculator.prepareExecutionPlan(scripts);
        String expectedResult = "2 > 5 > 4 > 3 > 1";
        String actualResult = executionPlan.stream().map(String::valueOf).collect(Collectors.joining(" > "));
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void shouldReturnExecutionOrderWhenOneDependentIdIsNotPresentInTheList() {
        List<VulnerabilityScript> scripts = Arrays.asList(
                new VulnerabilityScript(1, List.of(3, 5)),
                new VulnerabilityScript(3, List.of(7, 8))
        );
        List<Integer> executionPlan = executionPlanCalculator.prepareExecutionPlan(scripts);
        String expectedResult = "7 > 8 > 3 > 5 > 1";
        String actualResult = executionPlan.stream().map(String::valueOf).collect(Collectors.joining(" > "));
        Assertions.assertEquals(expectedResult, actualResult);
    }
}
