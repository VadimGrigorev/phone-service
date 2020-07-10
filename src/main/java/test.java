import controller.PhoneServiceInterface;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class test {


    public static void main(String[] args) throws Exception {

        URL url = new URL("http://localhost:9999/ws/hello?wsdl");
        QName qname = new QName("http://ws.mkyong.com/", "HelloWorldImplService");

        Service service = Service.create(url, qname);

        PhoneServiceInterface ps = service.getPort(PhoneServiceInterface.class);

        System.out.println(ps.getContacts());

    }

}
