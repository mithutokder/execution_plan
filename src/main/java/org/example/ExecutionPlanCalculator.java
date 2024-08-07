package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExecutionPlanCalculator {

    /***
     * This method prepares the execution plan for the provided list of scripts
     * @param scripts the list of scripts.
     * @return execution plan with the scripts ids which can be executed sequentially.
     */
    public List<Integer> prepareExecutionPlan(List<VulnerabilityScript> scripts) {
        Map<Integer, List<Integer>> scriptAndDependencies = scripts.stream()
                .collect(Collectors.toMap(VulnerabilityScript::getScriptId,
                        VulnerabilityScript::getDependencies));
        List<Integer> executionPlan = new ArrayList<>();
        for (VulnerabilityScript script : scripts) {
            if (!executionPlan.contains(script.getScriptId())) {
                if (!(script.getDependencies() == null || script.getDependencies().isEmpty())) {
                    List<Integer> dependencies = new ArrayList<>();
                    prepareExecutionPlan(script.getDependencies(), scriptAndDependencies, dependencies);
                    executionPlan.addAll(dependencies);
                }
                executionPlan.add(script.getScriptId());
            }
        }
        return executionPlan;
    }

    /***
     * This recursive method takes one dependency list & adds it's dependents list into the
     * provided list
     * @param dependencies list of integers for which dependent script id is fetched.
     * @param scriptAndDependencies map of script id & it's dependent script ids for better search operation
     * @param results accumulator to append the script id
     */
    private void prepareExecutionPlan(List<Integer> dependencies,
                                      Map<Integer, List<Integer>> scriptAndDependencies,
                                      List<Integer> results) {
        for (Integer dependency : dependencies) {
            if (scriptAndDependencies.containsKey(dependency) && !scriptAndDependencies.get(dependency).isEmpty()) {
                prepareExecutionPlan(scriptAndDependencies.get(dependency), scriptAndDependencies, results);
            }
            results.add(dependency);
        }
    }
}
