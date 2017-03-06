package th.ac.mahidol.rama.emam.manager;

import android.os.StrictMode;
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
    private String URL = "http://appcenter.rama.mahidol.ac.th/webservice/patientservice.WSDL"; // connected but no data(empty) in devfox_ws
    private String SOAP_ACTION;
    private SoapPrimitive resultString;

    private String NAME_SPACEPHOTO = "http://tempuri.org/PatPhotoService/message/";
    private String URLPHOTO = "http://appcenter.rama.mahidol.ac.th/Webservice/PatPhotoService.WSDL";
    private String resultLinkPhoto = null;

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
//            Log.e("check", "Error: " + ex.getMessage());
        }

        return String.valueOf(resultString);
    }


    public String getDrugADR(String methodName, String mrn){

        SOAP_ACTION = "http://tempuri.org/patientservice/action/patientservice."+ methodName;

        try {

            SoapObject request = new SoapObject(NAME_SPACE, methodName);
            request.addProperty("hn", mrn);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);

            resultString = (SoapPrimitive) soapEnvelope.getResponse();
//            Log.d("check", "resultString = "+ resultString);


        } catch (Exception ex) {
//            Log.e("check", "Error: " + ex.getMessage());
        }
        return String.valueOf(resultString);
    }


    public String getLogin(String methodName, String username, String password){

        SOAP_ACTION = "http://tempuri.org/patientservice/action/patientservice."+ methodName;

        try {

            SoapObject request = new SoapObject(NAME_SPACE, methodName);
            request.addProperty("user_id", username);
            request.addProperty("password_id", password);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);

            resultString = (SoapPrimitive) soapEnvelope.getResponse();
//            Log.d("check", "resultString = "+ resultString);

        } catch (Exception ex) {
            Log.e("check", "Error: " + ex.getMessage());
        }
        return String.valueOf(resultString);
    }

    public String getDrugIPD(String methodName, String hn){

        SOAP_ACTION = "http://tempuri.org/patientservice/action/patientservice."+ methodName;

        try {

            SoapObject request = new SoapObject(NAME_SPACE, methodName);
            request.addProperty("hn", hn);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);

            resultString = (SoapPrimitive) soapEnvelope.getResponse();
//            Log.d("check", "resultString = "+ resultString);


        } catch (Exception ex) {
//            Log.e("check", "Error: " + ex.getMessage());
        }
        return String.valueOf(resultString);
    }

    public String getLinkPhoto(String methodName, String idCardNo){

        SOAP_ACTION = "http://tempuri.org/PatPhotoService/action/PatPhotoService."+ methodName;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {

            SoapObject request = new SoapObject(NAME_SPACEPHOTO, methodName);
            request.addProperty("mId_Card", idCardNo);


            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URLPHOTO);
            transport.call(SOAP_ACTION, soapEnvelope);

            resultLinkPhoto = (String) soapEnvelope.getResponse();
//            Log.d("check", "resultLinkPhoto = "+ resultLinkPhoto);


        } catch (Exception ex) {
//            Log.e("check", "getLinkPhoto Error: " + ex.getMessage());
//            for(int i=0; i< ex.getStackTrace().length;i++){
//                Log.e("check", "getStackTrace Error: " + ex.getStackTrace()[i]);
//            }
        }
        return resultLinkPhoto;
    }

}
