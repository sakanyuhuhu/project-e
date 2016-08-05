package th.ac.mahidol.rama.emam.dao.mrnforprepare;


import java.util.List;

public class MRNListBean {
    private List<MRNBean> mrnBeans;

    public MRNListBean(){

    }

    public MRNListBean(List<MRNBean> mrnBeans) {
        this.mrnBeans = mrnBeans;
    }

    public List<MRNBean> getMrnBeans() {
        return mrnBeans;
    }

    public void setMrnBeans(List<MRNBean> mrnBeans) {
        this.mrnBeans = mrnBeans;
    }
}
