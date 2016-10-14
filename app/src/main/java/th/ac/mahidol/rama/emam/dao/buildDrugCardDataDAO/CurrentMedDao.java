package th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO;


import com.google.gson.annotations.SerializedName;

public class CurrentMedDao {
    @SerializedName("hn")        private String hn;
    @SerializedName("ord_id")    private String ordId;
    @SerializedName("action")    private String action;
    @SerializedName("code")      private String code;
    @SerializedName("name")      private String name;
    @SerializedName("dose")      private String dose;
    @SerializedName("unit")      private String unit;
    @SerializedName("route")     private String route;
    @SerializedName("site")      private String site;
    @SerializedName("qtytmg")    private String qtytmg;
    @SerializedName("method")    private String method;
    @SerializedName("spectime")  private String spectime;
    @SerializedName("specday")   private String specday;
    @SerializedName("days")      private String days;
    @SerializedName("daysdose")  private String daysdose;
    @SerializedName("prn")       private boolean prn;
    @SerializedName("refill")    private String refill;
    @SerializedName("ordtype")   private String ordtype;
    @SerializedName("reqtype")   private String reqtype;
    @SerializedName("active")    private String active;
    @SerializedName("takeaction")private String takeaction;
    @SerializedName("adispdt")   private String adispdt;
    @SerializedName("adisptm")   private String adisptm;
    @SerializedName("startdt")   private String startdt;
    @SerializedName("starttm")   private String starttm;
    @SerializedName("stopdt")    private String stopdt;
    @SerializedName("stoptm")    private String stoptm;
    @SerializedName("taketot")   private String taketot;
    @SerializedName("returntot") private String returntot;
    @SerializedName("oriward")   private String oriward;
    @SerializedName("ordward")   private String ordward;
    @SerializedName("retward")   private String retward;
    @SerializedName("orddt")     private String orddt;
    @SerializedName("ordtm")     private String ordtm;
    @SerializedName("ordpid")    private String ordpid;
    @SerializedName("dc_by")     private String dcBy;
    @SerializedName("dc_tm")     private String dcTm;
    @SerializedName("offdt")     private String offdt;
    @SerializedName("offtm")     private String offtm;
    @SerializedName("offpid")    private String offpid;
    @SerializedName("comfirmdt") private String comfirmdt;
    @SerializedName("confirmtm") private String confirmtm;
    @SerializedName("confirmby") private String confirmby;
    @SerializedName("ph_by")     private String phBy;
    @SerializedName("senddt")    private String senddt;
    @SerializedName("sendtm")    private String sendtm;
    @SerializedName("tstamp")    private String tstamp;
    @SerializedName("prop_help") private String propHelp;
    @SerializedName("ph_note")   private String phNote;
    @SerializedName("mims")      private String mims;

    public String getHn() {
        return hn;
    }

    public void setHn(String hn) {
        this.hn = hn;
    }

    public String getOrdId() {
        return ordId;
    }

    public void setOrdId(String ordId) {
        this.ordId = ordId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getQtytmg() {
        return qtytmg;
    }

    public void setQtytmg(String qtytmg) {
        this.qtytmg = qtytmg;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSpectime() {
        return spectime;
    }

    public void setSpectime(String spectime) {
        this.spectime = spectime;
    }

    public String getSpecday() {
        return specday;
    }

    public void setSpecday(String specday) {
        this.specday = specday;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDaysdose() {
        return daysdose;
    }

    public void setDaysdose(String daysdose) {
        this.daysdose = daysdose;
    }

    public boolean isPrn() {
        return prn;
    }

    public void setPrn(boolean prn) {
        this.prn = prn;
    }

    public String getRefill() {
        return refill;
    }

    public void setRefill(String refill) {
        this.refill = refill;
    }

    public String getOrdtype() {
        return ordtype;
    }

    public void setOrdtype(String ordtype) {
        this.ordtype = ordtype;
    }

    public String getReqtype() {
        return reqtype;
    }

    public void setReqtype(String reqtype) {
        this.reqtype = reqtype;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getTakeaction() {
        return takeaction;
    }

    public void setTakeaction(String takeaction) {
        this.takeaction = takeaction;
    }

    public String getAdispdt() {
        return adispdt;
    }

    public void setAdispdt(String adispdt) {
        this.adispdt = adispdt;
    }

    public String getAdisptm() {
        return adisptm;
    }

    public void setAdisptm(String adisptm) {
        this.adisptm = adisptm;
    }

    public String getStartdt() {
        return startdt;
    }

    public void setStartdt(String startdt) {
        this.startdt = startdt;
    }

    public String getStarttm() {
        return starttm;
    }

    public void setStarttm(String starttm) {
        this.starttm = starttm;
    }

    public String getStopdt() {
        return stopdt;
    }

    public void setStopdt(String stopdt) {
        this.stopdt = stopdt;
    }

    public String getStoptm() {
        return stoptm;
    }

    public void setStoptm(String stoptm) {
        this.stoptm = stoptm;
    }

    public String getTaketot() {
        return taketot;
    }

    public void setTaketot(String taketot) {
        this.taketot = taketot;
    }

    public String getReturntot() {
        return returntot;
    }

    public void setReturntot(String returntot) {
        this.returntot = returntot;
    }

    public String getOriward() {
        return oriward;
    }

    public void setOriward(String oriward) {
        this.oriward = oriward;
    }

    public String getOrdward() {
        return ordward;
    }

    public void setOrdward(String ordward) {
        this.ordward = ordward;
    }

    public String getRetward() {
        return retward;
    }

    public void setRetward(String retward) {
        this.retward = retward;
    }

    public String getOrddt() {
        return orddt;
    }

    public void setOrddt(String orddt) {
        this.orddt = orddt;
    }

    public String getOrdtm() {
        return ordtm;
    }

    public void setOrdtm(String ordtm) {
        this.ordtm = ordtm;
    }

    public String getOrdpid() {
        return ordpid;
    }

    public void setOrdpid(String ordpid) {
        this.ordpid = ordpid;
    }

    public String getDcBy() {
        return dcBy;
    }

    public void setDcBy(String dcBy) {
        this.dcBy = dcBy;
    }

    public String getDcTm() {
        return dcTm;
    }

    public void setDcTm(String dcTm) {
        this.dcTm = dcTm;
    }

    public String getOffdt() {
        return offdt;
    }

    public void setOffdt(String offdt) {
        this.offdt = offdt;
    }

    public String getOfftm() {
        return offtm;
    }

    public void setOfftm(String offtm) {
        this.offtm = offtm;
    }

    public String getOffpid() {
        return offpid;
    }

    public void setOffpid(String offpid) {
        this.offpid = offpid;
    }

    public String getComfirmdt() {
        return comfirmdt;
    }

    public void setComfirmdt(String comfirmdt) {
        this.comfirmdt = comfirmdt;
    }

    public String getConfirmtm() {
        return confirmtm;
    }

    public void setConfirmtm(String confirmtm) {
        this.confirmtm = confirmtm;
    }

    public String getConfirmby() {
        return confirmby;
    }

    public void setConfirmby(String confirmby) {
        this.confirmby = confirmby;
    }

    public String getPhBy() {
        return phBy;
    }

    public void setPhBy(String phBy) {
        this.phBy = phBy;
    }

    public String getSenddt() {
        return senddt;
    }

    public void setSenddt(String senddt) {
        this.senddt = senddt;
    }

    public String getSendtm() {
        return sendtm;
    }

    public void setSendtm(String sendtm) {
        this.sendtm = sendtm;
    }

    public String getTstamp() {
        return tstamp;
    }

    public void setTstamp(String tstamp) {
        this.tstamp = tstamp;
    }

    public String getPropHelp() {
        return propHelp;
    }

    public void setPropHelp(String propHelp) {
        this.propHelp = propHelp;
    }

    public String getPhNote() {
        return phNote;
    }

    public void setPhNote(String phNote) {
        this.phNote = phNote;
    }

    public String getMims() {
        return mims;
    }

    public void setMims(String mims) {
        this.mims = mims;
    }
}
