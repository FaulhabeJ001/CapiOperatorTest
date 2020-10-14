package tmp.apps.CapiOperator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "operator.develop.httpclient")
public class DebugProperties {
    private boolean wiretap;
    private boolean skipssl;
    private Proxy proxy;

    public boolean isWiretap() {
        return wiretap;
    }

    public void setWiretap(boolean wiretap) {
        this.wiretap = wiretap;
    }

    public boolean isSkipssl() {
        return skipssl;
    }

    public void setSkipssl(boolean skipssl) {
        this.skipssl = skipssl;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public static class Proxy {
        private boolean active;
        private String host;
        private int port;

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
