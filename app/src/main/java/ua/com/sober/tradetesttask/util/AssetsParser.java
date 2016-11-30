package ua.com.sober.tradetesttask.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import ua.com.sober.tradetesttask.models.Asset;

/**
 * Created by Dmitry on 11/29/2016.
 */

public class AssetsParser {
    public static List<Asset> parse(Document document) {
        List<Asset> assetList = new ArrayList<>();

        Elements assetElements = document.select("body > div.int-Panel.line.tableViewBO > div.line.table-box > div.ins-t-block.table > div.body-t > div.o-tr-line");
        for (Element assetElement : assetElements) {
            Asset asset = new Asset();

            // Parse status
            boolean isEnabled = assetElement.attributes().get("class").equals("o-tr-line active-pr-alert");
            asset.setEnabled(isEnabled);

            // Parse name
            Element nameElement = assetElement.select("div.cell.instrument > div > span").first();
            asset.setName(nameElement.text());

            // Parse current rate
            Element rateElement = assetElement.select("div.cell.cur-rate").first();
            boolean isUp = rateElement.attributes().get("class").equals("cell cur-rate up");
            asset.setDirection(isUp);
            Elements rateStringElements = rateElement.select("div.item > div.rate-arrow > span");
            String rateString = "";
            for (Element rateStringElement : rateStringElements) {
                switch (rateStringElement.attributes().get("class")) {
                    case "arrow-range":
                        break;
                    case "":
                        asset.setCurrentRateString(asset.getCurrentRateString() + rateStringElement.text());
                        break;
                    case "big":
                        asset.setCurrentRateString(asset.getCurrentRateString() + "<big><b>" + rateStringElement.text() + "</b></big>");
                        break;
                    case "small":
                        asset.setCurrentRateString(asset.getCurrentRateString() + "<sup>" + rateStringElement.text() + "</sup>");
                        break;
                    default:
                        break;
                }
                if (!rateStringElement.text().equals(",")) {
                    rateString += rateStringElement.text();
                }
            }
            asset.setCurrentRate(Float.parseFloat(rateString));

            // Parse change
            Element changeElement = assetElement.select("div.cell.change-td > div.item").first();
            asset.setChange(changeElement.text());

            assetList.add(asset);
        }

        return assetList;
    }
}
