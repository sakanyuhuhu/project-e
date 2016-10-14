package th.ac.mahidol.rama.emam.manager;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.CurrentMedDao;

public class SearchCurrentMedManager extends DefaultHandler {

    Boolean currentElement = false;
    String currentValue = "";
    CurrentMedDao dao = null;
    private ArrayList<CurrentMedDao> itemsList = new ArrayList<CurrentMedDao>();

    public ArrayList<CurrentMedDao> getItemsList() {
        return itemsList;
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        currentElement = true;
        currentValue = "";
        if (localName.equals("cursor_mos")) {
            dao = new CurrentMedDao();
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        currentElement = false;
        if (localName.equalsIgnoreCase("hn"))
            dao.setHn(currentValue);
        else if (localName.equalsIgnoreCase("ord_id"))
            dao.setOrdId(currentValue);
        else if (localName.equalsIgnoreCase("action"))
            dao.setAction(currentValue);
        else if (localName.equalsIgnoreCase("name"))
            dao.setName(currentValue);
        else if (localName.equalsIgnoreCase("dose"))
            dao.setDose(currentValue);
        else if (localName.equalsIgnoreCase("unit"))
            dao.setUnit(currentValue);
        else if (localName.equalsIgnoreCase("route"))
            dao.setRoute(currentValue);
        else if (localName.equalsIgnoreCase("site"))
            dao.setSite(currentValue);
        else if (localName.equalsIgnoreCase("qtytmg"))
            dao.setQtytmg(currentValue);
        else if (localName.equalsIgnoreCase("method"))
            dao.setMethod(currentValue);
        else if (localName.equalsIgnoreCase("spectime"))
            dao.setSpectime(currentValue);
        else if (localName.equalsIgnoreCase("specday"))
            dao.setSpecday(currentValue);
        else if (localName.equalsIgnoreCase("days"))
            dao.setDays(currentValue);
        else if (localName.equalsIgnoreCase("daysdose"))
            dao.setDaysdose(currentValue);
        else if (localName.equalsIgnoreCase("prn"))
            dao.setPrn(Boolean.parseBoolean(currentValue));
        else if (localName.equalsIgnoreCase("refill"))
            dao.setRefill(currentValue);
        else if (localName.equalsIgnoreCase("ordtype"))
            dao.setOrdtype(currentValue);
        else if (localName.equalsIgnoreCase("reqtype"))
            dao.setReqtype(currentValue);
        else if (localName.equalsIgnoreCase("active"))
            dao.setActive(currentValue);
        else if (localName.equalsIgnoreCase("takeaction"))
            dao.setTakeaction(currentValue);
        else if (localName.equalsIgnoreCase("adispdt"))
            dao.setAdispdt(currentValue);
        else if (localName.equalsIgnoreCase("adisptm"))
            dao.setAdisptm(currentValue);
        else if (localName.equalsIgnoreCase("startdt"))
            dao.setStartdt(currentValue);
        else if (localName.equalsIgnoreCase("starttm"))
            dao.setStarttm(currentValue);
        else if (localName.equalsIgnoreCase("stopdt"))
            dao.setStopdt(currentValue);
        else if (localName.equalsIgnoreCase("stoptm"))
            dao.setStoptm(currentValue);
        else if (localName.equalsIgnoreCase("taketot"))
            dao.setTaketot(currentValue);
        else if (localName.equalsIgnoreCase("returntot"))
            dao.setReturntot(currentValue);
        else if (localName.equalsIgnoreCase("oriward"))
            dao.setOriward(currentValue);
        else if (localName.equalsIgnoreCase("ordward"))
            dao.setOrdward(currentValue);
        else if (localName.equalsIgnoreCase("retward"))
            dao.setRetward(currentValue);
        else if (localName.equalsIgnoreCase("orddt"))
            dao.setOrddt(currentValue);
        else if (localName.equalsIgnoreCase("ordtm"))
            dao.setOrdtm(currentValue);
        else if (localName.equalsIgnoreCase("ordpid"))
            dao.setOrdpid(currentValue);
        else if (localName.equalsIgnoreCase("dc_by"))
            dao.setDcBy(currentValue);
        else if (localName.equalsIgnoreCase("dc_tm"))
            dao.setDcTm(currentValue);
        else if (localName.equalsIgnoreCase("offdt"))
            dao.setOffdt(currentValue);
        else if (localName.equalsIgnoreCase("offtm"))
            dao.setOfftm(currentValue);
        else if (localName.equalsIgnoreCase("offpid"))
            dao.setOffpid(currentValue);
        else if (localName.equalsIgnoreCase("comfirmdt"))
            dao.setComfirmdt(currentValue);
        else if (localName.equalsIgnoreCase("confirmtm"))
            dao.setConfirmtm(currentValue);
        else if (localName.equalsIgnoreCase("confirmby"))
            dao.setConfirmby(currentValue);
        else if (localName.equalsIgnoreCase("ph_by"))
            dao.setPhBy(currentValue);
        else if (localName.equalsIgnoreCase("senddt"))
            dao.setSenddt(currentValue);
        else if (localName.equalsIgnoreCase("sendtm"))
            dao.setSendtm(currentValue);
        else if (localName.equalsIgnoreCase("tstamp"))
            dao.setTstamp(currentValue);
        else if (localName.equalsIgnoreCase("prop_help"))
            dao.setPropHelp(currentValue);
        else if (localName.equalsIgnoreCase("ph_note"))
            dao.setPhNote(currentValue);
        else if (localName.equalsIgnoreCase("mims"))
            dao.setMims(currentValue);
        else if (localName.equalsIgnoreCase("cursor_mos"))
            itemsList.add(dao);
    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if (currentElement) {
            currentValue = currentValue +  new String(ch, start, length);
        }

    }

}
