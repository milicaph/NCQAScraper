package parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.util.ArrayList;

public class HTMLParser {



    private static String rating(Element cell0){
        Document subdoc = Jsoup.parse(cell0.html());
        Elements value = subdoc.select("div.best");

        return value.text();

    }



}
