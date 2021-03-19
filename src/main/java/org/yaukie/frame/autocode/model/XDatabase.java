package org.yaukie.frame.autocode.model;

import java.io.Serializable;
import java.util.Date;

public class XDatabase implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column x_database.id
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column x_database.name
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column x_database.database_type
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    private String databaseType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column x_database.database_contype
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    private String databaseContype;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column x_database.host_name
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    private String hostName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column x_database.database_name
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    private String databaseName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column x_database.port
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    private Integer port;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column x_database.created_time
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    private Date createdTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column x_database.update_time
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column x_database.username
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    private String username;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column x_database.password
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    private String password;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column x_database.servername
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    private String servername;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table x_database
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column x_database.id
     *
     * @return the value of x_database.id
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column x_database.id
     *
     * @param id the value for x_database.id
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column x_database.name
     *
     * @return the value of x_database.name
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column x_database.name
     *
     * @param name the value for x_database.name
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column x_database.database_type
     *
     * @return the value of x_database.database_type
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public String getDatabaseType() {
        return databaseType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column x_database.database_type
     *
     * @param databaseType the value for x_database.database_type
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column x_database.database_contype
     *
     * @return the value of x_database.database_contype
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public String getDatabaseContype() {
        return databaseContype;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column x_database.database_contype
     *
     * @param databaseContype the value for x_database.database_contype
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public void setDatabaseContype(String databaseContype) {
        this.databaseContype = databaseContype;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column x_database.host_name
     *
     * @return the value of x_database.host_name
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column x_database.host_name
     *
     * @param hostName the value for x_database.host_name
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column x_database.database_name
     *
     * @return the value of x_database.database_name
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column x_database.database_name
     *
     * @param databaseName the value for x_database.database_name
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column x_database.port
     *
     * @return the value of x_database.port
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public Integer getPort() {
        return port;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column x_database.port
     *
     * @param port the value for x_database.port
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column x_database.created_time
     *
     * @return the value of x_database.created_time
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column x_database.created_time
     *
     * @param createdTime the value for x_database.created_time
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column x_database.update_time
     *
     * @return the value of x_database.update_time
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column x_database.update_time
     *
     * @param updateTime the value for x_database.update_time
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column x_database.username
     *
     * @return the value of x_database.username
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column x_database.username
     *
     * @param username the value for x_database.username
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column x_database.password
     *
     * @return the value of x_database.password
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column x_database.password
     *
     * @param password the value for x_database.password
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column x_database.servername
     *
     * @return the value of x_database.servername
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public String getServername() {
        return servername;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column x_database.servername
     *
     * @param servername the value for x_database.servername
     *
     * @mbg.generated Fri Jan 29 19:28:33 CST 2021
     */
    public void setServername(String servername) {
        this.servername = servername;
    }
}