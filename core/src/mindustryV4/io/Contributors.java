package mindustryV4.io;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import mindustryV4.net.Net;
import ucore.function.Consumer;

import static mindustryV4.Vars.contributorsURL;

public class Contributors{

    public static void getContributors(Consumer<Array<Contributor>> success, Consumer<Throwable> fail){
        Net.http(contributorsURL, "GET", result -> {
            JsonReader reader = new JsonReader();
            JsonValue value = reader.parse(result).child;
            Array<Contributor> out = new Array<>();

            while(value != null){
                String login = value.getString("login");
                out.add(new Contributor(login));
                value = value.next;
            }

            success.accept(out);
        }, fail);
    }

    public static class Contributor{
        public final String login;

        public Contributor(String login){
            this.login = login;
        }

        @Override
        public String toString(){
            return "Contributor{" +
                    "login='" + login + '\'' +
                    '}';
        }
    }
}
