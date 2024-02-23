package com.example.loginsqllite;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

public class PriceCalculator {
    public interface PriceCallback {
        void onPriceReceived(double marketPricePerKg);
        void onFailure(String errorMessage);
    }

    public static void getMarketPricePerKg(String selectedProduct, PriceCallback callback) {
        String apiKey = "579b464db66ec23bdd0000013c36f711bac04b8d6e4069e661ea7cd1";
        String apiUrl = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUrl).newBuilder();
        urlBuilder.addQueryParameter("api-key", apiKey);
        urlBuilder.addQueryParameter("format", "xml");
        urlBuilder.addQueryParameter("offset", "0");
        urlBuilder.addQueryParameter("limit", "10");
        urlBuilder.addQueryParameter("filters[commodity]", selectedProduct);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Accept", "application/xml")
                .get()
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(new StringReader(responseBody));

                    int eventType = parser.getEventType();
                    boolean foundMaxPrice = false;
                    double maxPrice = 0;

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG && "max_price".equals(parser.getName())) {
                            foundMaxPrice = true;
                        } else if (eventType == XmlPullParser.TEXT && foundMaxPrice) {
                            maxPrice = Double.parseDouble(parser.getText());
                            foundMaxPrice = false;
                        }

                        eventType = parser.next();
                    }

                    double marketPricePerKg = maxPrice / 100.0;
                    callback.onPriceReceived(marketPricePerKg);
                } else {
                    callback.onFailure("Failed to fetch market data.");
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
                callback.onFailure("Error during network operation.");
            }
        }).start();
    }
}
