package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

public class UserStorage {

    private static final String FILE_PATH = "test-output/user-data.json";

    public static void saveEmail(String email) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("email", email);
            Files.write(Paths.get(FILE_PATH), obj.toString().getBytes());
        } catch (IOException e) {
            System.out.println("Kullanıcı e-postası dosyaya yazılamadı: " + e.getMessage());
        }
    }

    public static String readEmail() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            JSONObject obj = new JSONObject(content);
            return obj.getString("email");
        } catch (Exception e) {
            System.out.println("E-posta okunamadı veya dosya bulunamadı.");
            return null;
        }
    }
}
