import service.girlsallowed.GAQService;

import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        Endpoint.publish("http://0.0.0.0:9003/quotations", new GAQService());
//        System.out.println("Girlsallowed Service is running at: http://0.0.0.0:9003/quotations");
    }
}
