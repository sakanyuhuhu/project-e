package th.ac.mahidol.rama.emam.manager;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import th.ac.mahidol.rama.emam.dao.PatientDao;

public class SearchPatientByWardManager extends DefaultHandler {

    Boolean currentElement = false;
    String currentValue = "";
    PatientDao dao = null;
    private ArrayList<PatientDao> itemsList = new ArrayList<PatientDao>();

    public ArrayList<PatientDao> getItemsList() {
        return itemsList;
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        currentElement = true;
        currentValue = "";
        if (localName.equals("cur_bedWd")) {
            dao = new PatientDao();
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        currentElement = false;
        if (localName.equalsIgnoreCase("ward"))
            dao.setWard(currentValue);
        else if (localName.equalsIgnoreCase("mrn"))
            dao.setMrn(currentValue);
        else if (localName.equalsIgnoreCase("bedno"))
            dao.setBedno(currentValue);
        else if (localName.equalsIgnoreCase("enc_type"))
            dao.setEncType(currentValue);
        else if (localName.equalsIgnoreCase("enc_id"))
            dao.setEncId(currentValue);
        else if (localName.equalsIgnoreCase("cur_bedWd"))
            itemsList.add(dao);
    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if (currentElement) {
            currentValue = currentValue +  new String(ch, start, length);
        }

    }

}
