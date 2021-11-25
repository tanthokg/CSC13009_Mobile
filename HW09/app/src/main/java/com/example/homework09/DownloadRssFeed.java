package com.example.homework09;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DownloadRssFeed extends AsyncTask<String, Void, ArrayList<SingleItem>> {
    ShowHeadlines callerContext;
    String urlAddress, urlCaption;
    ProgressDialog dialog = null;

    public DownloadRssFeed(Context callerContext){
        this.callerContext = (ShowHeadlines) callerContext;
        dialog = new ProgressDialog(callerContext);
    }
    protected void onPreExecute() {
        this.dialog.setMessage("Please wait\nReading RSS feed ...");
        this.dialog.setCancelable(false);
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<SingleItem> result) {
        super.onPostExecute(result); callerContext.newsList = result;

        int layoutID = android.R.layout.simple_list_item_1;
        ArrayAdapter<SingleItem> adapterNews = new ArrayAdapter<SingleItem>(callerContext, layoutID, result);
        callerContext.myListView.setAdapter(adapterNews);
        dialog.dismiss();
    }

    public SingleItem dissectItemNode(NodeList nodeList, int i){

        try {
            Element entry = (Element) nodeList.item(i);
            Element title = (Element) entry.getElementsByTagName("title").item(0);
            Element description = (Element) entry.getElementsByTagName("description").item(0);
            Element pubDate = (Element) entry.getElementsByTagName("pubDate").item(0);
            Element link = (Element) entry.getElementsByTagName("link").item(0);

            String titleValue = title.getFirstChild().getNodeValue();
            String descriptionValue =description.getFirstChild().getNodeValue();
            String dateValue = pubDate.getFirstChild().getNodeValue();
            String linkValue = link.getFirstChild().getNodeValue();
            SingleItem singleItem = new SingleItem(dateValue, titleValue, descriptionValue, linkValue );

            return singleItem;
        }
        catch (DOMException e) { return new SingleItem("", "Error", e.getMessage(), null); }

    }

    @Override
    protected ArrayList<SingleItem> doInBackground(String... params) {
        ArrayList<SingleItem> newsList = new ArrayList<SingleItem>();
        urlAddress = params[0];
        urlCaption = params[1];
        this.dialog.setMessage("Please wait\nReading RSS feed" + urlCaption + "...");

        try {
            URL url = new URL(urlAddress);
            URLConnection connection;
            connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConnection.getInputStream();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                Document dom = db.parse(in);

                Element treeElements = dom.getDocumentElement();

                newsList.clear();
                NodeList itemNodes = treeElements.getElementsByTagName("item");
                if ((itemNodes != null) && (itemNodes.getLength() > 0)) {
                    for (int i = 0; i < itemNodes.getLength(); i++) {
                        newsList.add( dissectItemNode(itemNodes, i) );
                    }
                }
            }

            httpConnection.disconnect();
        }
        catch (Exception e) { Log.e("Error>> ", e.getMessage() ); }
        return newsList;
    }
}