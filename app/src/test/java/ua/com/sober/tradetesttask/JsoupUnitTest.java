package ua.com.sober.tradetesttask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Created by dmitry on 11/28/16.
 */

public class JsoupUnitTest {
    @Test
    public void getDataFromTable() {
        Document document = null;
//        Element table = null;
        Elements assets = null;
        try {
            document = Jsoup.connect("https://trade.opteck.com/trade/iframe?view=table").get();
//            table = document.select("body > div.int-Panel.line.tableViewBO > div.line.table-box > div.ins-t-block.table > div.body-t").first();
            assets = document.select("body > div.int-Panel.line.tableViewBO > div.line.table-box > div.ins-t-block.table > div.body-t > div.o-tr-line");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Element asset : assets) {
            boolean isEnabled = asset.attributes().get("class").equals("o-tr-line active-pr-alert");

            if (isEnabled) {
                System.out.println("enabled: " + asset.attributes().get("class"));
            } else {
                System.out.println("disabled: " + asset.attributes().get("class"));
            }

        }

        assertNotNull(document);
//        assertNotNull(table);
        assertNotNull(assets);
    }
}
