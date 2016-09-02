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
        if (localName.equals("tmp_adr")) {
            dao = new DrugAdrDao();
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        currentElement = false;
        if (localName.equalsIgnoreCase("drugname"))
            dao.setDrugname(currentValue);
        else if (localName.equalsIgnoreCase("side_effect"))
            dao.setSideEffect(currentValue);
        else if (localName.equalsIgnoreCase("naranjo"))
            dao.setNaranjo(currentValue);
        else if (localName.equalsIgnoreCase("tmp_adr"))
            itemsList.add(dao);
    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if (currentElement) {
            currentValue = currentValue +  new String(ch, start, length);
        }

    }

}
