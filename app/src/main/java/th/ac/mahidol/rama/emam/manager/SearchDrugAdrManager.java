package th.ac.mahidol.rama.emam.manager;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugAdrDao;

public class SearchDrugAdrManager extends DefaultHandler {

    Boolean currentElement = false;
    String currentValue = "";
    DrugAdrDao dao = null;
    private ArrayList<DrugAdrDao> itemsList = new ArrayList<DrugAdrDao>();

    public ArrayList<DrugAdrDao> getItemsList() {
        return itemsList;
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        currentElement = true;
        currentValue = "";
        if (localName.equals("cur_bedWd")) {
            dao = new DrugAdrDao();
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        currentElement = false;

            itemsList.add(dao);
    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if (currentElement) {
            currentValue = currentValue +  new String(ch, start, length);
        }

    }

}
