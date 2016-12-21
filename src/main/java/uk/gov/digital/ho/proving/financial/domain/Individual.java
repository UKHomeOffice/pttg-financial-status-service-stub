package uk.gov.digital.ho.proving.financial.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Document
public class Individual {

    @JsonFormat(shape=JsonFormat.Shape.STRING)

    private String title;
    @NotNull
    private String forename;
    @NotNull
    private String surname;
    @NotNull
    private String nino;


    public Individual() {
    }

    public Individual(String title, String forename, String surname, String nino) {
        this.title = title;
        this.forename = forename;
        this.surname = surname;
        this.nino = nino;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNino() {
        return nino;
    }

    public void setNino(String nino) {
        this.nino = nino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Individual that = (Individual) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (forename != null ? !forename.equals(that.forename) : that.forename != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        return nino != null ? nino.equals(that.nino) : that.nino == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (forename != null ? forename.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (nino != null ? nino.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "title='" + title + '\'' +
                ", forename='" + forename + '\'' +
                ", surname='" + surname + '\'' +
                ", nino='" + nino + '\'' +
                '}';
    }
}
