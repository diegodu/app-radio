package com.example.lectorrss;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LectorRss extends AsyncTask<Void, Void, Void> {
    ArrayList<Noticia> noticias;

    Context context;
    RecyclerView recyclerView;
    String direccion = "https://www.ivoox.com/podcast-area-88_fg_f1250443_filtro_1.xml";

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    URL url;
    ProgressDialog progressDialog;

    public LectorRss(Context context, RecyclerView recyclerView){
        this.recyclerView = recyclerView;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");

    }
    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        AdapterNoticia adapterNoticia = new AdapterNoticia(noticias,context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapterNoticia);
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        procesarXml(obtenerDatos());
        return null;
    }

    private void procesarXml(Document data){
        if (data!=null){
          noticias = new ArrayList<>();
            Element root=data.getDocumentElement();
            Node channel=root.getChildNodes().item(0);
            NodeList items = channel.getChildNodes();
            for (int i=0;i<items.getLength();i++){
                Node hihoActual = items.item(i);
                if (hihoActual.getNodeName().equalsIgnoreCase("item")){
                    Noticia noticia = new Noticia();
                    NodeList itemsChild = hihoActual.getChildNodes();
                    for (int j=0;j<itemsChild.getLength();j++){
                        Node actural = itemsChild.item(j);
                       // Log.d("elementos: ",actural.getTextContent());
                        if(actural.getNodeName().equalsIgnoreCase("title")){
                            noticia.setnTitulo(actural.getTextContent());
                        }else if(actural.getNodeName().equalsIgnoreCase("link")){
                            noticia.setmEnlace(actural.getTextContent());
                        }else if(actural.getNodeName().equalsIgnoreCase("enclosure")){
                            String mUrl=actural.getAttributes().item(0).getTextContent();
                            noticia.setmAudio(mUrl);
                        }else if(actural.getNodeName().equalsIgnoreCase("description")){
                            noticia.setmDescripcion(actural.getTextContent());
                        }else if(actural.getNodeName().equalsIgnoreCase("pubDate")){
                            noticia.setmFechaPub(actural.getTextContent());
                        }else if(actural.getNodeName().equalsIgnoreCase("itunes:duration")){
                            noticia.setMduracion(actural.getTextContent());
                        }else if(actural.getNodeName().equalsIgnoreCase("itunes:episodeType")){
                            noticia.setmEpisodio(actural.getTextContent());
                        }else if(actural.getNodeName().equalsIgnoreCase("guid")){
                            noticia.setmGuia(actural.getTextContent());
                        }else if(actural.getNodeName().equalsIgnoreCase("itunes:image")){
                            String mUrl1=actural.getAttributes().item(0).getTextContent();
                            noticia.setmImagen(mUrl1);
                        }
                    }
                    noticias.add(noticia);
                    Log.d("TITULO: ", noticia.getnTitulo());
                    Log.d("ENLACE: ", noticia.getmEnlace());
                    Log.d("AUDIO: ", noticia.getmAudio());
                    Log.d("DESCRIPCION: ", noticia.getmDescripcion());
                    Log.d("FECHA PUBLICACION: ", noticia.getmFechaPub());
                    Log.d("DURACION: ", noticia.getMduracion());
                    Log.d("EPISODIO: ", noticia.getmEpisodio());
                    Log.d("GUIA: ", noticia.getmGuia());
                    Log.d("IMAGEN: ", noticia.getmImagen());

                }
            }
        }

    }
    public Document obtenerDatos(){
        try {
            url = new URL(direccion);
            HttpURLConnection conection = (HttpURLConnection)url.openConnection();
            conection.setRequestMethod("GET");
            InputStream inputStream = conection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(inputStream);
            return xmlDoc;
        }catch (Exception e){
            e.printStackTrace();
            return null;

        }
    }
}
