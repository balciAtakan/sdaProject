package test;

import main.java.sda.web.services.APIService;
import main.java.sda.web.views.WordView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

class ThesaurusAPITester
{

    final String endpoint = "http://thesaurus.altervista.org/thesaurus/v1";
    private final String key = "I1RtpH5OjtyGTULi0kWN";
    final String word = "power";

    @Test
    void Thesaurus()
    {
        SendRequest(word, "en_US", key, "json");
    } // end of Thesaurus


    void SendRequest(String word, String language, String key, String output)
    {
        try
        {
            URL serverAddress = new URL(endpoint + "?word=" + URLEncoder.encode(word,
                    "UTF-8") + "&language=" + language + "&key=" + key + "&output=" + output);
            HttpURLConnection connection = (HttpURLConnection) serverAddress.openConnection();
            connection.connect();
            int rc = connection.getResponseCode();
            if (rc == 200)
            {
                String line = null;
                BufferedReader br = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) sb.append(line + '\n');
                JSONObject obj = (JSONObject) JSONValue.parse(sb.toString());
                JSONArray array = (JSONArray) obj.get("response");
                for (int i = 0; i < array.size(); i++)
                {
                    JSONObject list = (JSONObject) ((JSONObject) array.get(i)).get("list");
                    //if(!list.get("category").equals("(noun)"))
                        //continue;
                    System.out.println(list.get("category") + ":" + list.get("synonyms"));
                }
            } else System.out.println("HTTP error:" + rc);
            connection.disconnect();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void dataMuse()
    {

        APIService api = new APIService();
        String word = "mechanic";

        List<WordView> results = api.findSynonymsWithDataMuse(word);
        WordView res = results.get(0);

        for (WordView result : res.getSynonyms())
        {
            System.out.println("Synonym: " + result.getWord() +" {" + result.getScore() + "}");
        }

    }

/*
        URL url = null;
        try
        {
            url = new URL("https://api.datamuse.com/words?rel_syn=mechanism&max=10");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) sb.append(line + '\n');

                JSONArray array = (JSONArray) JSONValue.parse(sb.toString());
                //JSONArray array = (JSONArray) obj.get("response");
                for (int i = 0; i < array.size(); i++)
                {
                    System.out.println(((JSONObject) array.get(i)).get("score") + ":" + ((JSONObject) array.get(i)).get("word"));
                }

                for (String line2; (line2 = reader.readLine()) != null;) {
                    System.out.println(line2);
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    */
}
