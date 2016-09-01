package th.ac.mahidol.rama.emam.manager;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class SoapManager {
//        private String URL = "http://appcenter.rama.mahidol.ac.th/webservice/IMP_Web_Services.WSDL";
//    private String NAME_SPACE = "http://tempuri.org/IMP_Web_Services/message/";
//    private String URL = "http://devfox_ws.rama.mahidol.ac.th/webservice/IMP_Web_Services.WSDL";
    private String NAME_SPACE = "http://tempuri.org/patientservice/message/";
    private String URL = "http://devfox_ws.rama.mahidol.ac.th/webservice/patientservice.WSDL";
    private String SOAP_ACTION;
    private SoapPrimitive resultString;


    public SoapManager() {

    }

    public String getPatientByWard(String methodName, String wardId){

        SOAP_ACTION = "http://tempuri.org/IMP_Web_Services/action/IMP_Web_Services."+ methodName;

        try {

            SoapObject request = new SoapObject(NAME_SPACE, methodName);
            request.addProperty("p_Ward", wardId);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);

            resultString = (SoapPrimitive) soapEnvelope.getResponse();


        } catch (Exception ex) {
            Log.e("check", "Error: " + ex.getMessage());
        }

        return String.valueOf(resultString);
    }


    public String getADR(String methodName, String mrn){

        SOAP_ACTION = "http://tempuri.org/patientservice/action/patientservice."+ methodName;

        try {

            SoapObject request = new SoapObject(NAME_SPACE, methodName);
            request.addProperty("hn", mrn);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);

            resultString = (SoapPrimitive) soapEnvelope.getResponse();


        } catch (Exception ex) {
            Log.e("check", "Error: " + ex.getMessage());
        }
        return String.valueOf(resultString);
    }

}
