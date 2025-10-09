package org.example.repository;

import org.example.Branch;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class BranchRepository {

    private static final String DB_URL = "jdbc:sqlite:src/main/resources/Employee.db";

    public void saveAll(List<Branch> branches) throws SQLException {
        String query = """
            INSERT OR REPLACE INTO branches (id, name, address, manager_id)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            for (Branch branch : branches) {
                pstmt.setInt(1, branch.getId());
                pstmt.setString(2, branch.getName());
                pstmt.setString(3, branch.getAddress());
                pstmt.setInt(4, branch.getManagerId());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        }
    }
}