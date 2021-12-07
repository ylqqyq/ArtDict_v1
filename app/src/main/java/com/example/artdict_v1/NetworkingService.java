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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import java.util.logging.LogRecord;

public class NetworkingService {
    String artURL = "https://api.artic.edu/api/v1/artworks/";
    String searchURL1 = "https://api.artic.edu/api/v1/artworks/search?q=";
    String SearchURL2 = "&limit=50";

    String imgURL1 = "https://www.artic.edu/iiif/2/";
    String imgURL2 = "/full/843,/0/default.jpg";
    String imgURL3 = "/full/200,/0/default.jpg";

    public static final ExecutorService networkingExecutor = Executors.newFixedThreadPool(4);
    static Handler networkHandler = new Handler(Looper.getMainLooper());

    interface networkingListener {
        void APIlistener(String jsonString);
        void APIImgListener(Bitmap image);
    }

    networkingListener listener;

    public void fetchArtListData(String text) {
        String completedURL = searchURL1 + text + SearchURL2;
        connect(completedURL);
    }

    public void fetchArtDetailData(int id) {
        String completedURL = artURL + id;
        connect(completedURL);
    }

    public void getThumbnailImageData(String img_id) {
        String completedURL = imgURL1 + img_id +imgURL3;
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
    public void getImageData(String img_id) {
        String completedURL = imgURL1 + img_id +imgURL2;
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
