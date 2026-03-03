import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RoadSafetyTool {

    // --- COLOR CONSTANTS ---
    public static final String RESET  = "\u001B[0m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN   = "\u001B[36m";
    public static final String WHITE  = "\u001B[37m";
    public static final String ORANGE = "\u001B[38;5;208m";
    public static final String GOLD = "\u001B[38;5;220m";
    public static final String BOLD = "\u001B[1m";

public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        if (DatabaseConnection.getConnection() == null) {
            System.out.println("❌ Connection failed: Could not connect to Uranium. Check auth.cfg.");
            return;
        }

        printIntro();

        while (true) {
            System.out.print("\n" + CYAN + "Select option: " + RESET);
            input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("\n" + GREEN + "Exiting..." + "\n");
                break;
            }
            else if (input.equalsIgnoreCase("h")) {
                printHelp();
            } 
            else if (input.equalsIgnoreCase("admin reset")) {
                resetDatabase();
            } 
            else if (input.equalsIgnoreCase("q1")) {
                runQ1();
            } 
            else if (input.equalsIgnoreCase("q2")) {
                runQ2();
            } 
            else if (input.equalsIgnoreCase("q3")) {
                runQ3();
            } 
            else if (input.equalsIgnoreCase("q4")) {
                runQ4();
            } 
            else if (input.equalsIgnoreCase("q5")) {
                runQ5();
            } 
            else if (input.equalsIgnoreCase("q6")) {
                runQ6();
            } 
            else if (input.equalsIgnoreCase("q7")) {
                runQ7();
            } 
            else if (input.equalsIgnoreCase("q8")) {
                runQ8();
            } 
            else if (input.equalsIgnoreCase("q9")) {
                runQ9();
            } 
            else if (input.equalsIgnoreCase("q10")) {
                runQ10();
            } else if (input.equalsIgnoreCase("d")) {
                listAllDistricts(); 
            } 
            else if (input.toLowerCase().startsWith("d ")) {
                searchByDistrict(input.substring(2).trim()); // Search Specific
            }
            else if (input.equalsIgnoreCase("v")) {
                listAllMakes(); 
            } 
            else if (input.toLowerCase().startsWith("v ")) {
                searchVehicleModel(input.substring(2).trim()); // Search Specific
            }
            else if (input.toLowerCase().startsWith("s ")) {
                searchByDate(input.substring(2).trim());    // Search Specific
            } 
            else {
                System.out.println("Invalid command. Type 'h' for help.");
            }
        }
        scanner.close();
    }

    private static void printIntro() {        
        String line = "════════════════════════════════════════════════════════════════════════════════════════════════════════════";
        System.out.println();
        System.out.println(GOLD + "════════════════════════════════════════════════════════════════════════════════════════════════════════════");
        printCentered("ROAD SAFETY ANALYST TOOL - UK Accidents (2010-2015)", GOLD);
        
        System.out.println(GOLD + line + RESET);
        System.out.println();
        System.out.println("***Intro: This tool inspires from the dataset collected from the UK government with 2 main tables:");
        System.out.println();
        System.out.println("\t 💠 Accidents (which contains the detailed information about the accidents)");
        System.out.println();
        System.out.println("\t 💠 Vehicles (which contains the detailed information about the vehicles involved in the accidents.");
        System.out.println();
        System.out.println("In addition, some general information about drivers (age band, sex, home area) will also be reported.");
        System.out.println();
        System.out.println("All together help provide an overall picture about road accidents in the UK for public safety improvement.");
        System.out.println();
        System.out.println("Both policymakers and general public anywhere can educate about trafﬁc safety through the ﬁndings of this dataset.");
        System.out.println("\n" + YELLOW + "h   ----- 🙋‍♂️ Get help. 🙋‍♂️");
        System.out.println("\n" + YELLOW + "q   ----- 🔚 Exit. 🔚");
        System.out.println(GOLD + "════════════════════════════════════════════════════════════════════════════════════════════════════════════");
    }

    private static void printHelp() {
        System.out.println("\n*** Commands:");
        System.out.println("\n" + PURPLE + "q1  ----- Top 10 makes with MOST accidents.");
        System.out.println("\n" + PURPLE + "q2  ----- Top 10 makes with LEAST accidents.");
        System.out.println("\n" + PURPLE + "q3  ----- Fatalities by driver age band.");
        System.out.println("\n" + PURPLE + "q4  ----- Accidents by severity and weather.");
        System.out.println("\n" + PURPLE + "q5  ----- The accident with the most casualties.");
        System.out.println("\n" + PURPLE + "q6  ----- Accident rates: Urban vs Rural (Deprived Drivers).");
        System.out.println("\n" + PURPLE + "q7  ----- Most dangerous hour by Day of Week.");
        System.out.println("\n" + PURPLE + "q8  ----- Accidents by Speed Limit.");
        System.out.println("\n" + PURPLE + "q9  ----- Accidents by Light Conditions.");
        System.out.println("\n" + PURPLE + "q10 ----- Top 10 High-Risk Junctions (Casualties/Accident).");
        System.out.println("---------------------------------------------------------");
        System.out.println("d             ----- List ALL Districts (Local Authorities).");
        System.out.println("d <name>      ----- Search District Stats (Accidents + Police %).");
        System.out.println("v             ----- List ALL Vehicle Makes.");
        System.out.println("v <make>      ----- See models for a specific make.");
        System.out.println("v <make>, <n> ----- See top N popular models for a make.");
        System.out.println("s <yyyy-mm>   ----- Search accidents by month/year.");
        System.out.println("\n" + ORANGE + "🚨 admin reset 🚨   ----- ⚠️ WARNING: ⚠️ Deletes and repopulates database.");
        System.out.println("\n" + YELLOW + "h   ----- 🙋‍♂️ Help 🙋‍♂️");
        System.out.println("\n" + YELLOW + "q   ----- 🔚 Exit 🔚");
    }

    // --- Q1: Top 10 Makes (Most Accidents) ---
    private static void runQ1() {
        System.out.println("\n--- Q1: Top 10 Makes with Most Accidents ---");
        String schema = DatabaseConnection.SCHEMA;
        String sql = "SELECT TOP 10 m.Make, COUNT(*) AS Count " +
                     "FROM " + schema + ".Vehicles v " +
                     "JOIN " + schema + ".Make m ON v.MakeID = m.MakeID " +
                     "GROUP BY m.Make ORDER BY Count DESC";
        runSimpleQuery(sql, "Make", "Accidents");
        
        printInterpretation("High accident counts typically correlate with sales volume (e.g., Ford/Vauxhall).\n" +
                            "   However, high numbers can also indicate makes driven by higher-risk demographics.");
    }

    // --- Q2: Top 10 Makes (Least Accidents) ---
    private static void runQ2() {
        System.out.println("\n--- Q2: Top 10 Makes with Fewest Accidents (Min 1) ---");
        String schema = DatabaseConnection.SCHEMA;
        String sql = "SELECT TOP 10 m.Make, COUNT(*) AS Count " +
                     "FROM " + schema + ".Vehicles v " +
                     "JOIN " + schema + ".Make m ON v.MakeID = m.MakeID " +
                     "GROUP BY m.Make ORDER BY Count ASC";
        runSimpleQuery(sql, "Make", "Accidents");

        printInterpretation("These brands appear 'safest', likely due to low ownership numbers (Exotic cars)\n" +
                            "   or specific usage patterns (e.g., specialized utility vehicles not often on highways).");
    }

    // --- Q3: Fatalities by Driver Age Band ---
    private static void runQ3() {
        System.out.println("\n--- Q3: Fatal Accidents by Driver Age Band ---");
        String schema = DatabaseConnection.SCHEMA;
        // Adjust 'Fatal' to '1' if your raw data uses numbers
        String sql = "SELECT d.DriverAgeBand, COUNT(*) AS FatalCount " +
                     "FROM " + schema + ".Vehicles v " +
                     "JOIN " + schema + ".Accidents a ON v.AccidentID = a.AccidentID " +
                     "JOIN " + schema + ".DriverAgeBand d ON v.DriverAgeBandID = d.DriverAgeBandID " +
                     "WHERE a.Severity = 'Fatal' " + 
                     "GROUP BY d.DriverAgeBand ORDER BY FatalCount DESC";
        runSimpleQuery(sql, "Age Band", "Fatalities");

        printInterpretation("The high fatality rate in young adults (16-25) aligns with higher risk-taking behavior.\n" +
                            "   Note: Fatalities in the '6-10' range typically represent cyclists or unauthorized vehicle usage.");
    }

    // --- Q4: Accidents by Weather (
    private static void runQ4() {
        System.out.println("\n--- Q4: Accident Severity Analysis by Weather ---");
        String schema = DatabaseConnection.SCHEMA;

        // COMPLEX QUERY: Calculates total AND percentage of severe crashes
        String sql = "SELECT TOP 10 w.WeatherDesc, " +
                     "COUNT(*) as Total, " +
                     "SUM(CASE WHEN a.Severity IN ('Fatal', 'Serious') THEN 1 ELSE 0 END) as SevereCount, " +
                     "(CAST(SUM(CASE WHEN a.Severity IN ('Fatal', 'Serious') THEN 1 ELSE 0 END) AS FLOAT) / COUNT(*)) * 100 as PctSevere " +
                     "FROM " + schema + ".Accidents a " +
                     "JOIN " + schema + ".WeatherCondition w ON a.WeatherID = w.WeatherID " +
                     "GROUP BY w.WeatherDesc " +
                     "ORDER BY Total DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.printf("%-30s | %-10s | %-15s%n", "Weather", "Total", "% Serious/Fatal");
            System.out.println("-------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-30s | %,10d | %6.2f%%%n", rs.getString(1), rs.getInt(2), rs.getDouble(4));
            }
        } catch (Exception e) { e.printStackTrace(); }

        printInterpretation("Most accidents occur in 'Fine' weather simply because people drive more often.\n" +
                            "   However, users are often negligent in 'Fine' conditions (higher speeds),\n" +
                            "   whereas bad weather (Snow/Fog) often forces drivers to slow down, potentially lowering severity.");
    }

    // --- Q5: The Single Worst Accident ---
    private static void runQ5() {
        System.out.println("\n--- Q5: The Accident with Most Casualties ---");
        String schema = DatabaseConnection.SCHEMA;
        String sql = "SELECT TOP 1 a.AccidentID, a.Casualties, d.FullDate, t.TimeText, " +
                     "w.WeatherDesc, r.RoadCondition " +
                     "FROM " + schema + ".Accidents a " +
                     "JOIN " + schema + ".[Dates] d ON a.DateID = d.DateID " +
                     "JOIN " + schema + ".[Times] t ON a.TimeID = t.TimeID " +
                     "JOIN " + schema + ".WeatherCondition w ON a.WeatherID = w.WeatherID " +
                     "JOIN " + schema + ".RoadCondition r ON a.RoadConditionID = r.RoadConditionID " +
                     "ORDER BY a.Casualties DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                System.out.println("Accident ID:    " + rs.getString("AccidentID"));
                System.out.println("Casualties:     " + rs.getInt("Casualties"));
                System.out.println("Date/Time:      " + rs.getString("FullDate") + " at " + rs.getString("TimeText"));
                System.out.println("Conditions:     " + rs.getString("WeatherDesc") + ", " + rs.getString("RoadCondition"));
            }
        } catch (Exception e) { e.printStackTrace(); }

        printInterpretation("After verification, the Record did exist with AccidentID: 20144100J0489 occurred in Hertfordshire (Police Force Code 41) on October 20, 2014.\n" +
                            "\n" +
                            "However, we couldn't find any news about it. However, for data integrity, we keep this finding.\n" +
                            "\n" +
                            "For assumption, it could be that in minor bus collisions, police sometimes record every single passenger on board as a \"casualty\" for insurance and legal completeness, even if they were only shaken up or had no visible injuries. Alternatively, it could be a simple data entry typo (e.g., 13 became 93).\n" + 
                            "\n" +
                            "The Discrepancy: News reports from that time (e.g., Watford Observer) mention a bus crash involving Arriva buses where \"several\" people were injured, or numbers closer to 12 to 15 people needing treatment.\n");
    }

    // --- Q6: Deprived Drivers (Urban vs Rural) ---
    private static void runQ6() {
        System.out.println("\n--- Q6: Accidents for Deprived Drivers (Deciles 1-3) ---");
        String schema = DatabaseConnection.SCHEMA;
        
        // Complex Calculation: Counts accidents for deprived drivers, grouped by Area Type.
        // Also calculates "Per 1000" relative to the total deprived drivers involved.
        String sql = "SELECT a.AccidentAreaType, COUNT(*) as Count, " +
                     "CAST(COUNT(*) AS FLOAT) * 1000 / (SELECT COUNT(*) FROM " + schema + ".Vehicles v2 WHERE v2.DriverIMD_ID BETWEEN 1 AND 3) as Per1000 " +
                     "FROM " + schema + ".Accidents a " +
                     "JOIN " + schema + ".Vehicles v ON a.AccidentID = v.AccidentID " +
                     "WHERE v.DriverIMD_ID BETWEEN 1 AND 3 " + // IMD 1-3 = Most Deprived
                     "GROUP BY a.AccidentAreaType " +
                     "ORDER BY Count DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.printf("%-15s | %-15s | %-20s%n", "Area Type", "Total Accidents", "Per 1000 Drivers");
            System.out.println("---------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-15s | %-,15d | %-20.2f%n", rs.getString(1), rs.getInt(2), rs.getDouble(3));
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        printInterpretation("Urban areas show ~3x more accidents for deprived drivers.\n" +
                            "   Suggests higher risk due to complex road networks and traffic volume in cities.");
    }

    // --- Q7: Most Dangerous Hour per Day ---
    private static void runQ7() {
        System.out.println("\n--- Q7: Most Dangerous Hour by Day of Week ---");
        String schema = DatabaseConnection.SCHEMA;

        // CTE + Window Function to find the MAX hour for EACH day
        String sql = "WITH HourlyStats AS (" +
                     "   SELECT d.DayOfWeek, t.Hour, COUNT(*) as Count " +
                     "   FROM " + schema + ".Accidents a " +
                     "   JOIN " + schema + ".[Dates] d ON a.DateID = d.DateID " +
                     "   JOIN " + schema + ".[Times] t ON a.TimeID = t.TimeID " +
                     "   GROUP BY d.DayOfWeek, t.Hour " +
                     "), RankedStats AS (" +
                     "   SELECT *, ROW_NUMBER() OVER(PARTITION BY DayOfWeek ORDER BY Count DESC) as rn " +
                     "   FROM HourlyStats" +
                     ") " +
                     "SELECT DayOfWeek, Hour, Count FROM RankedStats WHERE rn = 1 " +
                     "ORDER BY CASE DayOfWeek " +
                     "   WHEN 'Monday' THEN 1 WHEN 'Tuesday' THEN 2 WHEN 'Wednesday' THEN 3 " +
                     "   WHEN 'Thursday' THEN 4 WHEN 'Friday' THEN 5 WHEN 'Saturday' THEN 6 WHEN 'Sunday' THEN 7 END";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.printf("%-12s | %-20s | %-10s%n", "Day", "Peak Hour", "Accidents");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                int h = rs.getInt("Hour");
                String timeRange = String.format("%02d:00 - %02d:00", h, h+1);
                System.out.printf("%-12s | %-20s | %,10d%n", rs.getString("DayOfWeek"), timeRange, rs.getInt("Count"));
            }
        } catch (Exception e) { e.printStackTrace(); }

        printInterpretation("Peak hours usually align with Rush Hour (17:00 weekdays) and Noon on weekends.\n" +
                            "   This confirms UK patterns match global traffic trends.");
    }

    // --- Q8: Speed Limit Analysis ---
    private static void runQ8() {
        System.out.println("\n--- Q8: Accidents by Speed Limit ---");
        String schema = DatabaseConnection.SCHEMA;
        String sql = "SELECT SpeedLimit, COUNT(*) as Count " +
                     "FROM " + schema + ".Accidents " +
                     "GROUP BY SpeedLimit ORDER BY Count DESC";
        runSimpleQuery(sql, "Speed Limit (mph)", "Accidents");
        printInterpretation("The majority of accidents occur at 30mph (Urban zones).\n" +
                            "   This challenges the misconception that speed alone causes crashes; low-speed inattention is a major factor.");
    }

    // --- Q9: Light Conditions ---
    private static void runQ9() {
        System.out.println("\n--- Q9: Accidents by Light Conditions ---");
        String schema = DatabaseConnection.SCHEMA;
        String sql = "SELECT LightConditions, COUNT(*) as Count, " +
                     "(CAST(COUNT(*) AS FLOAT) / (SELECT COUNT(*) FROM " + schema + ".Accidents)) * 100 as Pct " +
                     "FROM " + schema + ".Accidents " +
                     "GROUP BY LightConditions ORDER BY Count DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.printf("%-30s | %-10s | %-10s%n", "Light Condition", "Total", "% Total");
            System.out.println("--------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-30s | %,10d | %5.2f%%%n", rs.getString(1), rs.getInt(2), rs.getDouble(3));
            }
        } catch (Exception e) { e.printStackTrace(); }

        printInterpretation("Most accidents occur in Daylight (approx 75%), challenging the idea that night driving is inherently riskier.\n" +
                            "   Higher traffic volumes during the day outweigh the visibility risks of night.");
    }

    // --- Q10: Most Dangerous Junctions ---
    private static void runQ10() {
        System.out.println("\n--- Q10: Top 10 Junction Types by Casualties per Accident ---");
        String schema = DatabaseConnection.SCHEMA;
        
        // Calculates Average Casualties per single Accident for each junction type
        String sql = "SELECT TOP 10 j.JunctionDetail, " +
                     "AVG(CAST(a.Casualties AS FLOAT)) as AvgCasualties, " +
                     "COUNT(*) as TotalAccidents " +
                     "FROM " + schema + ".Accidents a " +
                     "JOIN " + schema + ".JunctionDetail j ON a.JunctionDetailID = j.JunctionDetailID " +
                     "GROUP BY j.JunctionDetail " +
                     "HAVING COUNT(*) > 50 " + // Filter out rare outliers
                     "ORDER BY AvgCasualties DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.printf("%-40s | %-15s | %-10s%n", "Junction Type", "Avg Casualties", "Accidents");
            System.out.println("-------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-40s | %-15.3f | %,10d%n", rs.getString(1), rs.getDouble(2), rs.getInt(3));            }
        } catch (Exception e) { e.printStackTrace(); }

        printInterpretation("Intersections and Roundabouts often have higher casualty rates than straight roads.\n" +
                            "   Complex decision-making at junctions leads to worse outcomes when errors occur.");
    }

    // --- Helper for Interpretation ---
    private static void printInterpretation(String text) {
        System.out.println("\n💡 ANALYST INTERPRETATION:");
        System.out.println("   " + text);
        System.out.println("-------------------------------------------------------------");
    }

    // --- HANDLER for 's' commands ---
    private static void handleSearch(String arg) {
        if (arg.matches("\\d{4}-\\d{2}")) {
            searchByDate(arg);
        } else {
            searchByDistrict(arg);
        }
    }

    // --- SEARCH: By Date (With Validation) ---
    private static void searchByDate(String dateStr) {
        System.out.println("\n--- Search Results for " + dateStr + " ---");
        String[] parts = dateStr.split("-");
        
        // 1. Basic Parse
        int year;
        int month;
        try {
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid numbers. Please use format YYYY-MM.");
            return;
        }

        // 2. Logic Validation (Edge Cases)
        if (month < 1 || month > 12) {
            System.out.println("❌ Invalid Month: " + month + ". Must be between 01 and 12.");
            return;
        }
        
        // Check if year is within your dataset range (2010-2015)
        if (year < 2010 || year > 2015) {
            System.out.println("⚠️  Note: Our dataset only covers 2010 to 2015.");
            System.out.println("    Searching for " + year + " will likely return 0 results.");
        }

        String schema = DatabaseConnection.SCHEMA;
        String sql = "SELECT COUNT(*) as Total FROM " + schema + ".Accidents a " +
                     "JOIN " + schema + ".[Dates] d ON a.DateID = d.DateID " +
                     "WHERE d.Year = ? AND d.Month = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.printf("Total Accidents in %s: %,d%n", dateStr, rs.getInt("Total"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- List ALL Districts (4 Columns) ---
    private static void listAllDistricts() {
        System.out.println("\n--- All UK Local Authorities (Districts) ---");
        String schema = DatabaseConnection.SCHEMA;
        String sql = "SELECT LocalAuthorityName FROM " + schema + ".LocalAuthority ORDER BY LocalAuthorityName";
        printFourColumns(sql);
    }

    // ---  List ALL Makes (4 Columns) ---
    private static void listAllMakes() {
        System.out.println("\n--- All Vehicle Makes Registered in Database ---");
        String schema = DatabaseConnection.SCHEMA;
        String sql = "SELECT Make FROM " + schema + ".Make ORDER BY Make";
        printFourColumns(sql);
    }

    // --- HELPER: Prints any single-column query in a 4-column grid ---
    private static void printFourColumns(String sql) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            List<String> items = new ArrayList<>();
            while (rs.next()) {
                items.add(rs.getString(1));
            }

            int count = 0;
            for (String item : items) {
                // Print formatted to 30 chars wide
                System.out.printf("%-30.30s", item); 
                count++;
                // New line every 4 items
                if (count % 4 == 0) {
                    System.out.println();
                }
            }
            // Final newline if the last row wasn't full
            if (count % 4 != 0) System.out.println();
            
            System.out.println("\n(Total Records: " + count + ")");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // --- Search District (with Police Attendance %) ---
    private static void searchByDistrict(String district) {
        System.out.println("\n--- Search Results for District: " + district + " ---");
        String schema = DatabaseConnection.SCHEMA;
        
        // LOGIC:
        // 1. Join Authorities to Accidents.
        // 2. Count TOTAL accidents.
        // 3. Count accidents where PoliceAttendance = 1 (Assuming 1=Yes in raw data).
        // 4. Calculate Percentage.
        String sql = "SELECT l.LocalAuthorityName, " +
                     "COUNT(a.AccidentID) as Total, " +
                     "SUM(CASE WHEN a.PoliceAttendance = 'Yes' THEN 1 ELSE 0 END) as PoliceCount " +
                     "FROM " + schema + ".LocalAuthority l " +
                     "LEFT JOIN " + schema + ".Accidents a ON l.LocalAuthorityID = a.LocalAuthorityID " +
                     "WHERE l.LocalAuthorityName LIKE ? " +
                     "GROUP BY l.LocalAuthorityName " +
                     "ORDER BY Total DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + district + "%");
            ResultSet rs = pstmt.executeQuery();
            
            boolean found = false;
            
            System.out.printf("%-35s | %-15s | %-15s%n", "Local Authority", "Total Accidents", "Police Attended %");
            System.out.println("-----------------------------------------------------------------------");

            while (rs.next()) {
                found = true;
                int total = rs.getInt("Total");
                int police = rs.getInt("PoliceCount");
                double pct = (total == 0) ? 0.0 : ((double)police / total * 100);

                System.out.printf("%-35s | %,15d | %-14.1f%%%n", 
                    rs.getString("LocalAuthorityName"), 
                    total, 
                    pct);
            }
            
            if (!found) System.out.println("❌ No district matches found.");

        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- Vehicle Search Handler ---
    private static void searchVehicleModel(String input) {
        String make;
        int limit = -1; // Default: Show all (No limit)

        // Logic to parse "Make, Limit" or just "Make"
        if (input.contains(",")) {
            String[] parts = input.split(",");
            make = parts[0].trim();
            try {
                limit = Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Invalid number format for limit. Showing all results.");
            }
        } else {
            make = input.trim();
        }

        System.out.println("\n--- Search Results for Make: " + make + (limit > 0 ? " (Top " + limit + ")" : "") + " ---");
        String schema = DatabaseConnection.SCHEMA;

        // COMPLEX SQL:
        // 1. Joins Vehicles -> Model -> Make to count accidents per model.
        // 2. Uses Window Function SUM(COUNT(*)) OVER() to get the TOTAL accidents for that Make.
        // 3. Calculates the Percentage of the Make's total accidents that this model represents.
        
        String topClause = (limit > 0) ? "TOP " + limit + " " : "";

        String sql = "SELECT " + topClause + "mo.Model, COUNT(*) as Count, " +
                     "(CAST(COUNT(*) AS FLOAT) / SUM(COUNT(*)) OVER()) * 100 as PctOfMake " +
                     "FROM " + schema + ".Vehicles v " +
                     "JOIN " + schema + ".Model mo ON v.ModelID = mo.ModelID " +
                     "JOIN " + schema + ".Make ma ON v.MakeID = ma.MakeID " +
                     "WHERE ma.Make LIKE ? " +
                     "GROUP BY mo.Model " +
                     "ORDER BY Count DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + make + "%");
            ResultSet rs = pstmt.executeQuery();
            
            boolean found = false;
            System.out.printf("%-30s | %-10s | %-15s%n", "Model", "Accidents", "% of Make");
            System.out.println("-------------------------------------------------------------");
            
            while (rs.next()) {
                found = true;
                System.out.printf("%-30s | %,10d | %6.2f%%%n", 
                    rs.getString(1), rs.getInt(2), rs.getDouble(3));
            }
            
            if (!found) {
                System.out.println("❌ No models found for make: " + make);
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- Helper for simple 2-column queries ---
private static void runSimpleQuery(String sql, String col1, String col2) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf("%-30s | %-10s%n", col1, col2);
            System.out.println("-------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-30s | %,10d%n", rs.getString(1), rs.getInt(2));            }
        } catch (Exception e) { System.out.println(RED + "❌ Error: " + e.getMessage()); }
    }

    // --- ADMIN RESET LOGIC ---
    private static void resetDatabase() {
        System.out.println("⚠️  WARNING: Wiping database and reloading from SQL file...");
        String filePath = "COMP_3380_Group36_database.sql"; 
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
             
            if (conn == null) return;
            conn.setAutoCommit(false);

            System.out.println("Dropping old tables...");
            String schema = DatabaseConnection.SCHEMA;
            String[] tablesToDrop = {
                "Vehicles", "Accidents", "Maneuvre", "RestrictedLane", "LeavingRoad", "Towing", "SkidEvent", 
                "JunctionLocation", "HitObjectOffRoad", "HitObjectOnRoad", "IMD_Decile", "DriverAgeBand", 
                "FuelType", "VehicleType", "Model", "Make", "WeatherCondition", "SpecialCondition", 
                "RoadType", "RoadCondition", "LocalAuthority", "HighwayAuthority", "PoliceForce", 
                "JunctionDetail", "JunctionControl", "RoadHazard", "Times", "Dates"
            };
            
            for (String table : tablesToDrop) {
                try { stmt.executeUpdate("DROP TABLE IF EXISTS " + schema + "." + table); } catch (Exception e) {}
            }
            conn.commit();
            System.out.println("✔ Old tables dropped.");

            System.out.println("Importing new structure...");
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            StringBuilder sqlQuery = new StringBuilder();
            int count = 0;
            
            System.out.print("Progress (1 dot = 1000 inserts): ");
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.trim().startsWith("--")) continue;
                sqlQuery.append(line);
                if (line.trim().endsWith(";")) {
                    String query = sqlQuery.toString();
                    if (query.trim().toUpperCase().startsWith("INSERT")) {
                        stmt.addBatch(query);
                        count++;
                        if (count % 1000 == 0) {
                            stmt.executeBatch();
                            conn.commit();
                            System.out.print(".");
                        }
                    } else {
                        stmt.execute(query);
                        conn.commit();
                    }
                    sqlQuery.setLength(0);
                }
            }
            stmt.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("\n" + GREEN + "\n✔ Database successfully repopulated!");
            
        } catch (Exception e) {
            System.out.println("\n❌ Error repopulating database: " + e.getMessage());
        }
    }

    // --- Helper to Center Text ---
    private static void printCentered(String text, String color) {
        int width = 100; // "═══" line length
        int padSize = (width - text.length()) / 2;
        String padding = String.format("%" + padSize + "s", "");
        
        // Print: Padding + Color + Bold + Text + Reset
        System.out.println(padding + color + BOLD + text + RESET);
    }
}