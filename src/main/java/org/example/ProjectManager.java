package org.example;

import java.util.*;

public class ProjectManager {
    private static final Random random = new Random();
    private static int nextId = 1;

    public static Project generateProject() {
        int id = nextId++;
        String name = generateProjectName();
        return new Project(id, name);
    }

    public static void generateProjects(List<Project> projects, int count) {
        for (int i = 0; i < count; i++) {
            projects.add(generateProject());
        }
    }

    private static String generateProjectName() {
        String[] prefixes = {"Project", "System", "Platform", "Application", "Service", "Module", "Framework"};
        String[] adjectives = {"Smart", "Innovative", "Digital", "Automated", "Cloud", "Secure", "Efficient"};
        String[] domains = {"Management", "Analytics", "Monitoring", "Optimization", "Security", "Integration", "Automation"};

        String prefix = prefixes[random.nextInt(prefixes.length)];
        String adjective = adjectives[random.nextInt(adjectives.length)];
        String domain = domains[random.nextInt(domains.length)];

        return prefix + " " + adjective + " " + domain;
    }
    public static void assignProjects(List<Employee> employees, List<Project> projects, Map<Integer, List<Integer>> assignment) {
        for (Employee employee : employees) {
            int projectCount = random.nextInt(5) + 1;
            List<Integer> employeeProjectIds = new ArrayList<>();

            for (int i = 0; i < projectCount; i++) {
                Project randomProject = projects.get(random.nextInt(projects.size()));
                employeeProjectIds.add(randomProject.getId());
            }

            assignment.put(employee.getId(), employeeProjectIds);
        }
    }
}