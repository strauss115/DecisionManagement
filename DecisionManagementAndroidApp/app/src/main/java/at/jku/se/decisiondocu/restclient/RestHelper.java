package at.jku.se.decisiondocu.restclient;

import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import at.jku.se.decisiondocu.BuildConfig;

/**
 * Created by Martin on 12.05.15.
 */
public class RestHelper {

    // -------------------------------------------------------------------------------------

    public static boolean DEBUG_MODE = false;

    public static final int PORT_TOMCAT = 8080;
    public static final int PORT_CHAT   = 2222;

    public static String HOST_OFFLINE = "192.168.0.102";
    public static final String HOST_ONLINE = "ubuntu.mayerb.net";
    public static final String REST_BASEDIR = "/DecisionDocu/api/";

    static {
        if (!BuildConfig.BASE_PATH.isEmpty()) {
            HOST_OFFLINE = BuildConfig.BASE_PATH;
        }
    }


    // -------------------------------------------------------------------------------------

    /**
     * Returns base url
     * @return Trimed base url
     */
    public static String GetBaseURL() {
        return GetBaseURL(true);
    }

    /**
     * Returns base url
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
     * Returns chat base url
     * @return
     */
    public static String GetBaseURLChat() {
        String url = (DEBUG_MODE ? HOST_OFFLINE : HOST_ONLINE) + ":" + PORT_CHAT;
        return url;
    }

    //private static final String BASEURL_OFFLINE = "http://www.oracle.com";
    //private static final String BASEURL_ONLINE = "http://www.oracle.com";

    private static Client client, client2, client3;

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

    protected static WebTarget getWebTargetWithChunckedFeature() {
        if (client3 == null) {
            try {
                client3 = ClientBuilder.newClient(new ClientConfig()).register(AndroidFriendlyFeature.class);
                client3.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");
            } catch (Exception e) {}
        }
        return client3.target(GetBaseURL(false));
    }

    /**
     * This class is needed to make jersy and android compatible
     */
    public static class AndroidFriendlyFeature implements Feature{

        /**
         * {@inheritDoc}
         */
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
