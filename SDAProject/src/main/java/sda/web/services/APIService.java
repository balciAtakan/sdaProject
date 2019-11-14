package main.java.sda.web.services;

import main.java.sda.web.views.MessageView;
import main.java.sda.web.views.WordView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope("session")
public class APIService {

    //private static Logger log = LogManager.getLogger(APIService.class);


    /*   Api Call to Datamuse with given word.
     *      The result is a list with the given and its synonyms with their score.
     *      - Only one synonym returns that synonym only
     *      - Multiple synonyms returns only the list of these word, which have more than 500 score
     *      - No Synonym with 500 points, result returns the highest scored synonym
     *
     * */
    public MessageView findSynonymsWithDataMuse(String word) {

        URL url;
        MessageView result = new MessageView(word);

        try {
            url = new URL("https://api.datamuse.com/words?rel_syn=" + word + "&max=10");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int rc = connection.getResponseCode();
            if (rc == 200) {
                BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    sb.append(line).append('\n');

                JSONArray array = (JSONArray) JSONValue.parse(sb.toString());

                if (!array.isEmpty()) {

                    if (array.size() == 1) {
                        result.getSynonyms().add(parseToWord((JSONObject) array.get(0)));
                        System.out.println("Only Result : " + result.getSynonyms().get(0));
                        return result;
                    }
                    for (Object obj : array) {
                        System.out.println(((JSONObject) obj).get("score") + ":" + ((JSONObject) obj).get("word"));
                        if (parseToWord((JSONObject) obj).getScore() < 500)
                            continue;

                        result.getSynonyms().add(parseToWord((JSONObject) obj));
                    }

                    if (result.getSynonyms().isEmpty())
                        result.getSynonyms().add(parseToWord((JSONObject) array.get(0)));

                } else
                    System.out.println("Empty set of results from api!");
            } else
                System.out.println("HTTP error:" + rc);

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Result size: " + result.getSynonyms().size());
        return result;
    }

    private WordView parseToWord(JSONObject result) {

        return new WordView(result.get("word").toString(), Integer.parseInt(result.get("score").toString()));
    }
}
