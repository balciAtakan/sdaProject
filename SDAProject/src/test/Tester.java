package test;

import main.java.sda.web.util.SDAConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Tester {

    @Test
    void testStopwordsSize() {

    Assertions.assertNotNull(SDAConstants.stopwords);
    System.out.println("Stopwords size: " + SDAConstants.stopwords.length);

    }
}


