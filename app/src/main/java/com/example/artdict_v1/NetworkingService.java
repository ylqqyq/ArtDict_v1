package com.example.artdict_v1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.LinkPermission;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import java.util.logging.LogRecord;

public class NetworkingService {

    String chiURL = "https://api.artic.edu/api/v1/artworks/";
    String chiSearchURL1 = "search?q=";
    String chiSearchURL2 = "&limit=20";

    String chiImgURL1 = "https://www.artic.edu/iiif/2/";
    String chiImgURL2 = "/full/843,/0/default.jpg";
    String chiImgURLs = "/full/200,/0/default.jpg";

    String rijksQURL = "https://www.rijksmuseum.nl/api/nl/collection?";
    private String apiKeyR = "key=VaUtnjGb";
    String rijksSearch = "&culture=en&imgonly=true&q=";
    String rijksDetailURL = "https://www.rijksmuseum.nl/api/nl/collection/";

    public static final ExecutorService networkingExecutor = Executors.newFixedThreadPool(4);
    static Handler networkHandler = new Handler(Looper.getMainLooper());

    interface networkingListener {
        void APIlistener(String jsonString);
        void APIImgListener(Bitmap image);
    }

    networkingListener listener;

    //fetch search results
    public void fetchArtListData(String text) {
        String completedURLChi = chiURL+chiSearchURL1 + text + chiSearchURL2;
        String completedURLRijks = rijksQURL+ apiKeyR + rijksSearch + text;
        connect(completedURLChi);
        connect(completedURLRijks);
    }

    public void fetchDetailData(int id) {
        String completedURLChi = chiURL + id;
        String completedURLRijks = rijksDetailURL + id + "?" + apiKeyR;
        connect(completedURLChi);
        connect(completedURLRijks);
    }

    //second call with object id to get img_id
    public void getImgID(String id) {
        String completedURL = chiURL + id;
        connect(completedURL);
    }

    public void getThumbnailImageDatafromChi(String img_id) {
        //parse img_id from object in model
        String completedURL = chiImgURL1 + img_id +chiImgURLs;
        //only get the image of smaller size
        networkingExecutor.execute((new Runnable() {
            @Override
            public void run() {
                URL urlObj = null;
                try {
                    urlObj = new URL(completedURL);
                    InputStream in = ((InputStream) urlObj.getContent());
                    Bitmap imageData = BitmapFactory.decodeStream(in);
                    networkHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.APIImgListener(imageData);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
    }
    public void getImageDatafromChi(String img_id) {
        String completedURL = chiImgURL1 + img_id +chiImgURL2;
        networkingExecutor.execute((new Runnable() {
            @Override
            public void run() {
                URL urlObj = null;
                try {
                    urlObj = new URL(completedURL);
                    InputStream in = ((InputStream) urlObj.getContent());
                    Bitmap imageData = BitmapFactory.decodeStream(in);
                    networkHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.APIImgListener(imageData);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public void getImageDatafromRijks(String img_url) {
        networkingExecutor.execute((new Runnable() {
            @Override
            public void run() {
                URL urlObj = null;
                try {
                    urlObj = new URL(img_url);
                    InputStream in = ((InputStream) urlObj.getContent());
                    Bitmap imageData = BitmapFactory.decodeStream(in);
                    networkHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.APIImgListener(imageData);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private void connect(String url) {
        networkingExecutor.execute(new Runnable() {
            String jsonString = "";
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL urlObj = new URL(url);
                    httpURLConnection = (HttpURLConnection) urlObj.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    int status = httpURLConnection.getResponseCode();

                    if((status >=200) && (status <=299)) {
                        InputStream in = httpURLConnection.getInputStream();
                        InputStreamReader reader = new InputStreamReader(in);
                        int read = 0;
                        while((read = reader.read()) != -1) {
                            char c = (char)read;
                            jsonString += c;
                        }
                        final String finalJson = jsonString;

                        networkHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.APIlistener(finalJson);
                            }
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    httpURLConnection.disconnect();
                }
            }
        });
    }

}
