package at.jku.se.decisiondocu.restclient;

import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Created by Martin on 12.05.15.
 */
public class RestHelper {

    public static boolean DEBUG_MODE = true;

   // public static final String BASEURL_OFFLINE = "http://192.168.0.104:8080/DecisionDocu/api/";
    public static final String BASEURL_OFFLINE = "http://itchyaut22.ddns.net:8080/DecisionDocu/api/";
    private static final String BASEURL_ONLINE = "http://ubuntu.mayerb.net:8080/DecisionDocu/api/";

    public static String GetBaseURL() {
        String url = (DEBUG_MODE ? BASEURL_OFFLINE : BASEURL_ONLINE);
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    //private static final String BASEURL_OFFLINE = "http://www.oracle.com";
    //private static final String BASEURL_ONLINE = "http://www.oracle.com";

    private static Client client, client2;

    /**
     * Returniert das WebTarget des Backends inkl. der Registrierung des
     * AndroidFriendlyFeatures.
     * @return
     */
    protected static WebTarget getWebTarget() {
        if (client == null) {
            try {
                client = ClientBuilder.newClient().register(AndroidFriendlyFeature.class);
            } catch (Exception e) {}
        }
        return client.target((DEBUG_MODE ? BASEURL_OFFLINE : BASEURL_ONLINE));
    }

    /**
     * Returniert das WebTarget des Backends inkl. der Registrierung des
     * AndroidFriendlyFeatures. (Backend kann auch Dokumente hochladen,...)
     * @return
     */
    protected static WebTarget getWebTargetWithMultiFeature() {
        if (client2 == null) {
            try {
                ClientConfig clientConfig = new ClientConfig();
                clientConfig.register(MultiPartFeature.class);
                client2 = ClientBuilder.newClient(clientConfig).register(AndroidFriendlyFeature.class);
            } catch (Exception e) {}
        }
        return client2.target((DEBUG_MODE ? BASEURL_OFFLINE : BASEURL_ONLINE));
    }

    /**
     * Diese Klasse wird benötigt, um Jersey mit Android kompatibel zu machen.
     */
    public static class AndroidFriendlyFeature implements Feature{

        @Override
        public boolean configure(FeatureContext context) {
            context.register(new AbstractBinder() {
                @Override
                protected void configure() {
                    addUnbindFilter(new Filter() {
                        @Override
                        public boolean matches(Descriptor d) {
                            String implClass = d.getImplementation();
                            return implClass.startsWith(
                                    "org.glassfish.jersey.message.internal.DataSource")
                                    || implClass.startsWith(
                                    "org.glassfish.jersey.message.internal.RenderedImage");
                        }
                    });
                }
            });
            return true;
        }
    }
}
