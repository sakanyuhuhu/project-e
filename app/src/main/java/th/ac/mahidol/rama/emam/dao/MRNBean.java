package th.ac.mahidol.rama.emam.dao;


public class MRNBean {

    private String mrn;

    public String getMrn() {
        return mrn;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    @Override
    public String toString() {
        return "MRNBean{" +
                "mrn='" + mrn + '\'' +
                '}';
    }
}
