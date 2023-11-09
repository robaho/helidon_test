
import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;

import io.helidon.http.HeaderNames;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

public class SimpleFileServer {
    private static class DevNullService implements HttpService {
        @Override
        public void routing(HttpRules rules) {
            rules.get("/devnull", this::getHandler);
        }

        private void getHandler(ServerRequest request,
                                ServerResponse response) throws IOException {
            long size = Long.parseLong(request.query().first("size").orElseThrow());
            byte[] buffer = new byte[1024 * 1024];
            response.header(HeaderNames.CONTENT_TYPE, "application/octet-stream");
            response.status(200);
            response.contentLength(size);
            OutputStream os = response.outputStream();
            while (size > 0) {
                long len = Math.min(size, buffer.length);
                os.write(buffer, 0, (int) len);
                size -= len;
            }
            os.close();
        }
    }
    public static void main(String[] args) throws UnknownHostException {
        WebServer webServer = WebServer.builder()
          .host("localhost")
          .routing(r -> r.register(new DevNullService()))
          .port(8888)
          .build();

        webServer.start();
    }
}