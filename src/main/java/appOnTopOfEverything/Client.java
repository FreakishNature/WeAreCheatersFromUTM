package appOnTopOfEverything;

import appOnTopOfEverything.model.DataRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.httpLike.processors.HttpLikeClient;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

public class Client {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();

        HttpLikeClient client = new HttpLikeClient("localhost",4000);
        System.out.println(client.send("POST", "/data", mapper.writeValueAsString(new DataRequest("1", "wooow"))));
        System.out.println(client.send("GET", "/data", ""));
        System.out.println(client.send("GET", "/data", "1"));

    }
}
