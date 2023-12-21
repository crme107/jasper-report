package org.example.Model;

public class Holiday {
    private String NAME;
    private String DATE;
    private String COUNTRY;

    public Holiday() {}

    public Holiday(String NAME, String DATE, String COUNTRY) {
        this.NAME = NAME;
        this.DATE = DATE;
        this.COUNTRY = COUNTRY;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getCOUNTRY() {
        return COUNTRY;
    }

    public void setCOUNTRY(String COUNTRY) {
        this.COUNTRY = COUNTRY;
    }
}
