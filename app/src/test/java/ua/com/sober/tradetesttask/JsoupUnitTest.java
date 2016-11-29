package ua.com.sober.tradetesttask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import ua.com.sober.tradetesttask.models.Asset;
import ua.com.sober.tradetesttask.util.AssetsParser;

/**
 * Created by dmitry on 11/28/16.
 */

public class JsoupUnitTest {
    @Test
    public void getDataFromTable() {
        Document document = null;
        try {
            document = Jsoup.connect("https://trade.opteck.com/trade/iframe?view=table").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Asset> assetList = AssetsParser.parse(document);

        assertNotNull(document);
        assertFalse(assetList.isEmpty());
    }
}
