package th.ac.mahidol.rama.emam.manager;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import th.ac.mahidol.rama.emam.dao.buildCheckLoginDAO.CheckLoginDao;

public class SearchLoginManager extends DefaultHandler {

    Boolean currentElement = false;
    String currentValue = "";
    CheckLoginDao dao = null;
    private ArrayList<CheckLoginDao> itemsList = new ArrayList<CheckLoginDao>();

    public ArrayList<CheckLoginDao> getItemsList() {
        return itemsList;
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        currentElement = true;
        currentValue = "";
        if (localName.equals("staff_detail")) {
            dao = new CheckLoginDao();
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        currentElement = false;
        if (localName.equalsIgnoreCase("name"))
            dao.setName(currentValue);
        else if (localName.equalsIgnoreCase("role"))
            dao.setRole(currentValue);
        else if (localName.equalsIgnoreCase("staff_detail"))
            itemsList.add(dao);
    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if (currentElement) {
            currentValue = currentValue +  new String(ch, start, length);
        }

    }

}
