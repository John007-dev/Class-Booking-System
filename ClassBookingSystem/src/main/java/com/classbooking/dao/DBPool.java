        package com.classbooking.dao; // Ensure this matches your package

        import java.sql.Connection;
        import java.sql.SQLException;
        import com.zaxxer.hikari.HikariConfig;
        import com.zaxxer.hikari.HikariDataSource;

        public class DBPool {
            private static HikariDataSource dataSource;

            static {
                HikariConfig config = new HikariConfig();
                // JDBC Connection URL for your ClassBookingDB
                config.setJdbcUrl("jdbc:mysql://localhost:3306/ClassBookingDB?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8");
                
                // Your MySQL credentials
                config.setUsername("root");     // Default for lab
                config.setPassword("password"); // Default for lab (ensure this is your actual password)
                
                // Explicitly set the Driver Class Name for MySQL Connector/J 8.x+
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");

                // HikariCP Recommended Performance Settings
                config.setMinimumIdle(5);           // Minimum number of idle connections
                config.setMaximumPoolSize(20);        // Maximum number of connections
                config.setConnectionTimeout(30000);   // Max time (ms) to wait for a connection
                
                // Optional: Other useful HikariCP settings
                // config.setIdleTimeout(600000);      // Max time (ms) an idle connection can stay in pool
                // config.setMaxLifetime(1800000);     // Max lifetime (ms) of a connection in the pool
                // config.addDataSourceProperty("cachePrepStmts", "true");
                // config.addDataSourceProperty("prepStmtCacheSize", "250");
                // config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

                try {
                    dataSource = new HikariDataSource(config);
                } catch (Exception e) {
                    System.err.println("FATAL: HikariCP DataSource could not be initialized for ClassBookingDB!");
                    e.printStackTrace();
                    throw new RuntimeException("Could not initialize HikariCP DataSource", e);
                }
            }

            public static Connection getConnection() throws SQLException {
                if (dataSource == null) {
                    throw new SQLException("HikariCP DataSource (ClassBookingDB) is not initialized. Check server logs.");
                }
                return dataSource.getConnection();
            }

            private DBPool() {} // Private constructor to prevent instantiation

            // Optional: Method to close the DataSource (e.g., on application shutdown via ServletContextListener)
            public static void closeDataSource() {
                if (dataSource != null && !dataSource.isClosed()) {
                    dataSource.close();
                    System.out.println("HikariCP DataSource (ClassBookingDB) has been closed.");
                }
            }
        }
        