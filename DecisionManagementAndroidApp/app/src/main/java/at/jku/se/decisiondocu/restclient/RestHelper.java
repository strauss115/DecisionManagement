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

    // -------------------------------------------------------------------------------------

    public static boolean DEBUG_MODE = true;

    public static final int PORT_TOMCAT = 8080;
    public static final int PORT_CHAT   = 2222;

    public static final String HOST_OFFLINE = "192.168.0.104";
    public static final String HOST_ONLINE = "ubuntu.mayerb.net";
    public static final String REST_BASEDIR = "/DecisionDocu/api/";

    // -------------------------------------------------------------------------------------

    /**
     *
     * @return
     */
    public static String GetBaseURL() {
        return GetBaseURL(true);
    }

    /**
     *
     * @param trimEnd
     * @return
     */
    public static String GetBaseURL(boolean trimEnd) {
        String url = "http://" + (DEBUG_MODE ? HOST_OFFLINE : HOST_ONLINE) + ":" + PORT_TOMCAT + REST_BASEDIR;
        if (trimEnd && url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     *
     * @return
     */
    public static String GetBaseURLChat() {
        String url = (DEBUG_MODE ? HOST_OFFLINE : HOST_ONLINE) + ":" + PORT_CHAT;
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
        return client.target(GetBaseURL(false));
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
        return client2.target(GetBaseURL(false));
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
