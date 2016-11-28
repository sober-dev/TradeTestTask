package ua.com.sober.tradetesttask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
        Element table = null;
        try {
            document = Jsoup.connect("https://trade.opteck.com/trade/iframe?view=table").get();
            table = document.select("body > div.int-Panel.line.tableViewBO > div.line.table-box > div.ins-t-block.table > div.body-t").first();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(document);
        assertNotNull(table);
    }
}
