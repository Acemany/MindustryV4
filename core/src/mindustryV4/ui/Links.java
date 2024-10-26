package mindustryV4.ui;

import com.badlogic.gdx.graphics.Color;
import ucore.util.Bundles;

public class Links{
    private static LinkEntry[] links;

    private static void createLinks(){
        links = new LinkEntry[]{
            new LinkEntry("telegram", "https://t.me/acemany", Color.valueOf("2481cc")),
            new LinkEntry("trello", "https://trello.com/b/IvmwyEwH", Color.valueOf("026aa7")),
            new LinkEntry("github", "https://github.com/acemany/MindustryV4_reforked/", Color.valueOf("24292e")),
            new LinkEntry("dev-builds", "https://github.com/acemany/MindustryV4_reforked/releases/", Color.valueOf("fafbfc"))
        };
    }

    public static LinkEntry[] getLinks(){
        if(links == null){
            createLinks();
        }

        return links;
    }

    public static class LinkEntry{
        public final String name, description, link;
        public final Color color;

        public LinkEntry(String name, String link, Color color){
            this.name = name;
            this.color = color;
            this.description = Bundles.getNotNull("text.link." + name + ".description");
            this.link = link;
        }
    }
}
