package uk.gov.digital.ho.proving.financial.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document
@CompoundIndexes({
        @CompoundIndex(name = "acc_idx", unique = true, def = "{'individual.nino' : 1}")
})
public class Applications {

    @Id
    private String id;

    @NotNull
    private Individual individual;

    public Applications() {
    }

    public Applications(Individual individual) {
        this.individual = individual;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Applications that = (Applications) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return individual != null ? individual.equals(that.individual) : that.individual == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (individual != null ? individual.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Applications{" +
                "id='" + id + '\'' +
                ", individual=" + individual +
                '}';
    }
}
