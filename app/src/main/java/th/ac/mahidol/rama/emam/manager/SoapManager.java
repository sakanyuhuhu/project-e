package th.ac.mahidol.rama.emam.manager;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

import th.ac.mahidol.rama.emam.dao.SearchPatientByWardDao;

public class SoapManager {
    private String NAME_SPACE = "http://tempuri.org/IMP_Web_Services/message/";
    private String METHOD_NAME;
    private String URL = "http://devfox_ws.rama.mahidol.ac.th/webservice/IMP_Web_Services.WSDL";
    private String SOAP_ACTION;
    private List<SearchPatientByWardDao> listSearchPatientByWardDao;
    private SoapPrimitive resultString;

    public SoapManager(String METHOD_NAME) {
        this.METHOD_NAME = METHOD_NAME;
        SOAP_ACTION = "http://tempuri.org/IMP_Web_Services/action/IMP_Web_Services."+ METHOD_NAME;
        try {
            SoapObject Request = new SoapObject(NAME_SPACE, METHOD_NAME);
//            Request.addProperty("p_Ward", "SDIPD83");

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();

            Log.d("check", "Result Celsius: " + resultString);
        } catch (Exception ex) {
            Log.e("check", "SoapManager Error: " + ex.getMessage());
        }
    }

    public String getVersion(){
        return resultString+" ";
    }



}
