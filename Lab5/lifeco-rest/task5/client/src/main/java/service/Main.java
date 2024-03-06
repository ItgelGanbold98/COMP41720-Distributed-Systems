package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import service.core.ClientInfo;
import service.core.Quotation;

import java.text.NumberFormat;
import java.io.IOException;
import java.util.Iterator;


public class Main {

    /**
     * This is the starting point for the application. Here, we must
     * get a reference to the Broker Service and then invoke the
     * getQuotations() method on that service.
     * <p>
     * Finally, you should print out all quotations returned
     * by the service.
     *
     * @param args
     */

    private final OkHttpClient client = new OkHttpClient();
    public String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"), json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }


    public static void main(String[] args) {

        System.out.println("RUNNING CLIENT");
        Main postClientInfo = new Main();
        String url = "http://localhost:8083/applications";


//         Create the broker and run the test data
        for (ClientInfo info : clients) {
//            Application new_id = new Application(info);
            displayProfile(info);
            try {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(info);
                String postResponse = postClientInfo.doPostRequest(url, json);
                JsonNode postJSONify = mapper.readTree(postResponse);
                String urlId = postJSONify.get("id").asText();

                String getResponse = postClientInfo.doGetRequest(url + "/" + urlId);
                try {
                    JsonNode jsonNode = mapper.readTree(getResponse);
                    // Looping through the elements of the JSON array
                    Iterator<JsonNode> elements = jsonNode.elements();
                    while (elements.hasNext()) {
                        JsonNode jsonElement = elements.next();

                        String company = jsonElement.get("company").asText();
                        String reference = jsonElement.get("reference").asText();
                        double price = jsonElement.get("price").asDouble();

                        Quotation tempQuotation = new Quotation();

                        tempQuotation.company = company;
                        tempQuotation.reference = reference;
                        tempQuotation.price = price;

                        displayQuotation(tempQuotation);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("\n");
        }

        postClientInfo.client.connectionPool().evictAll();  // Close idle connections
        postClientInfo.client.dispatcher().executorService().shutdownNow();  // Shutdown the executor service
    }
            /**
             * Display the client info nicely.
             *
             * @param info
             */
            public static void displayProfile (ClientInfo info){
                System.out.println("|=================================================================================================================|");
                System.out.println("|                                     |                                     |                                     |");
                System.out.println(
                        "| Name: " + String.format("%1$-29s", info.name) +
                                " | Gender: " + String.format("%1$-27s", (info.gender == ClientInfo.MALE ? "Male" : "Female")) +
                                " | Age: " + String.format("%1$-30s", info.age) + " |");
                System.out.println(
                        "| Weight/Height: " + String.format("%1$-20s", info.weight + "kg/" + info.height + "m") +
                                " | Smoker: " + String.format("%1$-27s", info.smoker ? "YES" : "NO") +
                                " | Medical Problems: " + String.format("%1$-17s", info.medicalIssues ? "YES" : "NO") + " |");
                System.out.println("|                                     |                                     |                                     |");
                System.out.println("|=================================================================================================================|");
            }

            /**
             * Display a quotation nicely - note that the assumption is that the quotation will follow
             * immediately after the profile (so the top of the quotation box is missing).
             *
             * @param quotation
             */
            public static void displayQuotation (Quotation quotation){
                System.out.println(
                        "| Company: " + String.format("%1$-26s", quotation.company) +
                                " | Reference: " + String.format("%1$-24s", quotation.reference) +
                                " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price)) + " |");
                System.out.println("|=================================================================================================================|");
            }

            /**
             * Test Data
             */
            public static final ClientInfo[] clients = {
                    new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false),
                    new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 1.6, 100, true, true),
                    new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 21, 1.78, 65, false, false),
                    new ClientInfo("Rem Collier", ClientInfo.MALE, 49, 1.8, 120, false, true),
                    new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 1.9, 75, true, false),
                    new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 0.45, 1.6, false, false)
            };
        }