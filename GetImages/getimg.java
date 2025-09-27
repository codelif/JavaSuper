package GetImages;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getimg {

  private static void downloadImages(String query, int limit, Path outputDir) throws IOException, InterruptedException {
    String API_KEY = System.getenv("API_KEY");
    String CX = System.getenv("CX");

    if (API_KEY == null && CX == null) {
      throw new IllegalStateException("Set API_KEY and CX in your environment.");
    }

    // custom search engine capping at 10 it is
    int num = Math.min(Math.max(limit, 1), 10);

    String searchUrl = String.format(
        "https://www.googleapis.com/customsearch/v1?q=%s&cx=%s&key=%s&searchType=image&num=%d", urlEncode(query), CX,
        API_KEY, num);

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(searchUrl)).GET().build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200) {
      throw new RuntimeException("Search failed: HTTP " + response.statusCode() + " — " + response.body());
    }

    // extracting image urls from json with regex to avoid json dependencies; no
    // maven, pom.xml shi- for me, add if required elsewhere and then change here
    // too
    // (also fsck the formatter for ruining my comment by formatting it)
    String json = response.body();
    // unescape a couple of common sequences just in case
    json = json.replace("\\/", "/").replace("\\u003d", "=");
    Pattern linkPattern = Pattern.compile("\"link\"\\s*:\\s*\"(https?[^\"\\\\]+)\"");
    Matcher m = linkPattern.matcher(json);

    List<String> urls = new ArrayList<>();
    while (m.find() && urls.size() < num) {
      urls.add(m.group(1));
    }

    if (urls.isEmpty()) {
      System.out.println("No image URLs found.");
      return;
    }

    Files.createDirectories(outputDir);

    int idx = 1;
    for (String url : urls) {
      try {
        HttpRequest imgReq = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<byte[]> imgResp = client.send(imgReq, HttpResponse.BodyHandlers.ofByteArray());

        if (imgResp.statusCode() != 200) {
          System.out.println("Skip (HTTP " + imgResp.statusCode() + "): " + url);
          continue;
        }
        String imgType = imgResp.headers().firstValue("Content-Type").orElse("");
        String ext = findExtension(imgType, url);

        Path file = outputDir.resolve(String.format("%02d%s", idx++, ext));
        Files.write(file, imgResp.body());
        System.out.println("Saved: " + file + " (" + url + ")");
      } catch (Exception e) {
        System.out.println("Failed to download: " + url + " — " + e.getMessage());
      }
    }
  }

  public void getImages(String query) throws Exception {
    downloadImages(query, 5, Path.of("images"));
  }

  private static String urlEncode(String s) {
    try {
      return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
    } catch (Exception e) {
      return s;
    }
  }

  private static String findExtension(String imgType, String url) {
    String ext = "";
    if (imgType != null) {
      switch (imgType.toLowerCase()) {
        case "image/jpeg":
        case "image/jpg":
          ext = ".jpg";
          break;
        case "image/png":
          ext = ".png";
          break;
        case "image/gif":
          ext = ".gif";
          break;
        case "image/webp":
          ext = ".webp";
          break;
        case "image/bmp":
          ext = ".bmp";
          break;
        case "image/svg+xml":
          ext = ".svg";
          break;
      }
    }
    if (ext.isEmpty()) {
      // Fallback: try to infer from the URL path
      String path = url;
      int q = path.indexOf('?');
      if (q >= 0)
        path = path.substring(0, q);
      int dot = path.lastIndexOf('.');
      if (dot >= 0 && dot >= path.length() - 6) { // simple guard
        ext = path.substring(dot).toLowerCase();
        // sanitize a bit
        if (!ext.matches("\\.(jpg|jpeg|png|gif|webp|bmp|svg)")) {
          ext = ".img";
        }
      } else {
        ext = ".img";
      }
    }
    return ext;
  }
}
