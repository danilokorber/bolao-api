package io.easyware.shared.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.java.Log;
import org.yaml.snakeyaml.reader.StreamReader;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Log
public class HttpClient {

    private static final Logger LOGGER = Logger.getLogger(HttpClient.class.getName());

    private URL url;
    private HttpURLConnection con;

    public HttpClient(String urlToAccess) throws IOException {
        this(urlToAccess, null);
    }

    public HttpClient(String urlToAccess, Map<String, String> params) throws IOException {
        try {
            url = new URL(urlToAccess + "?" + ParameterStringBuilder.getParamsString(params));
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            throw e;
        }
    }

    public void close() {
        con.disconnect();
    }

    public InputStream get(String auth) throws IOException {
        try {
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            if (auth != null) con.setRequestProperty("Authorization", auth);

            con.setDoOutput(true);
            con.connect();
            return con.getInputStream();
        } catch (Exception e) {
            log.severe(e.getClass().getName() + " | " + e.getMessage());
        }
        log.warning("An error occurred. Returning null for HttpClient.get(" + auth + ")");
        return null;
    }

    public InputStream get() throws IOException {
        return this.get(null);
    }

    public String postForm(Map<String, String> body) throws IOException, ConnectException {
        log.info("POST " + url.toString());
        try {
            byte[] postData = ParameterStringBuilder.getParamsString(body).getBytes(StandardCharsets.UTF_8);

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Content-Length", Integer.toString(postData.length));
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }
            con.connect();
            String json_response = "";
            InputStreamReader in = new InputStreamReader(con.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String text = "";
            while ((text = br.readLine()) != null) {
                json_response += text;
            }
            return json_response;
        } catch (Exception e) {
            log.severe(e.getClass().getSimpleName() + " | " + e.getMessage());
        } finally {
            con.disconnect();
        }
        return null;
    }

    public String put(Object object) {
        return put(object, null);
    }

    public String put(Object object, String auth) {
        log.info("PUT " + url.toString());
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String body = ow.writeValueAsString(object);

            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            if (auth != null) con.setRequestProperty("Authorization", auth);
            con.setDoOutput(true);

            log.info("   URL    : " + url.toString());
            log.info("   BODY   : " + body);
            con.getRequestProperties().values().forEach(h -> log.info("   HEADER : " + h));

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = body.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            con.connect();
            String json_response = "";
            InputStreamReader in = new InputStreamReader(con.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String text = "";
            while ((text = br.readLine()) != null) {
                json_response += text;
            }
            return json_response;
        } catch (Exception e) {
            log.severe(e.getClass().getSimpleName() + " | " + e.getMessage());
        } finally {
            con.disconnect();
        }
        return null;
    }
}
