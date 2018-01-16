# KIISGateway
KIISGateway adalah salah satu komponen dalam KIIS (Kepri Integrated Information System)

## Mekanisme Otentikasi Rest KIIS Gateway 
Untuk mengakses layanan KIISBigData harus melakukan otentikasi terlebih dahulu, yang mana hasil outputnya dalam bentuk json. contoh implementasi dalam beberapa bahasa pemrograman.
### Java

```
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
```
