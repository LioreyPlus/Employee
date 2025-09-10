package org.example;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BranchGenerator {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    private static int lastId = 0;
    private static int managerCounter = 2;


    private static final String[] BRANCH_NAMES = {
            "Central", "Northern", "Southern", "Eastern", "Western",
            "Main", "City", "Regional", "Capital", "Metropolitan",
            "Business", "Commercial", "Financial", "Corporate", "Headquarters",
            "Downtown", "Uptown", "International", "Global", "National"
    };

    private static final String[] BRANCH_TYPES = {
            "Branch", "Office", "Center", "Division", "Representative Office",
            "Department", "Agency", "Bureau", "Unit", "Facility",
            "Outlet", "Location", "Site", "Premises", "Establishment"
    };


    public static Branch createRandomBranch() {
        int id = ++lastId;
        String name = generateBranchName();
        String address = faker.address().fullAddress();
        int managerId = managerCounter++;

        return new Branch(id, name, address, managerId);
    }


    public static List<Branch> createRandomBranches(int count) {
        List<Branch> branches = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            branches.add(createRandomBranch());
        }

        return branches;
    }


    private static String generateBranchName() {
        String prefix = BRANCH_NAMES[random.nextInt(BRANCH_NAMES.length)];
        String suffix = BRANCH_TYPES[random.nextInt(BRANCH_TYPES.length)];

        return prefix + " " + suffix;
    }

}