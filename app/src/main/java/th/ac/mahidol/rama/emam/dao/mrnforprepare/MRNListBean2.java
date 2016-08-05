package th.ac.mahidol.rama.emam.dao.mrnforprepare;

import java.util.List;

public class MRNListBean2 {
    private List<MRNBean2> mrnBeans;

    public MRNListBean2(){

    }
    public MRNListBean2(List<MRNBean2> mrnBeans) {
        this.mrnBeans = mrnBeans;
    }

    public List<MRNBean2> getMrnBeans() {
        return mrnBeans;
    }

    public void setMrnBeans(List<MRNBean2> mrnBeans) {
        this.mrnBeans = mrnBeans;
    }
}
