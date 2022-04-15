package com.passwordmanager;
import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

public class PasswordManagement {
    static Connection con;
    static Statement st1, st2;
    static ResultSet rs1;
    public static void main(String[] args) {
        Password passwordObj = new Password();
        int option, label = 0,flag = 1, adminLable = 0;
        String temp;
        Scanner S = new Scanner(System.in);
        System.out.println("\n\t~~~ Password Management System ~~~\n");

        /// ---- Connection block ----
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ooad", "root", "");
            System.out.println("connected");
            st1 = con.createStatement();
            rs1 = st1.executeQuery("select * from users");
            st1.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        ///  ---- Login -----
        do {
            while (label == 0) {
                try {
                    System.out.print("\n  ~~~ Login ~~~\nEnter 1 if you want to login (or) Enter 0 if you want" +
                                     " to create new account (or) 2 to exit: ");
                    option = S.nextInt();
                    switch (option) {
                        case 0 -> {
                            try {
                                st2 = con.createStatement();
                                System.out.print("Enter your name: ");
                                passwordObj.setName(S.next());
                                System.out.print("Enter username: ");
                                passwordObj.setUsername(S.next());
                                System.out.print("Enter password: ");
                                passwordObj.setPassword(S.next());
                                st2.executeUpdate("insert into users values('" + passwordObj.getUsername() +
                                        "','" + passwordObj.getName() + "','" + passwordObj.getPassword() + "')");
                                System.out.println("Hai " + passwordObj.getName() + ",");
                                label = 1;
                                st2.close();
                            } catch (Exception e) {
                                System.out.println("User Already exists");
                                label = 0;
                            }
                        }
                        case 1 -> {
                            System.out.println("Enter your Username");
                            passwordObj.setUsername(S.next());
                            System.out.print("Enter Password: ");
                            passwordObj.setPassword(S.next());
                            try {
                                st1 = con.createStatement();
                                rs1 = st1.executeQuery("select * from users where username = '"
                                        + passwordObj.getUsername() + "' and password = '" +
                                        passwordObj.getPassword() + "'");
                                rs1.next();
                                System.out.println("Hai " + rs1.getString("name") + ",");
                                label = 1;
                                if (Objects.equals(passwordObj.getUsername(), "admin")) {
                                    adminLable = 1;
                                }
                                st1.close();
                            } catch (Exception e) {
                                System.out.println("Enter valid credentials");
                            }

                        }
                        case 2 -> {
                            System.out.println("bye...");
                            System.exit(0);
                        }
                        default -> System.out.println("Enter valid input");
                    }
                } catch (Exception e) {
                    System.out.println("bye");
                    System.exit(0);
                }
            }

            // ----- After Login for User ------
            while (label == 1 && adminLable == 0) {
                System.out.print("\nMenu\n1. Add new password details\n2. Delete existing password details\n" +
                                 "3. Update password\n4. View all passwords\n5. Delete my account\n" +
                                 "6. Logout\n7. Change account password\n8. Exit\n" +
                                 "Enter your option: ");
                option = S.nextInt();
                switch (option) {
                    case 1 -> {
                        try {
                            System.out.println(" ~~~ Adding new password ~~~");
                            System.out.print("Enter password domain: ");
                            passwordObj.setDomain(S.next());
                            System.out.print("Enter password: ");
                            passwordObj.setDomainPassword(S.next());
                            st2 = con.createStatement();
                            st2.executeUpdate("insert into passwords values('" + passwordObj.getUsername() +
                                    "','" + passwordObj.getDomain() + "','" + passwordObj.getDomainPassword() + "')");
                            st2.close();
                            System.out.println("Password successfully added");
                        } catch (Exception e) {
                            System.out.println("Can't add password");
                        }

                    }
                    case 2 -> {
                        try {
                            System.out.println(" ~~~ Password deletion ~~~");
                            System.out.print("Enter password domain name: ");
                            temp = S.next();
                            passwordObj.setDomain(temp);
                            st1 = con.createStatement();
                            rs1 = st1.executeQuery("select * from passwords where username = '" +
                                                   passwordObj.getUsername() + "' and domain = '" +
                                                   passwordObj.getDomain() + "'");
                            rs1.next();
                            System.out.print(rs1.getString("domain") + " ");
                            st1.close();
                            st1 = con.createStatement();
                            st1.executeUpdate("delete from passwords where username = '" + passwordObj.getUsername()
                                              + "' and domain = '" + temp + "'");
                            System.out.println("password successfully deleted");
                            st1.close();
                        } catch (Exception e) {
                            System.out.println("no such domain password is available..");
                        }
                    }
                    case 3 -> {
                        try {
                            System.out.println(" ~~~ Update password ~~~");
                            System.out.print("Enter domain name: ");
                            passwordObj.setDomain(S.next());
                            st1 = con.createStatement();
                            rs1 = st1.executeQuery("select * from passwords where username = '" +
                                    passwordObj.getUsername() + "' and domain = '" + passwordObj.getDomain() + "'");
                            rs1.next();
                            System.out.print("Enter new password for " + rs1.getString("domain") + ": ");
                            st1.close();
                            passwordObj.setDomainPassword(S.next());
                            st1 = con.createStatement();
                            st1.executeUpdate("update passwords set specific_password = '" +
                                    passwordObj.getDomainPassword() + "' where username = '" + passwordObj.getUsername()
                                    + "' and domain = '" + passwordObj.getDomain() + "'");
                            st1.close();
                            System.out.println("Password successfully updated");
                        } catch (Exception e) {
                            System.out.println("No such password domain found");
                        }
                    }
                    case 4 -> {
                        try {
                            System.out.println(" ~~~ All password details ~~~");
                            st1 = con.createStatement();
                            rs1 = st1.executeQuery("select * from passwords where username = '" +
                                    passwordObj.getUsername() + "'");

                            System.out.println("Domain\tPassword");
                            while (rs1.next()) {
                                System.out.print(rs1.getString("domain") + "\t");
                                System.out.println(rs1.getString("specific_password"));
                            }
                            System.out.println();
                            st1.close();
                        } catch (Exception e) {
                            System.out.println("Can't access password details");
                        }
                    }
                    case 5 -> {
                        try {
                            System.out.print("Are you sure to delete your account(yes-1, no-0): ");
                            int state = S.nextInt();
                            if (state == 1) {
                                st1 = con.createStatement();
                                st1.executeUpdate("delete from passwords where username = '" +
                                        passwordObj.getUsername() + "'");
                                st1.executeUpdate("delete from users where username = '" +
                                        passwordObj.getUsername() + "'");
                                st1.close();
                                label = 0;
                                passwordObj.deleteAll();
                                flag = 0;
                                System.out.println("Deletion successful");
                            }
                        } catch (Exception e) {
                            System.out.println("Deletion failed");
                        }
                    }
                    case 6 -> {
                        try {
                            label = 0;
                            passwordObj.deleteAll();
                            flag = 0;
                        } catch (Exception e) {
                            System.out.println("error");
                        }
                    }
                    case 7 -> {
                        System.out.println("Enter current password: ");
                        String s = S.next();
                        try {
                            st2 = con.createStatement();
                            rs1 = st2.executeQuery("select * from users where username = '" +
                                    passwordObj.getUsername() + "' and password = '" + s + "'");
                            st2.close();

                            // new password
                            System.out.println("Enter new Password: ");
                            String newpass = S.next();
                            st2 = con.createStatement();
                            st2.executeUpdate("update users set password = '" + newpass + "' where username = '" +
                                    passwordObj.getUsername() + "' and password = '" + s + "'");
                            passwordObj.setPassword(newpass);
                            st2.close();
                        } catch (Exception e) {
                            System.out.println("Enter correct password..");
                        }
                    }
                    case 8 -> {
                        System.out.println("Bye...");
                        System.exit(0);
                    }
                    default -> System.out.println("Enter valid option");
                }
            }

            /// After login for admin ---
            if(adminLable == 1){
                do {
                    System.out.println("\nMenu for Admin\n1.View users\n2.Delete user\n3.Change admin password\n" +
                                       "4.Logout\n5.Exit");
                    System.out.print("Enter option: ");
                    int adminOption = S.nextInt();
                    switch (adminOption) {
                        case 1 -> {
                            try {
                                System.out.println(" ~~~ All user details ~~~");
                                st1 = con.createStatement();
                                rs1 = st1.executeQuery("select * from users");
                                System.out.println("Username\t\tName");
                                while (rs1.next()) {
                                    System.out.print(rs1.getString("username") + "\t\t");
                                    System.out.println(rs1.getString("name"));
                                }
                                st1.close();
                            } catch (Exception e) {
                                System.out.println("Can't access password details");
                            }
                        }
                        case 2 -> {
                            try {
                                System.out.println(" ~~~ Delete user ~~~");
                                System.out.print("Enter username to delete: ");
                                String tempUsername = S.next();
                                st1 = con.createStatement();
                                rs1 = st1.executeQuery("select * from users where username = '" +tempUsername +"'");
                                rs1.next();
                                System.out.print("User " + rs1.getString("username") + " is available..");
                                st1.close();
                                st1 = con.createStatement();
                                st1.executeUpdate("delete from passwords where username = '"+ tempUsername +"'");
                                st1.executeUpdate("delete from users where username = '"+ tempUsername +"'");
                                System.out.println("User deleted...");
                                st1.close();
                            } catch (Exception e) {
                                System.out.println("no user available with given username");
                            }
                        }
                        case 3 -> {
                            System.out.print("Enter current password: ");
                            String tempPassword = S.next();
                            if(Objects.equals(passwordObj.getPassword(), tempPassword)){
                                try {
                                    System.out.print("Enter new password: ");
                                    String tempNewPass = S.next();
                                    st1 = con.createStatement();
                                    st1.executeUpdate("update users set password = '" + tempNewPass +
                                            "' where username = '" + passwordObj.getUsername() + "' and password = '" +
                                            tempPassword + "'");
                                    System.out.println("Password updated..");
                                    st1.close();
                                    passwordObj.setPassword(tempNewPass);
                                } catch (Exception e) {
                                    System.out.println("Can't update password");
                                }
                            }
                            else {
                                System.out.println("Password is wrong..");
                            }
                        }
                        case 4 -> {
                            adminLable =0;
                            label = 0;
                            flag = 0;
                        }
                        case 5 -> {
                            System.out.println("bye");
                            System.exit(0);
                        }
                        default -> System.out.println("Enter valid input");
                    }
                }while (adminLable == 1);
            }
        }while( flag == 0);
    }
}

