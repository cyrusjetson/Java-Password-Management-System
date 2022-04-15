package com.passwordmanager;

public class Password extends User{
    private String domain;
    private String domainPassword;

    // ----- Getters and Setters ------
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomainPassword() {
        return domainPassword;
    }

    public void setDomainPassword(String domainPassword) {
        this.domainPassword = domainPassword;
    }
    public void deleteAll(){
        this.domain = "";
        this.domainPassword= "";
        super.deleteAll();
    }
}
