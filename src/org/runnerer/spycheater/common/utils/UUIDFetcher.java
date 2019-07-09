package org.runnerer.spycheater.common.utils;

import com.google.common.collect.ImmutableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.Callable;

public class UUIDFetcher
        implements Callable<Map<String, UUID>>
{

    private static final double PROFILES_PER_REQUEST = 100.0;
    private static final String PROFILE_URL = "https://api.mojang.com/profiles/minecraft";
    private final JSONParser jsonParser = new JSONParser();
    private final List<String> names;
    private final boolean rateLimiting;

    public UUIDFetcher(List<String> list, boolean bl)
    {
        this.names = ImmutableList.copyOf(list);
        this.rateLimiting = bl;
    }

    public UUIDFetcher(List<String> list)
    {
        this(list, true);
    }

    private static void writeBody(HttpURLConnection httpURLConnection, String string) throws Exception
    {
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(string.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private static HttpURLConnection createConnection() throws Exception
    {
        URL uRL = new URL("https://api.mojang.com/profiles/minecraft");
        HttpURLConnection httpURLConnection = (HttpURLConnection) uRL.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        return httpURLConnection;
    }

    private static UUID getUUID(String string)
    {
        return UUID.fromString(String.valueOf(string.substring(0, 8)) + "-" + string.substring(8, 12) + "-" + string.substring(12, 16) + "-" + string.substring(16, 20) + "-" + string.substring(20, 32));
    }

    public static byte[] toBytes(UUID uUID)
    {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uUID.getMostSignificantBits());
        byteBuffer.putLong(uUID.getLeastSignificantBits());
        return byteBuffer.array();
    }

    public static UUID fromBytes(byte[] arrby)
    {
        if (arrby.length != 16)
        {
            throw new IllegalArgumentException("Illegal byte array length: " + arrby.length);
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(arrby);
        long l = byteBuffer.getLong();
        long l2 = byteBuffer.getLong();
        return new UUID(l, l2);
    }

    public static UUID getUUIDOf(String string) throws Exception
    {
        return (UUID) new UUIDFetcher(Arrays.asList(string)).call().get(string);
    }

    @Override
    public Map<String, UUID> call() throws Exception
    {
        HashMap<String, UUID> hashMap = new HashMap<String, UUID>();
        int n = (int) Math.ceil((double) this.names.size() / 100.0);
        for (int i = 0; i < n; ++i)
        {
            HttpURLConnection httpURLConnection = UUIDFetcher.createConnection();
            String string = JSONArray.toJSONString(this.names.subList(i * 100, Math.min((i + 1) * 100, this.names.size())));
            UUIDFetcher.writeBody(httpURLConnection, string);
            JSONArray jSONArray = (JSONArray) this.jsonParser.parse((Reader) new InputStreamReader(httpURLConnection.getInputStream()));
            for (Object e : jSONArray)
            {
                JSONObject jSONObject = (JSONObject) e;
                String string2 = (String) jSONObject.get("id");
                String string3 = (String) jSONObject.get("name");
                UUID uUID = UUIDFetcher.getUUID(string2);
                hashMap.put(string3, uUID);
            }
            if (!this.rateLimiting || i == n - 1) continue;
            Thread.sleep(100L);
        }
        return hashMap;
    }
}

