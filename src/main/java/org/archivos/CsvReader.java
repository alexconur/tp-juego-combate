package org.archivos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class CsvReader {
  private CsvReader() {}

  public static List<List<String>> readResource(String resourcePath) {
    var cl = Thread.currentThread().getContextClassLoader();

    try (InputStream in = cl.getResourceAsStream(resourcePath)) {

      if (in == null) throw new IllegalArgumentException("Recurso no encontrado: " + resourcePath);

      try (var br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
        List<List<String>> rows = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
          line = line.strip();

          if (line.isEmpty() || line.startsWith("#")) continue;
          rows.add(List.of(line.split("\\s*[,;]\\s*")));
        }
        return rows;
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}