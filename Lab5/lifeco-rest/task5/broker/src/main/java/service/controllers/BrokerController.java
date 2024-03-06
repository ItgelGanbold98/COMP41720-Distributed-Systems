package service.controllers;

import ch.qos.logback.core.net.server.Client;
import com.sun.org.apache.xpath.internal.operations.Quo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import service.core.Application;
import service.core.ClientInfo;
import service.core.Quotation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


@RestController
public class BrokerController {

    private HashMap<Integer, ArrayList<Quotation>> quotations = new HashMap<>();
    private ArrayList<String> listOfURLs = new ArrayList<>();


    @GetMapping(value = "/applications", produces = "application/json")
    public ResponseEntity<ArrayList> getInfo() {
        System.out.println(listOfURLs);
        return ResponseEntity.status(HttpStatus.OK).body(listOfURLs);
    }

    @PostMapping(value = "/applications", consumes = "application/json")
    public ResponseEntity<CustomResponse> createQuotationsResponse(
            @RequestBody ClientInfo info) throws MalformedURLException {

        Application postedInfo = new Application();
        postedInfo.info = info;
        ArrayList<URL> urls = new ArrayList<>();
        List<String> portList = Arrays.asList("http://auldfellas:8080", "http://girlsallowed:8081", "http://dodgygeezers:8082");

//      Retrieve the specific URLs
        for (String urlport : portList) {
            try {
                RestTemplate template = new RestTemplate();
                ResponseEntity<ClientInfo> response = template.postForEntity(urlport + "/quotations", info, ClientInfo.class);
                System.out.println("Trying to connect to: " + urlport + "/quotations");
                urls.add(new URL(response.getHeaders().getLocation().toString()));

                if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                    listOfURLs.add(response.getHeaders().getLocation().toString());
                    System.out.println("Location of resource: " + response.getHeaders().getLocation().toString());
                }
            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println("Can't connect to " + urlport + "/quotations");
            }
        }
//      Use the retrieved URLs to get the quotations and then construct a response Application object
        for (URL url : urls) {
            try {
                RestTemplate template = new RestTemplate();
                ResponseEntity<Quotation> response = template.getForEntity(url.toString(), Quotation.class);
                if (response != null) {
                    Quotation receivedQuotation = response.getBody();
                    postedInfo.quotations.add(receivedQuotation);

                    ArrayList<Quotation> list = quotations.get(postedInfo.id);
                    if (list == null) {
                        list = new ArrayList<>();
                        quotations.put(postedInfo.id, list);
                    }
                    list.add(receivedQuotation);
                } else {
                    System.out.println("No quotation found");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception occurred when posting to broker...");
            }
        }
//      Use a custom response class to make the response match the one shown in the lab
        CustomResponse returnResponse = new CustomResponse(postedInfo.id, postedInfo.info, postedInfo.quotations);
        return new ResponseEntity<>(returnResponse, HttpStatus.OK);
    }

    public static class CustomResponse {
        private int id;
        private ClientInfo info;
        private ArrayList<Quotation> quotations;
        public CustomResponse(int id, ClientInfo info, ArrayList<Quotation> quotations) {
            this.id = id;
            this.info = info;
            this.quotations = quotations;
        }
        public CustomResponse() {
        }
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public ClientInfo getInfo() {
            return info;
        }
        public void setInfo(ClientInfo info) {
            this.info = info;
        }
        public ArrayList<Quotation> getQuotations() {
            return quotations;
        }
        public void setQuotations(ArrayList<Quotation> quotations) {
            this.quotations = quotations;
        }
    }

    @Value("${server.port}")
    private int port;


    @GetMapping(value = "/applications/{id}", produces = {"application/json"})
    public ResponseEntity<ArrayList> getQuotation(@PathVariable Integer id) {
        ArrayList quotationArrayList = quotations.get(id);
        if (quotationArrayList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(quotationArrayList);
    }
}